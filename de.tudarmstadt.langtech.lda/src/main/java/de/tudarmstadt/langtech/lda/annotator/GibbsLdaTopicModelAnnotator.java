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
