package de.tudarmstadt.langtech.semantics.segmentation.segmenter;


import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ResourceInitializationException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.CollectionReaderFactory;
import org.uimafit.pipeline.SimplePipeline;

import de.tudarmstadt.langtech.semantics.segmentation.segmenter.annotator.OutputSegments;
import de.tudarmstadt.langtech.semantics.segmentation.segmenter.annotator.TopicTilingSegmenterAnnotator;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class RunTopicTilingOnFile {
	
	private static class Options {
		@Option(name="-tmd",usage="Directory of the topic model (GibbsLDA should be used)",required = true)
		String topicModelDirectory;
		@Option(name="-tmn",usage="Name of the topic model (GibbsLDA should be used)",required = true)
		String topicModelName;
		@Option(name="-dn",usage="Use the direct neighbor otherwise the highest neighbor will be used (default false)",required=false)
		boolean useDirectNeighbor=false;
		@Option(name="-i",usage="Number of inference iterations used to annotate words with topic IDs (default 100)",required=false)
		int inferenceIterations=100;
		@Option(name="-m",usage="Use mode counting (true/false) (default=true)",required=false)
		boolean modeCounting=true;
		@Option(name="-w",usage="Window size used to calculate the sentence similarity", required=false)
		int windowSize=1;
		@Option(name="-ri",usage="Use the repeated inference method",required = false)
		int repeatedInference=1;
		@Option(name="-rs",usage="Use the repeated segmentation",required = false)
		int repeatedSegmentation=1;
		@Option(name="-fd",usage="Directory fo the test files",required = true)
		public String fileDirectory;
		@Option(name="-fp",usage="File pattern for the test files",required = true)
		public String filePattern;
		@Option(name="-out",usage="File the content is written to (otherwise stdout will be used)",required = false)
		public String output=null;
//		@Option(name="-n",usage="Number of segments that should be made (the value -1 indicates, that segments are searched automatically)",required = true)
//		public String segmentNumber;
	}

	public static void main(final String[] args)
		throws ResourceInitializationException, UIMAException, IOException {
		Options options = new Options();
		CmdLineParser parser = new CmdLineParser(options);
		try {
		    parser.parseArgument(args);
		} catch( CmdLineException e ) {
		    System.err.println(e.getMessage());
		    System.err.println("java -jar myprogram.jar [options...] arguments...");
		    parser.printUsage(System.err);
		    return;
		}

		new RunTopicTilingOnFile(options);

	}

	public RunTopicTilingOnFile(Options opt) throws UIMAException, IOException {
		String neighbor = "HIGHEST_NEIGHBOR";
		if (opt.useDirectNeighbor)
			neighbor = "DIRECT_NEIGHBOR";
		final CollectionReader reader = CollectionReaderFactory.createCollectionReader(
				TextReader.class,
				TextReader.PARAM_PATH, opt.fileDirectory,
				TextReader.PARAM_PATTERNS, new String[] { "[+]" + opt.filePattern });
		
		AnalysisEngine segmenter = AnalysisEngineFactory.createPrimitive(StanfordSegmenter.class);
		AnalysisEngine topicTiling = AnalysisEngineFactory
					.createPrimitive(
							TopicTilingSegmenterAnnotator.class,
							TopicTilingSegmenterAnnotator.PARAM_LDA_MODEL_DIRECTORY,
							opt.topicModelDirectory,
							TopicTilingSegmenterAnnotator.PARAM_LDA_MODEL_NAME,
							opt.topicModelName,
							TopicTilingSegmenterAnnotator.PARAM_INFERENCE_ITERATION,
							opt.inferenceIterations,
							TopicTilingSegmenterAnnotator.PARAM_REPEAT_INFERENCE,
							opt.repeatedInference,
							TopicTilingSegmenterAnnotator.PARAM_REPEAT_SEGMENTATION,
							opt.repeatedSegmentation,
							TopicTilingSegmenterAnnotator.PARAM_WINDOW,
							opt.windowSize,
							TopicTilingSegmenterAnnotator.PARAM_DEPTH_SCORE,
							neighbor,
							TopicTilingSegmenterAnnotator.PARAM_MODE_COUNTING,
							opt.modeCounting);
		AnalysisEngine outputSegments = AnalysisEngineFactory.createPrimitive(OutputSegments.class,OutputSegments.PARAM_OUTPUT,opt.output);
		SimplePipeline.runPipeline(reader, segmenter, topicTiling,outputSegments);

	}

}
