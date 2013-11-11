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
