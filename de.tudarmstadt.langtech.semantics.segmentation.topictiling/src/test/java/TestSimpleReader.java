import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.JCasFactory;
import org.uimafit.pipeline.SimplePipeline;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.langtech.semantics.segmentation.segmenter.annotator.SimpleSegmenter;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TestSimpleReader {
	public static void main(String[] args) throws UIMAException, IOException {
//		String f = "test.txt";
//		JCas jcas = JCasFactory.createJCas();
//		jcas.setDocumentText(FileUtils.readFileToString(new File(f)));
//		AnalysisEngine segmenter = AnalysisEngineFactory.createPrimitive(SimpleSegmenter.class);
//		SimplePipeline.runPipeline(jcas, segmenter);
//		for(Sentence s:JCasUtil.select(jcas, Sentence.class)){
//			System.out.println(s.getCoveredText());
//			for (Token t: JCasUtil.selectCovered( Token.class,s)){
//				System.out.println(t.getCoveredText());
//			}	
//		}
		
		
	}
}
