package de.tudarmstadt.langtech.semantics.segmentation.segmenter.annotator;

import java.text.BreakIterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class SimpleSegmenter extends JCasAnnotator_ImplBase{
	public static  final String PARAM_TOKEN_BOUNDARY="TokenBoundary";
	public static  final String PARAM_SENTENCE_BOUNDARY="SentenceBoundary";
	@ConfigurationParameter(name = PARAM_SENTENCE_BOUNDARY,mandatory=false)
	private char sentenceBoundary = '\n';
	@ConfigurationParameter(name = PARAM_TOKEN_BOUNDARY,mandatory=false)
	private char tokenBoundary = ' ';
	
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		String txt = aJCas.getDocumentText();
		int prevToken = 0;
		int prevSentence = 0;
		System.out.println(txt);
		int i =0;
		for (i=0;i<txt.length();i++){
			
			if (txt.charAt(i)==sentenceBoundary && i-prevSentence>0){
				Sentence s = new Sentence(aJCas,prevSentence,i);
				s.addToIndexes();
				prevSentence=i+1;
				Token t = new Token(aJCas,prevToken,i);
				t.addToIndexes();
				prevToken=i+1;
			}
			if (txt.charAt(i)==tokenBoundary && i-prevToken>0){
				Token t = new Token(aJCas,prevToken,i);
				t.addToIndexes();				
				prevToken=i+1;
			}
			
		}
		if (i-prevSentence>0){
			Sentence s = new Sentence(aJCas,prevSentence,i);
			s.addToIndexes();
			Token t = new Token(aJCas,prevToken,i);
			t.addToIndexes();
		}
	}
	

}
