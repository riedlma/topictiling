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
		
package de.tudarmstadt.langtech.semantics.segmentation.segmenter.annotator;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.langtech.semantics.type.SegmentScore;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class OutputSegments extends JCasAnnotator_ImplBase {
	public static final String PARAM_OUTPUT = "Output";
	@ConfigurationParameter(name = PARAM_OUTPUT, mandatory = false)
	private String output;
	private PrintStream ps;
	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		if(output==null){
			ps = System.out;
		}else{
			try {
				ps = new  PrintStream(output);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		ps.println("<documents>");
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ps.println("<document>");
		ps.println("<documentName>"+DocumentMetaData.get(aJCas).getDocumentTitle()+"</documentName>");
		ps.println("<segments>");
		Collection<SegmentScore> ss = JCasUtil.select(aJCas, SegmentScore.class);
		int i = 0;
		for (SegmentScore s : ss) {
			if(i==0){
				if(s.getBegin()!=0){
					ps.println("<segment>");
					ps.println("<depthScore></depthScore>");
					ps.println("<text>");
					ps.println(StringEscapeUtils.escapeXml(aJCas.getDocumentText().substring(0,s.getBegin())));
					ps.println("</text>");
					ps.println("</segment>");
				}
			}
			ps.println("<segment>");
//			ps.println("<similarityScores>"+s.getSimilarityScores()+"</similarityScores>");
			ps.println("<depthScore>"+s.getScore()+"</depthScore>");
			ps.println("<text>");
			ps.println(StringEscapeUtils.escapeXml(s.getCoveredText()));
			ps.println("</text>");
			ps.println("</segment>");
			i+=1;
		}
		ps.println("</segments>");
		ps.println("</document>");
	}
	@Override
	public void collectionProcessComplete()
			throws AnalysisEngineProcessException {
	ps.println("</documents>");
	ps.close();
		super.collectionProcessComplete();
	}

}
