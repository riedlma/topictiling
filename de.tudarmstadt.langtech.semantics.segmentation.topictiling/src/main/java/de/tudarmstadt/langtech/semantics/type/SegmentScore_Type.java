
/* First created by JCasGen Fri Nov 08 16:51:38 CET 2013 */
package de.tudarmstadt.langtech.semantics.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed Aug 26 15:50:04 CEST 2015
 * @generated */
public class SegmentScore_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SegmentScore_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SegmentScore_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SegmentScore(addr, SegmentScore_Type.this);
  			   SegmentScore_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SegmentScore(addr, SegmentScore_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = SegmentScore.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.langtech.semantics.type.SegmentScore");
 
  /** @generated */
  final Feature casFeat_score;
  /** @generated */
  final int     casFeatCode_score;
  /** @generated */ 
  public double getScore(int addr) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "de.tudarmstadt.langtech.semantics.type.SegmentScore");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_score);
  }
  /** @generated */    
  public void setScore(int addr, double v) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "de.tudarmstadt.langtech.semantics.type.SegmentScore");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_score, v);}
    
  
 
  /** @generated */
  final Feature casFeat_similarityScores;
  /** @generated */
  final int     casFeatCode_similarityScores;
  /** @generated */ 
  public String getSimilarityScores(int addr) {
        if (featOkTst && casFeat_similarityScores == null)
      jcas.throwFeatMissing("similarityScores", "de.tudarmstadt.langtech.semantics.type.SegmentScore");
    return ll_cas.ll_getStringValue(addr, casFeatCode_similarityScores);
  }
  /** @generated */    
  public void setSimilarityScores(int addr, String v) {
        if (featOkTst && casFeat_similarityScores == null)
      jcas.throwFeatMissing("similarityScores", "de.tudarmstadt.langtech.semantics.type.SegmentScore");
    ll_cas.ll_setStringValue(addr, casFeatCode_similarityScores, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public SegmentScore_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_score = jcas.getRequiredFeatureDE(casType, "score", "uima.cas.Double", featOkTst);
    casFeatCode_score  = (null == casFeat_score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_score).getCode();

 
    casFeat_similarityScores = jcas.getRequiredFeatureDE(casType, "similarityScores", "uima.cas.String", featOkTst);
    casFeatCode_similarityScores  = (null == casFeat_similarityScores) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_similarityScores).getCode();

  }
}



    