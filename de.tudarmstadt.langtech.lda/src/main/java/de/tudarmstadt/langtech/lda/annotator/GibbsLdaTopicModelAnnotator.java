package de.tudarmstadt.langtech.lda.annotator;

import java.util.ArrayList;
import java.util.List;

import jgibbslda.Dictionary;
import jgibbslda.Inferencer;
import jgibbslda.LDACmdOption;
import jgibbslda.Model;

import org.apache.uima.UimaContext;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;

/**
 * @author Martin Riedl
 */
public abstract class GibbsLdaTopicModelAnnotator extends JCasAnnotator_ImplBase{
	public static final String PARAM_LDA_MODEL_DIR = "LdaModelDir";
	public static final String PARAM_LDA_MODEL_NAME = "LdaModelName";
	public static final String PARAM_LDA_INFERENCE_ITERATIONS = "LdaInferenceIterations";
	public static final String PARAM_LDA_INFERENCE_SAVE_PATH = "LdaInferenceSavePath";
	

	
	@ConfigurationParameter(name = PARAM_LDA_INFERENCE_SAVE_PATH, mandatory = false)
	private String ldaInferenceSavePath;
	private String ldaInferenceSaveName;

	public String getLdaInferenceSaveName() {
		return ldaInferenceSaveName;
	}
	public void setLdaInferenceSaveName(String ldaInferenceSaveName) {
		this.ldaInferenceSaveName = ldaInferenceSaveName;
	}
	@ConfigurationParameter(name = PARAM_LDA_MODEL_DIR, mandatory = true)
	private String ldaModelDir;
	@ConfigurationParameter(name = PARAM_LDA_MODEL_NAME, mandatory = true)
	private String ldaModelName;
	@ConfigurationParameter(name = PARAM_LDA_INFERENCE_ITERATIONS, mandatory = false, description = "Inference iterations used to built topic distribution for new model", defaultValue = "100")
	private int ldaInferenceIteration;
	
	private Inferencer inferencer;

//	public Model inference(String[] documents) {
//		Model m =  inferencer.inference(documents);
//		if(ldaInferenceSavePath!=null){
//			m.dir = ldaInferenceSavePath;
//			m.saveModel("inference_"+ldaInferenceSaveName);
//		}
//		return m;
//	}
	
	public Model inference(List<String>[] documents) {
		Model m =  inferencer.inference(documents);
		if(ldaInferenceSavePath!=null){
			m.dir = ldaInferenceSavePath;
			m.saveModel("inference_"+ldaInferenceSaveName);
		}
		return m;
	}
	public int getInferenceNiters() {
		return inferencer.niters;
	}

	public ArrayList<int[][]> getInferenceModeValues() {
		return inferencer.values;
	}
	
	public Dictionary getInferencerGlobalDict(){
		return inferencer.globalDict;
	}
	
	
	@Override
	public void initialize(UimaContext context)
		throws ResourceInitializationException {
		super.initialize(context);
		LDACmdOption options = new LDACmdOption();
		options.dir = ldaModelDir;
		options.modelName = ldaModelName;
		options.niters = ldaInferenceIteration;
		//Initiliaze inferencer
		inferencer = new Inferencer();
		inferencer.init(options);
	}
	
	

}
