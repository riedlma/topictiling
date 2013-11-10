package de.tudarmstadt.langtech.semantics.segmentation.segmenter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;

import jgibbslda.Inferencer;
import jgibbslda.LDACmdOption;
import jgibbslda.Model;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TextTilingWindowOptimized {
	private int segmentNumber = -1;
	private int window = 1;
	private int additionalVectorSize = 1;
	public List<Double> similarityScores;
	public List<Integer> minimaPosition;
	public List<Double> depthScores;
	private Inferencer inf;
	private String ldaModel;
	private LDACmdOption opt;
	private Properties prop;
	private int segmentIteration = 5;
	private int inferenceIterationRepeating = 1;
	private int inferenceIteration;

	public TextTilingWindowOptimized(String ldaModel) {
		this(ldaModel, -1);
	}

	public TextTilingWindowOptimized(String ldaModel, int segmentNumber) {
		super();
		this.ldaModel = ldaModel;
		opt = new LDACmdOption();
		opt.dir = ldaModel;
		// opt.modelName = "model-final";
		this.segmentNumber = segmentNumber;
		prop = new Properties();
		try {
			prop.load(new FileReader("topictiling_config"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		opt.modelName = prop.getProperty("model_name");
		window = 1;
		inferenceIteration = 100;
		inferenceIterationRepeating  = 1;
		segmentIteration = 1;
		if (prop.containsKey("window")) 
			window = Integer.parseInt(prop.getProperty("window"));
		if (prop.containsKey("infIteration"))
			inferenceIteration = Integer.parseInt(prop.getProperty("infIteration"));
		if (prop.containsKey("infIterationRepeating"))
			inferenceIterationRepeating = Integer.parseInt(prop.getProperty("infIterationRepeating"));
		if (prop.containsKey("segmentIteration"))
			segmentIteration = Integer.parseInt(prop.getProperty("segmentIteration"));
		System.err.println("window:"+window);
		System.err.println("inferenceIteration:"+inferenceIteration);
		System.err.println("inferenceIterationRepeating:"+inferenceIterationRepeating);
		System.err.println("window:"+window);
	}

	public List<Integer> segment(List<List<Token>> sentences) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (segmentNumber < 0) {
			return segment2(sentences);
		}
		for (int i = 0; i < segmentIteration; i++) {

			List<Integer> segments = segment2(sentences);
			System.out.println(segments);
			for (int value : segments) {
				int count = 0;
				if (map.containsKey(value)) {
					count = map.get(value);
				}
				map.put(value, count + 1);

			}
		}
		System.out.println(map);
		List<Integer> segments = new ArrayList<Integer>();
		for (int i = segmentIteration; i >= 0; i--) {
			for (Entry<Integer, Integer> e : map.entrySet()) {
				if (e.getValue() == i) {
					segments.add(e.getKey());
					if (segments.size() == segmentNumber) {
						Collections.sort(segments);
						return segments;
					}
				}

			}
		}
		Collections.sort(segments);
		return segments;
	}

	public List<Integer> segment2(List<List<Token>> sentences) {

		similarityScores = getSimilarityScores(sentences);
		minimaPosition = getMinima();
		depthScores = getDepthScores();
		// System.out.println(sentences.size());
		// System.out.println(similarityScores.size());
		// System.out.println(minimaPosition.size());
		// System.out.println(depthScores.size());
		// System.out.println(similarityScores);
		List<Integer> segments = new ArrayList<Integer>();
		if (segmentNumber < 0)
			segments = getSegments();
		else
			segments = getSegmentsNumberGiven();
		// add the last sentence as boundary if it is not set
		
		if (segments.size()>1&&segments.get(segments.size() - 1) != sentences.size()) {
			segments.add(sentences.size() - 1);
		}else{
			System.err.println("segment size:"+segments.size());
			System.err.println("similarites: "+similarityScores);
		}
		// System.out.println(segments);
		return segments;
	}

	private List<Integer> getSegmentsNumberGiven() {
		List<Integer> segments = new ArrayList<Integer>(minimaPosition);
		List<Double> depths = depthScores;
		List<Double> depths2 = new ArrayList<Double>(depthScores);
		if (depths.size() > segmentNumber) {

			Collections.sort(depths);
			double min = depths.get(depths.size() - segmentNumber + 1);// save

			for (int i = segments.size() - 1; i >= 0; i--) {
				if (depths2.get(i) < min) {
					segments.remove(i);
				}
			}
		}
//if(segments.size()==0){
//	System.err.println(depthScores);
//	System.err.println(similarityScores);
//}
		return segments;
	}

	public List<Integer> getSegments() {
		// copy minima list
		List<Integer> segments = new ArrayList<Integer>(minimaPosition);

		double mean = calculateMean(depthScores);
		double variance = calculateVariance(depthScores, mean);
		double threshold = mean - variance / 2.0;

		for (int i = segments.size() - 1; i >= 0; i--) {
			if (depthScores.get(i) < threshold) {
				segments.remove(i);
			}
		}
		return segments;
	}

	private double calculateVariance(List<Double> vals, double mean) {
		double variance = 0.0;
		for (double d : vals) {
			variance += (d - mean) * (d - mean);
		}
		variance /= vals.size();
		return variance;
	}

	private double calculateMean(List<Double> vals) {
		double mean = 0.0;
		for (double d : vals) {
			mean += d;
		}
		mean /= vals.size();
		return mean;
	}

	private List<Double> getDepthScores() {
		List<Double> depths = new ArrayList<Double>();
		double depth;
		for (int i : minimaPosition) {

			depths.add(getDepths(i));
		}
		return depths;
	}

	// //left and right neighbor
	private double getDepths(int minimumPosition) {
		int i = minimumPosition;
		double depths = similarityScores.get(i - 1) - similarityScores.get(i)
				+ similarityScores.get(i + 1) - similarityScores.get(i);
		return depths;
	}

	// //highest left and right neighbor
	// private double getDepths(int minimumPosition){
	// double eps = 1e-2;
	// int li = minimumPosition-1;
	// double val = similarityScores.get(li);
	// while((li-1)>=0&&similarityScores.get(li-1)>(val-eps)){
	// li--;
	// val = similarityScores.get(li);
	// }
	//
	// int ri = minimumPosition+1;
	// val = similarityScores.get(ri);
	// while((ri+1)<similarityScores.size()&&similarityScores.get(ri+1)>(val-eps)){
	// ri++;
	// val = similarityScores.get(ri);
	// }
	// double depths = similarityScores.get(li) -
	// similarityScores.get(minimumPosition)
	// + similarityScores.get(ri) - similarityScores.get(minimumPosition);
	// return depths;
	// }

	private List<Integer> getMinima() {
		List<Integer> minima = new ArrayList<Integer>();
		double prev = 0;
		double curr = 0;
		double next = 1;
		for (int i = 1; i < similarityScores.size() - 1; i++) {
			if (next != curr) {
				prev = similarityScores.get(i - 1);
			}
			curr = similarityScores.get(i);
			next = similarityScores.get(i + 1);
			if (curr < next && curr < prev) {
				minima.add(i);
			}
		}
		return minima;

	}

	private List<Double> getSimilarityScores(List<List<Token>> sentences) {
		List<Double> similarities = new ArrayList<Double>();
		List<String> parts = new ArrayList<String>();
		for (int i = 0; i < sentences.size(); i++) {
			parts.add(getPrev(sentences, i));
		}
		for (int i = window - 1; i > 0; i--) {
			parts.add(getPrev(sentences, sentences.size() - 1, i));
		}
		String[] partsArray = new String[parts.size()];
		int i = 0;
		for (String ss : parts) {
			partsArray[i++] = ss;
		}
		// int seed = Integer.parseInt(prop.getProperty("seed"));
		// if(seed >-1){
		// MathUtil.setMathRandomSeed(seed);
		// }
		// inf = new Inferencer();
		// inf.init(opt);
		//
		// inf.niters = 100;
		// inf.niters = Integer.parseInt(prop.getProperty("infIteration"));
		// Model m = inf.inference(partsArray);
		// for (i = 0; i < partsArray.length - window; i++) {
		// int[] v1 = getVector(i, m);
		// int[] v2 = getVector(i + window, m);
		//
		// double sim = calculateDotProduct(v1, v2);
		//
		// similarities.add(sim);
		//
		// }
		Model m = inference(partsArray);
		if (inferenceIterationRepeating == 1) {
			for (i = 0; i < partsArray.length - window; i++) {
				int[] v1 = getVector(m.K, m.z[i]);
				int[] v2 = getVector(m.K, m.z[i + window]);
				double sim = calculateDotProduct(v1, v2);
				similarities.add(sim);
			}
			
		} else {
			// initialize save structure for word wise topic stabilization
			ArrayList<int[][]> values = new ArrayList<int[][]>();
			for (int k = 0; k < partsArray.length; k++) {
				values.add(new int[m.z[k].size()][m.K]);
			}
			for (int k = 1; k < inferenceIterationRepeating; k++) {
				for (int p = 0; p < partsArray.length; p++) {
					for (int t = 0; t < m.z[p].size(); t++) {
						int topic = m.z[p].get(t);
						values.get(p)[t][topic]++;
					}
				}
				m = inference(partsArray);
			}
			
			List<Integer>[] newZ = new ArrayList[partsArray.length];
			Random r = new Random();
			for (int s = 0; s < values.size(); s++) {
				int[][] sentence = values.get(s);
				newZ[s] = new ArrayList<Integer>();
				for (int t = 0; t < sentence.length; t++) {
					List<Integer> candidates = getTopicCandidates(sentence[t]);
					
					int topic = candidates.get(r.nextInt(candidates.size()));
					newZ[s].add(topic);
				}

			}
			for (i = 0; i < newZ.length - window; i++) {
				int[] v1 = getVector(m.K, newZ[i]);
				int[] v2 = getVector(m.K, newZ[i + window]);
				double sim = calculateDotProduct(v1, v2);
				similarities.add(sim);
			}

		}
		
		// System.out.println(similarities.size());
		return similarities;
	}

	private List<Integer> getTopicCandidates(int[] topics) {
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		for (int m = inferenceIterationRepeating; m >= 0; m--) {

			for (int t = 0; t < topics.length; t++) {
				if (topics[t] == m) {
					candidates.add(t);
				}
			}
			if (candidates.size() > 0) {
				return candidates;
			}
		}
		return null;
	}

	private int[] getVector(int topicNumber, Collection<Integer> topicAssigment) {
		int[] vec = new int[topicNumber];
		for (int k : topicAssigment) {
			vec[k]++;
		}
		return vec;
	}

	private Model inference(String[] sentences) {
		inf = new Inferencer();
		inf.init(opt);

		inf.niters = inferenceIteration;
		// inf.niters = Integer.parseInt(prop.getProperty("infIteration"));
		Model m = inf.inference(sentences);
		return m;
	}

	private double[] norm(int[] v1) {
		double sum = 0.0;
		for (int v : v1) {
			sum += v;
		}
		double[] vd = new double[v1.length];
		for (int i = 0; i < v1.length; i++) {
			vd[i] = v1[i] / sum;
		}
		return vd;
	}

	private int[] getVector(int i, Model m) {
		int[] vec = new int[m.K];
		for (int k : m.z[i]) {
			vec[k]++;
		}
		return vec;
	}

	private String getPrev(List<List<Token>> sentences, int i) {

		return getPrev(sentences, i, window);
	}

	private String getPrev(List<List<Token>> sentences, int i, int window) {
		String result = "";
		for (int k = i; k >= 0 && k > (i - window); k--) {
			for (Token t : sentences.get(k)) {
				result += t.getCoveredText() + " ";
			}
		}
		return result;
	}

	private double calculateDotProduct(double[] vd1, double[] vd2) {
		double xy = 0;
		double sumX = 0;
		double sumY = 0;
		if (vd1.length != vd2.length) {
			throw new IllegalArgumentException("Cosine Similarity: X != Y");
		}
		for (int i = 0; i < vd1.length; i++) {
			double xi = vd1[i];
			double yi = vd2[i];

			xy += xi * yi;
			sumX += xi * xi;
			sumY += yi * yi;
		}

		return 1.0 * xy / (Math.sqrt(sumX) * Math.sqrt(sumY));
	}

	private double calculateDotProduct(int[] curr, int[] next) {
		int xy = 0;
		int sumX = 0;
		int sumY = 0;
		if (curr.length != next.length) {
			throw new IllegalArgumentException("Cosine Similarity: X != Y");
		}
		for (int i = 0; i < curr.length; i++) {
			int xi = curr[i];
			int yi = next[i];

			xy += xi * yi;
			sumX += xi * xi;
			sumY += yi * yi;
		}

		return 1.0 * xy / (Math.sqrt(sumX) * Math.sqrt(sumY));
	}

}
