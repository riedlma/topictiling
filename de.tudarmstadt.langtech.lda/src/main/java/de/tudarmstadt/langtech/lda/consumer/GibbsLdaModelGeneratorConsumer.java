package de.tudarmstadt.langtech.lda.consumer;

import jgibbslda.Estimator;
import jgibbslda.LDACmdOption;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasConsumer_ImplBase;

public class GibbsLdaModelGeneratorConsumer extends JCasConsumer_ImplBase {

	@Override
	public void process(JCas aJCas)
		throws AnalysisEngineProcessException {
		LDACmdOption options = new LDACmdOption();
		Estimator es = new Estimator();
		es.init(options);
		
	}
	
}
