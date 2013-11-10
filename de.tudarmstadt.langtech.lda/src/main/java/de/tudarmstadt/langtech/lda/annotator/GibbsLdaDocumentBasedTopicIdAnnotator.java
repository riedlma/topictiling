package de.tudarmstadt.langtech.lda.annotator;

import static org.uimafit.util.JCasUtil.select;
import static org.uimafit.util.JCasUtil.selectCovered;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;



public class GibbsLdaDocumentBasedTopicIdAnnotator
	extends GibbsLdaTopicIdAnnotator {

	@Override
	public List<String>[] getDocuments(JCas jcas) {
		Collection<Sentence> sentences = select(jcas, Sentence.class);
		@SuppressWarnings("unchecked")
		List<String>[] arr = new ArrayList[1];
		arr[0]= new ArrayList<String>();
		for (Sentence s : sentences) {
			for (Token t : selectCovered(Token.class, s)) {
				arr[0].add(t.getCoveredText());
			}
		}
		
		return arr;
	}
	

}
