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

package de.tudarmstadt.langtech.lda.annotator;

import static org.uimafit.util.JCasUtil.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import jgibbslda.Model;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.DoubleArray;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.uimafit.descriptor.ConfigurationParameter;

import de.tudarmstadt.langtech.lda.type.Topic;
import de.tudarmstadt.langtech.lda.type.TopicDistribution;
import de.tudarmstadt.langtech.lda.type.WordTopicDistribution;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public abstract class GibbsLdaTopicIdAnnotator extends
		GibbsLdaTopicModelAnnotator {
	public static final String PARAM_LDA_REPEAT_INFERENCE = "LdaRepeatInference";
	public static final String PARAM_ANNOTATE_DOCUMENT_TOPIC_DISTRIBUTION = "LdaAnnotateDocumentTopicDistribution";
	public static final String PARAM_ANNOTATE_WORD_TOPIC_DISTRIBUTION = "LdaAnnotateWordTopicDistribution";

	private static final Logger log = UIMAFramework
			.getLogger(GibbsLdaTopicIdAnnotator.class);
	@ConfigurationParameter(name = PARAM_LDA_REPEAT_INFERENCE, mandatory = false, defaultValue = "1")
	private int ldaRepeatInference;

	@ConfigurationParameter(name = PARAM_ANNOTATE_DOCUMENT_TOPIC_DISTRIBUTION, mandatory = false, defaultValue = "false")
	private boolean ldaAnnotateDocumentTopicDistribution = false;

	@ConfigurationParameter(name = PARAM_ANNOTATE_WORD_TOPIC_DISTRIBUTION, mandatory = false, defaultValue = "false")
	private boolean ldaAnnotateWordTopicDistribution = false;
	
	/**
	 * Function iterates over all tokens and assigns a topic ID. This can only
	 * be performed, when the token is within the model.
	 * 
	 * @param jcas
	 * @param z
	 */

	private void annotateTokenWithTopicId(JCas jcas, List<Integer>[] modelZ,
			List<Integer>[] modelModeZ, List<String>[] documents) {

		int si = 0;
		int ti = 0;
		int zti = 0;
		int actDocumentSize = 0;
		List<String> wordTokens = null;
		StringBuffer output = new StringBuffer();

		if (documents.length > 0) {
			wordTokens = documents[0];
			actDocumentSize = wordTokens.size();
		}

		for (Token t : select(jcas, Token.class)) {
			if (zti >= actDocumentSize) {
				ti = 0;
				zti = 0;
				si++;
				wordTokens = documents[si];
				actDocumentSize = wordTokens.size();
			}
			String token = t.getCoveredText();
			assert token.equals(wordTokens.get(zti));
			// System.out.print("indices: " + si + "\t" + ti + "\tsize: "
			// + modelZ[si].size() + " " + modelModeZ[si].size());
			// System.out.println("\t" + token + " "+ wordTokens.get(zti));
			if (getInferencerGlobalDict().word2id.containsKey(token)) {
				int topicId = modelZ[si].get(ti);
				int topicModeId = modelModeZ[si].get(ti);
				Topic topic = new Topic(jcas, t.getBegin(), t.getEnd());
				topic.setTopicId(topicId);
				topic.setTopicModeId(topicModeId);
				topic.addToIndexes();

				ti++;

				output.append(token).append(":").append(topicId).append(":")
						.append(topicModeId);

			} else {
				output.append(token).append(":NA");
			}
			output.append(" ");
			zti++;

		}
		log.log(Level.FINE, output.toString());
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		final List<String>[] documents = getDocuments(jcas);
		DocumentMetaData metaData = DocumentMetaData.get(jcas);
		super.setLdaInferenceSaveName(metaData.getDocumentTitle());
		Model m = inference(documents);
		// if no inference is repeated z contains the topic IDs that are used
		List<Integer>[] modelZ = m.z;
		List<Integer>[] modelModeZ;

		modelModeZ = getTopicListFromRepeated(getInferenceModeValues(),
				documents, getInferenceNiters(), 1);
		if (ldaRepeatInference > 1) {
			// initialize save structure for word wise topic stabilization
			ArrayList<int[][]> values = new ArrayList<int[][]>();
			for (int k = 0; k < documents.length; k++) {
				values.add(new int[modelZ[k].size()][m.K]);
			}
			for (int k = 1; k < ldaRepeatInference; k++) {
				for (int p = 0; p < documents.length; p++) {
					for (int t = 0; t < modelZ[p].size(); t++) {
						int topic = modelZ[p].get(t);
						values.get(p)[t][topic]++;
					}
				}
				m = inference(documents);
				modelZ = m.z;
				modelModeZ = getTopicListFromRepeated(getInferenceModeValues(),
						documents, getInferenceNiters(), 1);
			}
		}
		annotateTokenWithTopicId(jcas, modelZ, modelModeZ, documents);
		if (ldaAnnotateDocumentTopicDistribution)
			annotateDocumentsWithTopicDistribution(jcas, documents, m);
		if(ldaAnnotateWordTopicDistribution)
			annotateWordsWithTopicDistribution(jcas,m);
	}

	private void annotateWordsWithTopicDistribution(JCas jcas, Model m) {
		
		HashMap<String,DoubleArray> map = new HashMap<String, DoubleArray>();
		for(int wi =0;wi< m.phi.length;wi++){
			double[] topics=m.phi[wi];
			String word = getInferencerGlobalDict().id2word.get(wi);
			DoubleArray arr = new DoubleArray(jcas, topics.length);
			for(int ti=0;ti<topics.length;ti++){
				arr.set(ti, topics[ti]);
			}
			map.put(word, arr);
		}
		for (Token t : select(jcas, Token.class)) {
		
			DoubleArray arr = map.get(t.getCoveredText());
			if(arr!=null){
				WordTopicDistribution wtd = new WordTopicDistribution(jcas,t.getBegin(),t.getEnd());
				wtd.setTopicDistribution(arr);
				wtd.addToIndexes();
			}
			
		}
	}

	private void annotateDocumentsWithTopicDistribution(JCas jcas,
			List<String>[] documents, Model m) {
		int si = 0;
		int ti = 0;
		int start = -1;
		int docSize = documents[si].size();
		for (Token t : select(jcas, Token.class)) {
			if (start < 0) {
				docSize = documents[si].size();
				start = t.getBegin();
			}
			ti++;
			if (ti == docSize) {
				TopicDistribution td = new TopicDistribution(jcas, start,
						t.getEnd());
				start = -1;
				DoubleArray arr = new DoubleArray(jcas, m.K);
				for (int i = 0; i < m.theta[si].length; i++) {
					arr.set(i, m.theta[si][i]);
				}
				td.setTopicDistribution(arr);
				td.addToIndexes();

				si++;

				ti = 0;
			}

		}
	}

	private List<Integer>[] getTopicListFromRepeated(ArrayList<int[][]> values,
			List<String>[] partsArray, int max, int min) {
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
				} else {
					System.out.println("No Candidates found");

					System.out.println();
				}

			}

		}
		return newZ;

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

	public abstract List<String>[] getDocuments(JCas jcas);
}
