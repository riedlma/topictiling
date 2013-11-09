package de.tudarmstadt.ukp.dkpro.semantics.segmentation.segmenter.annotator;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.semantics.type.SegmentScore;

public class OutputSegments extends JCasAnnotator_ImplBase {
	public static final String PARAM_OUTPUT = "Output";
	@ConfigurationParameter(name = PARAM_OUTPUT, mandatory = false)
	private String output;
	private PrintStream ps;
	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		// TODO Auto-generated method stub
		super.initialize(context);
		if(output==null){
			ps = System.out;
		}else{
			try {
				ps = new  PrintStream(output);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
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
		for (SegmentScore s : JCasUtil.select(aJCas, SegmentScore.class)) {
			// SegmentScore score = JCasUtil.select(SegmentScore.class,
			// s).get(0);
			ps.println("<segment>");
			ps.println("<depthScore>"+s.getScore()+"</depthScore>");
			ps.println("<text>");
			ps.println(StringEscapeUtils.escapeXml(s.getCoveredText()));
			ps.println("</text>");
			ps.println("</segment>");
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
