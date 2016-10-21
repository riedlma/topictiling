package de.tudarmstadt.langtech.lda;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitive;

import java.io.IOException;
import java.text.BreakIterator;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.xwriter.CASDumpWriter;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.JCasFactory;
import org.uimafit.pipeline.SimplePipeline;

import de.tudarmstadt.langtech.lda.annotator.GibbsLdaDocumentBasedTopicIdAnnotator;
import de.tudarmstadt.langtech.lda.annotator.GibbsLdaSentenceBasedTopicIdAnnotator;
import de.tudarmstadt.langtech.lda.annotator.GibbsLdaTopicIdAnnotator;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TestLdaTopicModelAnnotator {
	public static void main(String[] args) throws UIMAException, IOException {
		JCas jcas = getJCas();

		//sentence wise
		AnalysisEngine ae = AnalysisEngineFactory.createPrimitive(GibbsLdaSentenceBasedTopicIdAnnotator.class, 
				GibbsLdaTopicIdAnnotator.PARAM_LDA_MODEL_NAME, "model-final",
				GibbsLdaTopicIdAnnotator.PARAM_LDA_MODEL_DIR, "src/test/resources/model",
				GibbsLdaTopicIdAnnotator.PARAM_ANNOTATE_DOCUMENT_TOPIC_DISTRIBUTION,true,
				GibbsLdaTopicIdAnnotator.PARAM_ANNOTATE_WORD_TOPIC_DISTRIBUTION,true,
				GibbsLdaTopicIdAnnotator.PARAM_LDA_REPEAT_INFERENCE, 100
				);

		//document wise
		AnalysisEngine ae2 = AnalysisEngineFactory.createPrimitive(GibbsLdaDocumentBasedTopicIdAnnotator.class, 
				GibbsLdaTopicIdAnnotator.PARAM_LDA_MODEL_NAME, "model-final",
				GibbsLdaTopicIdAnnotator.PARAM_LDA_MODEL_DIR, "src/test/resources/model",
				GibbsLdaTopicIdAnnotator.PARAM_ANNOTATE_DOCUMENT_TOPIC_DISTRIBUTION,true,
				GibbsLdaTopicIdAnnotator.PARAM_ANNOTATE_WORD_TOPIC_DISTRIBUTION,true,
				GibbsLdaTopicIdAnnotator.PARAM_LDA_REPEAT_INFERENCE, 100
				);

		
		AnalysisEngine out = createPrimitive(CASDumpWriter.class);
		SimplePipeline.runPipeline(jcas, ae,out);
	}

	private static JCas getJCas() throws UIMAException {
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentLanguage("en");
		String text = "This is some example document. And there is more text";
		jcas.setDocumentText(text);
		DocumentMetaData metaData = new DocumentMetaData(jcas);
		metaData.setDocumentTitle("Titel");
		metaData.addToIndexes();
		BreakIterator boundary = BreakIterator.getWordInstance();
		

		// print each sentence in reverse order
		boundary.setText(text);
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			Token t = new Token(jcas, start, end);
			t.addToIndexes();
		}
		boundary = BreakIterator.getSentenceInstance();
		boundary.setText(text);
		
		start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			Sentence t = new Sentence(jcas, start, end);
			t.addToIndexes();
		}
		return jcas;

	}
}
