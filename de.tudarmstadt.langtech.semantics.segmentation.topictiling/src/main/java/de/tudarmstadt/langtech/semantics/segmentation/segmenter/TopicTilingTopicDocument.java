package de.tudarmstadt.langtech.semantics.segmentation.segmenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jgibbslda.Inferencer;
import jgibbslda.LDACmdOption;
import jgibbslda.Model;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TopicTilingTopicDocument {
	public List<Double> similarityScores;
	public List<Integer> minimaPosition;
	public List<Double> depthScores;
	private Inferencer inf;
	private LDACmdOption opt;

	private int segmentNumber = -1;

	private int window = 1;
	private String ldaModelDirectory;
	private String ldaModelName;
	private int repeatSegmentation = 1;
	private int inferenceIterations = 100;
	private int repeatInference = 1;

	public TopicTilingTopicDocument(String ldaModelDirectory, String ldaModelName, int window, int repeatSegmentation, int repeatInference, int inferenceIteration) {
		this(ldaModelDirectory, ldaModelName, window, repeatSegmentation, repeatInference, inferenceIteration, -1);
	}

	public TopicTilingTopicDocument(String ldaModelDirectory, String ldaModelName, int window, int repeatSegmentation, int repeatInference, int inferenceIteration, int segmentNumber) {

		super();
		this.ldaModelDirectory = ldaModelDirectory;
		this.ldaModelName = ldaModelName;
		this.window = window;
		this.repeatInference = repeatInference;
		this.repeatSegmentation = repeatSegmentation;
		this.inferenceIterations = inferenceIteration;

		opt = new LDACmdOption();
		opt.dir = this.ldaModelDirectory;
		opt.modelName = this.ldaModelName;
		this.segmentNumber = segmentNumber;

	}

	public List<Integer> segment(List<List<Token>> sentences) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (segmentNumber < 0) {
			return segment2(sentences);
		}
		for (int i = 0; i < repeatSegmentation; i++) {

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
		for (int i = repeatSegmentation; i >= 0; i--) {
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
		System.out.println("SIM_TOPIC_TILING_DT: "+similarityScores);
		minimaPosition = getMinima();
		depthScores = getDepthScores();
		List<Integer> segments = new ArrayList<Integer>();
		if (segmentNumber < 0)
			segments = getSegments();
		else
			segments = getSegmentsNumberGiven();
		// add the last sentence as boundary if it is not set

		if (segments.size() > 1 && segments.get(segments.size() - 1) != sentences.size()) {
			segments.add(sentences.size() - 1);
		} else {
			System.err.println("segment size:" + segments.size());
			System.err.println("similarites: " + similarityScores);
		}
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
		// if(segments.size()==0){
		// System.err.println(depthScores);
		// System.err.println(similarityScores);
		// }
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
		double[][] topicDocument = null;
		for (i = 0; i < repeatInference; i++) {
			Model m = inference(partsArray);
			if (topicDocument == null) {
				topicDocument = new double[partsArray.length][m.K];
				for (int j = 0; j < partsArray.length; j++) {
					for (int k = 0; k < m.K; k++) {
						topicDocument[j][k] = 1.0;
					}
				}
			}
			for (int j = 0; j < partsArray.length; j++) {
				for (int k = 0; k < m.K; k++) {
					topicDocument[j][k] *= m.theta[j][k];
				}
			}
		}
		for (i = 0; i < partsArray.length - window; i++) {
			double[] v1 = topicDocument[i];
			double[] v2 = topicDocument[i + window];
			double sim = calculateDotProduct(v1, v2);
			similarities.add(sim);
		}
		// System.out.println(similarities.size());
		return similarities;
	}

	private List<Integer> getTopicCandidates(int[] topics) {
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		for (int m = repeatInference; m >= 0; m--) {

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

		inf.niters = inferenceIterations;
		// inf.niters = Integer.parseInt(prop.getProperty("infIteration"));
		Model m = inf.inference(sentences);
		return m;
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

	private double calculateDotProduct(double[] curr, double[] next) {
		double xy = 0;
		double sumX = 0;
		double sumY = 0;
		if (curr.length != next.length) {
			throw new IllegalArgumentException("Cosine Similarity: X != Y");
		}
		for (int i = 0; i < curr.length; i++) {
			double xi = curr[i];
			double yi = next[i];

			xy += xi * yi;
			sumX += xi * xi;
			sumY += yi * yi;
		}

		return 1.0 * xy / (Math.sqrt(sumX) * Math.sqrt(sumY));
	}

}
