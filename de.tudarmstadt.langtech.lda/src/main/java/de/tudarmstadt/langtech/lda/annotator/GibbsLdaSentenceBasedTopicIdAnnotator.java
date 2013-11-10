package de.tudarmstadt.langtech.lda.annotator;

import static org.uimafit.util.JCasUtil.select;
import static org.uimafit.util.JCasUtil.selectCovered;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class GibbsLdaSentenceBasedTopicIdAnnotator extends
		GibbsLdaTopicIdAnnotator {


	public List<String>[] getDocuments(JCas jcas) {
		Collection<Sentence> sentences = select(jcas, Sentence.class);
		@SuppressWarnings("unchecked")
		List<String>[] arr = new ArrayList[sentences.size()];
		int i = 0;
		for (Sentence s : select(jcas, Sentence.class)) {
			System.out.println(s.getCoveredText());
		}
		for (Sentence s : sentences) {
			StringBuffer line = new StringBuffer();
			arr[i] = new ArrayList<String>();
			for (Token t : selectCovered(Token.class, s)) {
				line.append(t.getCoveredText());
				line.append(" ");
				arr[i].add(t.getCoveredText());
			}
			i++;
		}

		return arr;
	}

}
