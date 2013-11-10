
/* First created by JCasGen Wed Apr 11 15:17:37 CEST 2012 */
package de.tudarmstadt.langtech.lda.type;

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
 * Updated by JCasGen Thu Apr 12 12:36:03 CEST 2012
 * @generated */
public class TopicDistribution_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TopicDistribution_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TopicDistribution_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TopicDistribution(addr, TopicDistribution_Type.this);
  			   TopicDistribution_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TopicDistribution(addr, TopicDistribution_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TopicDistribution.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.lda.type.TopicDistribution");
 
  /** @generated */
  final Feature casFeat_topicDistribution;
  /** @generated */
  final int     casFeatCode_topicDistribution;
  /** @generated */ 
  public int getTopicDistribution(int addr) {
        if (featOkTst && casFeat_topicDistribution == null)
      jcas.throwFeatMissing("topicDistribution", "de.tudarmstadt.ukp.dkpro.lda.type.TopicDistribution");
    return ll_cas.ll_getRefValue(addr, casFeatCode_topicDistribution);
  }
  /** @generated */    
  public void setTopicDistribution(int addr, int v) {
        if (featOkTst && casFeat_topicDistribution == null)
      jcas.throwFeatMissing("topicDistribution", "de.tudarmstadt.ukp.dkpro.lda.type.TopicDistribution");
    ll_cas.ll_setRefValue(addr, casFeatCode_topicDistribution, v);}
    
   /** @generated */
  public double getTopicDistribution(int addr, int i) {
        if (featOkTst && casFeat_topicDistribution == null)
      jcas.throwFeatMissing("topicDistribution", "de.tudarmstadt.ukp.dkpro.lda.type.TopicDistribution");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getDoubleArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_topicDistribution), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_topicDistribution), i);
  return ll_cas.ll_getDoubleArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_topicDistribution), i);
  }
   
  /** @generated */ 
  public void setTopicDistribution(int addr, int i, double v) {
        if (featOkTst && casFeat_topicDistribution == null)
      jcas.throwFeatMissing("topicDistribution", "de.tudarmstadt.ukp.dkpro.lda.type.TopicDistribution");
    if (lowLevelTypeChecks)
      ll_cas.ll_setDoubleArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_topicDistribution), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_topicDistribution), i);
    ll_cas.ll_setDoubleArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_topicDistribution), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TopicDistribution_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_topicDistribution = jcas.getRequiredFeatureDE(casType, "topicDistribution", "uima.cas.DoubleArray", featOkTst);
    casFeatCode_topicDistribution  = (null == casFeat_topicDistribution) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_topicDistribution).getCode();

  }
}



    