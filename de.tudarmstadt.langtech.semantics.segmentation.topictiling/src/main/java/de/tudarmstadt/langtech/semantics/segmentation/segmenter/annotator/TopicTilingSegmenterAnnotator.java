package de.tudarmstadt.langtech.semantics.segmentation.segmenter.annotator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.langtech.semantics.segmentation.segmenter.TextTilingWindowOptimized;
import de.tudarmstadt.langtech.semantics.segmentation.segmenter.TopicTiling;
import de.tudarmstadt.langtech.semantics.type.Segment;
import de.tudarmstadt.langtech.semantics.type.SegmentQuantity;
import de.tudarmstadt.langtech.semantics.type.SegmentScore;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TopicTilingSegmenterAnnotator extends JCasAnnotator_ImplBase {
	private boolean printSegments = false;

	public static final String PARAM_USE_ASSIGNED_TOPICS = "UseAssgnedTopics";
	public static final String PARAM_LDA_MODEL_DIRECTORY = "LdaModelDirectory";
	public static final String PARAM_LDA_MODEL_NAME = "LdaModelName";
	public static final String PARAM_WINDOW = "TopicTilingWindow";
	public static final String PARAM_REPEAT_SEGMENTATION = "RepeatedSegmentation";
	public static final String PARAM_INFERENCE_ITERATION = "InferenceIteration";
	public static final String PARAM_REPEAT_INFERENCE = "RepeatedInference";
	public static final String PARAM_DEPTH_SCORE = "DepthScore";
	public static final String PARAM_MODE_COUNTING = "ModeCounting";

	@ConfigurationParameter(name = PARAM_USE_ASSIGNED_TOPICS, mandatory = false)
	private boolean useAssignedTopics = false;;
	@ConfigurationParameter(name = PARAM_LDA_MODEL_DIRECTORY, mandatory = true)
	private String ldaModelDirectory;
	@ConfigurationParameter(name = PARAM_LDA_MODEL_NAME, mandatory = true)
	private String ldaModelName;
	@ConfigurationParameter(name = PARAM_WINDOW, mandatory = true)
	private int window;
	@ConfigurationParameter(name = PARAM_REPEAT_INFERENCE, mandatory = true)
	private int repeatInferences;
	@ConfigurationParameter(name = PARAM_REPEAT_SEGMENTATION, mandatory = true)
	private int repeatSegmentation;
	@ConfigurationParameter(name = PARAM_INFERENCE_ITERATION, mandatory = true)
	private int inferenceIteration;
	@ConfigurationParameter(name = PARAM_MODE_COUNTING, mandatory = true)
	private boolean modeCounting;

	@ConfigurationParameter(name = PARAM_DEPTH_SCORE, mandatory = true)
	private String depthScore;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);

	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		List<List<Token>> s = new ArrayList<List<Token>>();

		// int i = 0;
		Iterator<Segment> segments = JCasUtil.select(jcas, Segment.class)
				.iterator();
		Segment seg = null;
		if (segments.hasNext())
			seg = segments.next();

		for (Sentence ss : JCasUtil.select(jcas, Sentence.class)) {

			s.add(JCasUtil.selectCovered(Token.class, ss));

		}

		DocumentMetaData meta = DocumentMetaData.get(jcas);
		StringBuffer buffer = new StringBuffer();
		buffer.append(meta.getDocumentTitle());

		buffer.append("\n");
		// TopicTilingTopicDocument tttd ;

		TopicTiling tt;
		tt = new TopicTiling(ldaModelDirectory, ldaModelName, window,
				repeatSegmentation, repeatInferences, inferenceIteration,
				modeCounting, depthScore, useAssignedTopics);
		buffer.append("GOL: " + getGoldSegments(jcas) + "\n");
		List<Integer> segmentPositions;
		if (JCasUtil.select(jcas, SegmentQuantity.class).size() == 0) {
			segmentPositions = tt.segment(s);
		} else {
			int segNum = JCasUtil.select(jcas, SegmentQuantity.class)
					.iterator().next().getSegmentCount();
			segmentPositions = tt.segment(s, segNum);
		}

		// buffer.append("Similarities:" + tt.similarityScores + "\n");
		// buffer.append("SEG:" + segmentPositions);
		// System.out.println(buffer.toString());
		// System.out.println("SEG: "+segmentPositions);
		// System.out.println(buffer.toString());
		// List<Integer> segmentPositions2 = tttd.segment(s);
		// print(jcas,segmentPositions);
		// printRcode(jcas, segmentCounts, wtt2, segmentPositionsWnew);
		annotateSegments(jcas, segmentPositions, tt.depthScores,
				tt.minimaPosition);
	}

	private void printRcode(JCas jcas, int segmentCount,
			TextTilingWindowOptimized tt, List<Integer> segments) {
		// if (!printRcode)
		// return;
		DocumentMetaData metaData = DocumentMetaData.get(jcas);
		;
		String main = metaData.getDocumentTitle()
				+ ": Cosine Similarity between sentences ";
		if (segmentCount < 0)
			main = main + " (segments given: " + segmentCount + ")";
		StringBuffer buffer = new StringBuffer();
		buffer.append("#Cosine Similarity\n");
		buffer.append("pdf(file='" + metaData.getDocumentTitle()
				+ ".pdf',20,7);\n");
		buffer.append(toListInR(tt.similarityScores, "cos"));
		buffer.append(toListInR(segments, "estSeg"));
		buffer.append(toListInR(getGoldSegments(jcas), "seg"));
		buffer.append(toListInR(tt.minimaPosition, "canSeg"));
		buffer.append(toListInR(tt.depthScores, "depth"));
		// buffer.append(toListInR(getSentenceLengths(jcas), "senLen"));
		// buffer.append("X11();");
		// buffer.append("dev.new(width=20,heigth=7);");
		buffer.append("plot(0:"
				+ (tt.similarityScores.size() - 1)
				+ ",cos,type='l',xlab='Sentence',ylab='cosine similarity',main='"
				+ main + "');\n");
		// buffer.append("lines(senLen);\n");
		buffer.append("abline(v=seg,col='red',lty=5);\n");
		buffer.append("abline(v=estSeg,col='green',lwd=2,lty=4);\n");
		buffer.append("abline(v=seg[seg%in%estSeg],col='black',lwd=3);\n");
		buffer.append("points(estSeg,rep(max(cos)*0.98," + segments.size()
				+ "),col='green',pch=22);\n");
		buffer.append("points(canSeg,rep(max(cos)*0.9,"
				+ tt.minimaPosition.size() + "),col='blue',pch=23);\n");
		buffer.append("text(canSeg[-length(canSeg)],rep(max(cos)*c(0.84,0.88,0.92,0.94),length="
				+ tt.depthScores.size() + "),labels=depth);\n");
		// buffer.append("dev.copy(pdf,'"+m.getDocumentTitle()+".pdf')\n");
		buffer.append("dev.off();dev.off()");
		System.out.println(buffer.toString());

	}

	private List<Integer> getGoldSegments(JCas jcas) {

		List<Integer> ret = new ArrayList<Integer>();
		Iterator<Segment> segIt = JCasUtil.iterator(jcas, Segment.class);
		int sentenceCount = -1;
		while (segIt.hasNext()) {
			Segment seg = segIt.next();
			for (Sentence s : JCasUtil.selectCovered(jcas, Sentence.class, seg)) {
				sentenceCount++;
			}
			ret.add(sentenceCount);
		}
		return ret;
	}

	private <T> StringBuffer toListInR(List<T> list, String name) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(name);
		buffer.append("=c(");
		for (T sc : list) {
			if (sc instanceof Double) {
				DecimalFormat df = new DecimalFormat("#.##");
				buffer.append(df.format(sc).replace(",", "."));
			} else {
				buffer.append(sc);
			}
			buffer.append(",");
		}
		if (list.size() > 0)
			buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(");\n");
		return buffer;
	}

	private void annotateSegments(JCas jcas, List<Integer> segmentPositions,
			List<Double> depthScores, List<Integer> minimaPosition) {
		Iterator<Sentence> sentenceItr = JCasUtil
				.iterator(jcas, Sentence.class);
		int sentenceCount = -1;
		int prevBreak = 0;

		for (final int sBreak : segmentPositions) {
			final SegmentScore score = new SegmentScore(jcas);
//			final Segment seg = new Segment(jcas);

			Sentence segmentSentence = null;

			int beginOffset = 0;
			int endOffset = 0;

			// move sentenceItr to last sentence in segment
			for (; sentenceCount < sBreak; sentenceCount++) {
				segmentSentence = sentenceItr.next();

				if (sentenceCount == prevBreak) {
					beginOffset = segmentSentence.getBegin();
				}
			}

			if (segmentSentence != null) {
				endOffset = segmentSentence.getEnd();
			}
			score.setBegin(beginOffset);
//			seg.setBegin(beginOffset);
			score.setEnd(endOffset);
//			seg.setEnd(endOffset);
			int idx = minimaPosition.indexOf(sBreak);
			if (idx < 0) {
				score.setScore(1.0);
			} else {
				score.setScore(depthScores.get(idx));
			}
			score.addToIndexes();
//			seg.addToIndexes();

			if (printSegments) {
				System.out.println(sBreak + "\t" + sentenceCount + "\t"
						+ beginOffset + "\t" + endOffset);
			}
			prevBreak = sBreak;
		}
	}

	/**
	 * expects a list with the sentencenumber that will be segmented
	 * 
	 * @param jcas
	 * @param sentenceBreaks
	 */
	private void annotateSegments(JCas jcas, List<Integer> sentenceBreaks) {
		Iterator<Sentence> sentenceItr = JCasUtil
				.iterator(jcas, Sentence.class);
		int sentenceCount = -1;
		int prevBreak = 0;
		if (printSegments) {
			System.out.println("Annotated Segments");
			System.out.println(sentenceBreaks.toString());
		}

		for (final int sBreak : sentenceBreaks) {
			final Segment seg = new Segment(jcas);

			Sentence segmentSentence = null;

			int beginOffset = 0;
			int endOffset = 0;

			// move sentenceItr to last sentence in segment
			for (; sentenceCount < sBreak; sentenceCount++) {
				segmentSentence = sentenceItr.next();

				if (sentenceCount == prevBreak) {
					beginOffset = segmentSentence.getBegin();
				}
			}

			if (segmentSentence != null) {
				endOffset = segmentSentence.getEnd();
			}

			seg.setBegin(beginOffset);
			seg.setEnd(endOffset);
			seg.addToIndexes();

			if (printSegments) {
				System.out.println(sBreak + "\t" + sentenceCount + "\t"
						+ beginOffset + "\t" + endOffset);
			}
			prevBreak = sBreak;
		}
	}
}
