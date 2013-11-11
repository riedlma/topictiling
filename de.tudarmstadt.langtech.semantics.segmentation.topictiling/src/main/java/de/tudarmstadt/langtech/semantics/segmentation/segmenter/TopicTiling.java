/**
 *	Martin Riedl
 *	riedl@cs.tu-darmstadt.de
 *  FG Language Technology
 * 	Technische Universität Darmstadt, Germany
 * 
 * 
 *  This file is part of TopicTiling.
 *
 *  TopicTiling is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  TopicTiling is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with TopicTiling.  If not, see <http://www.gnu.org/licenses/>.
 */


package de.tudarmstadt.langtech.semantics.segmentation.segmenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import jgibbslda.Inferencer;
import jgibbslda.LDACmdOption;
import jgibbslda.Model;

import org.uimafit.util.JCasUtil;

import de.tudarmstadt.langtech.lda.type.Topic;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TopicTiling {
	public enum DepthScore {
		DIRECT_NEIGHBOR, HIGHEST_NEIGHBOR
	}

	public boolean useAssignedTopics = false;
	public List<String> sw;
	public List<Double> similarityScores;
	public List<Integer> minimaPosition;
	public List<Double> depthScores;
	private Inferencer inf;
	private LDACmdOption opt;
	private DepthScore depthScore;
	private int segmentNumber = -1;
	// private double[] weights = null;
	private int window = 1;
	private String ldaModelDirectory;
	private String ldaModelName;
	private int repeatSegmentation;
	private int inferenceIterations;
	private int repeatInference;
	// private boolean countAssignments=true;
	private int repeatInferenceMinimumCount = 1;
	private boolean modeCounting = false;

	public TopicTiling(String ldaModelDirectory, String ldaModelName,
			int window, int repeatSegmentation, int repeatInference,
			int inferenceIteration, boolean modeCounting, String depthScore,
			boolean topicsAssigned) {
		super();
		useAssignedTopics = topicsAssigned;

		sw = new ArrayList<String>();
		
		this.depthScore = DepthScore.valueOf(depthScore);
		this.ldaModelDirectory = ldaModelDirectory;
		this.ldaModelName = ldaModelName;
		this.window = window;
		this.repeatInference = repeatInference;
		this.repeatSegmentation = repeatSegmentation;
		this.inferenceIterations = inferenceIteration;
		this.modeCounting = modeCounting;
		if (!useAssignedTopics) {
			opt = new LDACmdOption();
			opt.dir = this.ldaModelDirectory;
			opt.modelName = this.ldaModelName;
			inf = new Inferencer();
			inf.init(opt);
			inf.niters = inferenceIterations;
		}
		
	}

	public TopicTiling(String ldaModelDirectory, String ldaModelName,
			int window, int repeatSegmentation, int repeatInference,
			int inferenceIteration, boolean modeCounting, String depthScore) {

		this(ldaModelDirectory, ldaModelName, window, repeatSegmentation,
				repeatInference, inferenceIteration, modeCounting, depthScore,
				false);
	}

	public List<Integer> segment(List<List<Token>> sentences) {
		return segment(sentences, -1);
	}

	public List<Integer> segment(List<List<Token>> sentences, int segmentNumber) {
		this.segmentNumber = segmentNumber;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	
		for (int i = 0; i < repeatSegmentation; i++) {

			List<Integer> segments = segment2(sentences);
			for (int value : segments) {
				int count = 0;
				if (map.containsKey(value)) {
					count = map.get(value);
				}
				map.put(value, count + 1);

			}
		}
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

	private List<Integer> segment2(List<List<Token>> sentences) {

		similarityScores = getSimilarityScores(sentences);

		minimaPosition = getMinima();
		depthScores = getDepthScores();
		List<Integer> segments = new ArrayList<Integer>();
		if (segmentNumber < 0)
			segments = getSegments();
		else
			segments = getSegmentsNumberGiven();
		// add the last sentence as boundary if it is not set

		if (segments.size() > 1
				&& segments.get(segments.size() - 1) != sentences.size()) {
			segments.add(sentences.size() - 1);
		}
		else {
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
	
		return segments;
	}

	private void printTopicWordAnnotation(List<Integer>[] newZ,
			List<Integer>[] otherZ, String[] partsArray, Model m) {

		
		int i = 0;
		for (String s : partsArray) {
			int j = 0;
			String[] tokens = s.split(" ");
			String other = new String();
			for (String t : tokens) {
				if (inf.globalDict.word2id.containsKey(t)) {
					System.out.print(t + ":" + newZ[i].get(j) + " ");
					other += t + ":" + otherZ[i].get(j) + " ";
					j++;
				}
				else {
					System.out.print(t + " ");
					other += t + " ";
				}
			}
			System.out.println();
			System.out.println(other);

			i++;
		}
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
			if (depthScore.equals(DepthScore.DIRECT_NEIGHBOR))
				depths.add(getDirectNeighborDepths(i));
			else
				depths.add(getHighestNeighborDepths(i));
		}
		return depths;
	}

	// //left and right neighbor
	private double getDirectNeighborDepths(int minimumPosition) {
		int i = minimumPosition;
		double depths = (similarityScores.get(i - 1) - similarityScores.get(i)
				+ similarityScores.get(i + 1) - similarityScores.get(i))/2.0;
		return depths;
	}

	// highest left and right neighbor
	private double getHighestNeighborDepths(int minimumPosition) {
		double eps = 1e-10;
		int li = minimumPosition - 1;
		double val = similarityScores.get(li);
		while ((li - 1) >= 0 && similarityScores.get(li - 1) > (val - eps)) {
			li--;
			val = similarityScores.get(li);
		}

		int ri = minimumPosition + 1;
		val = similarityScores.get(ri);
		while ((ri + 1) < similarityScores.size()
				&& similarityScores.get(ri + 1) > (val - eps)) {
			ri++;
			val = similarityScores.get(ri);
		}
		double depths = (similarityScores.get(li)
				- similarityScores.get(minimumPosition)
				+ similarityScores.get(ri)
				- similarityScores.get(minimumPosition))/2.0;
		return depths;
	}

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

		Model m = null;
		List<Integer>[] modelZ;
		if (useAssignedTopics) {
			@SuppressWarnings("unchecked")
			int i = 0;
			List<Integer>[] newZ = new ArrayList[sentences.size()];
			for (List<Token> sentence : sentences) {
				List<Integer> topicIdList = new ArrayList<Integer>();
				for (Token token : sentence) {
					List<Topic> topicIds = JCasUtil.selectCovered(Topic.class,
							token);
					// List<GibbsLdaTopic> topicIds =
					// JCasUtil.selectCovered(GibbsLdaTopic.class, token);

					if (topicIds.size() == 1) {
						if (modeCounting)
							topicIdList.add(topicIds.get(0).getTopicModeId());
						else
							topicIdList.add(topicIds.get(0).getTopicId());
					}
				}
				System.out.println(topicIdList);
				newZ[i++] = topicIdList;
			}
			for (i = 0; i < newZ.length - window; i++) {
				similarities.add(calculateSimilarity(100, i, newZ, null));
			}
			

		}
		else {
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
			m = inference(partsArray);
			modelZ = m.z;

			// modification
			if (modeCounting)
				modelZ = getTopicListFromRepeated(inf.values, partsArray,
						inferenceIterations, 1);
			if (repeatInference == 1) {
				for (i = 0; i < partsArray.length - window; i++) {
							similarities.add(calculateSimilarity(m.K, i, modelZ,
							partsArray));

				}
		
			}
			else {
				// initialize save structure for word wise topic stabilization
				ArrayList<int[][]> values = new ArrayList<int[][]>();
				for (int k = 0; k < partsArray.length; k++) {
					values.add(new int[modelZ[k].size()][m.K]);
				}
				for (int k = 1; k < repeatInference; k++) {
					for (int p = 0; p < partsArray.length; p++) {
						for (int t = 0; t < modelZ[p].size(); t++) {
							int topic = modelZ[p].get(t);
							values.get(p)[t][topic]++;
						}
					}
					m = inference(partsArray);
					modelZ = m.z;
					// modification
					if (modeCounting)
						modelZ = getTopicListFromRepeated(inf.values,
								partsArray, inferenceIterations, 1);
					}
				List<Integer>[] newZ = getTopicListFromRepeated(values,
						partsArray, repeatInference,
						repeatInferenceMinimumCount);
				for (i = 0; i < newZ.length - window; i++) {
					similarities.add(calculateSimilarity(m.K, i, newZ,
							partsArray));
				}
				
			}
		}

		return similarities;
	}

	
	private List<Integer>[] getTopicListFromRepeated(ArrayList<int[][]> values,
			String[] partsArray, int max, int min) {
		@SuppressWarnings("unchecked")
		List<Integer>[] newZ = new ArrayList[values.size()];
		Random r = new Random();
		for (int s = 0; s < values.size(); s++) {
			int[][] sentence = values.get(s);
			newZ[s] = new ArrayList<Integer>();
			for (int t = 0; t < sentence.length; t++) {
				List<Integer> candidates = getTopicCandidates(sentence[t], max,
						min);
				if (candidates.size() > 0) {
					int topic = candidates.get(r.nextInt(candidates.size()));
					newZ[s].add(topic);
				}
				else {
					System.out.println("No Candidates found");

				}

			}

		}
		return newZ;

	}

	private double calculateSimilarity(int topicNumber, int i,
			Collection<Integer>[] z, String[] partsArray) {

		double[] v1 = getVector(topicNumber, z[i]);
		double[] v2 = getVector(topicNumber, z[i + window]);
		String arrV = Arrays.toString(v1);
		
		double sim = calculateDotProduct(v1, v2);
		if (Double.isNaN(sim)) {
			return 1.0;
		}
		else {
			return sim;
		}
	}

	private List<Integer> getTopicCandidates(int[] topics, int max, int min) {
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		for (int m = max; m >= min; m--) {

			for (int t = 0; t < topics.length; t++) {
				if (topics[t] == m) {
					candidates.add(t);
				}
			}
			if (candidates.size() > 0) {
				return candidates;
			}
		}
		return new ArrayList<Integer>();
	}

	private double[] getVector(int topicNumber,
			Collection<Integer> topicAssigment) {
		double[] vec = new double[topicNumber];
		for (int k : topicAssigment) {
			vec[k]++;
		}
		return vec;
	}

	private Model inference(String[] sentences) {
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
				String text = t.getCoveredText().trim();
				if (!sw.contains(text))
					result += t.getCoveredText() + " ";
				else {
				}
			}
		}
		return result;
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
