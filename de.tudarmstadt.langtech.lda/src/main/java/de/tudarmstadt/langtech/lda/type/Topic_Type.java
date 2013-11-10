
/* First created by JCasGen Tue Feb 21 09:57:17 CET 2012 */
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
public class Topic_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Topic_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Topic_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Topic(addr, Topic_Type.this);
  			   Topic_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Topic(addr, Topic_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Topic.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.lda.type.Topic");



  /** @generated */
  final Feature casFeat_topicId;
  /** @generated */
  final int     casFeatCode_topicId;
  /** @generated */ 
  public int getTopicId(int addr) {
        if (featOkTst && casFeat_topicId == null)
      jcas.throwFeatMissing("topicId", "de.tudarmstadt.ukp.dkpro.lda.type.Topic");
    return ll_cas.ll_getIntValue(addr, casFeatCode_topicId);
  }
  /** @generated */    
  public void setTopicId(int addr, int v) {
        if (featOkTst && casFeat_topicId == null)
      jcas.throwFeatMissing("topicId", "de.tudarmstadt.ukp.dkpro.lda.type.Topic");
    ll_cas.ll_setIntValue(addr, casFeatCode_topicId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_topicModeId;
  /** @generated */
  final int     casFeatCode_topicModeId;
  /** @generated */ 
  public int getTopicModeId(int addr) {
        if (featOkTst && casFeat_topicModeId == null)
      jcas.throwFeatMissing("topicModeId", "de.tudarmstadt.ukp.dkpro.lda.type.Topic");
    return ll_cas.ll_getIntValue(addr, casFeatCode_topicModeId);
  }
  /** @generated */    
  public void setTopicModeId(int addr, int v) {
        if (featOkTst && casFeat_topicModeId == null)
      jcas.throwFeatMissing("topicModeId", "de.tudarmstadt.ukp.dkpro.lda.type.Topic");
    ll_cas.ll_setIntValue(addr, casFeatCode_topicModeId, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Topic_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_topicId = jcas.getRequiredFeatureDE(casType, "topicId", "uima.cas.Integer", featOkTst);
    casFeatCode_topicId  = (null == casFeat_topicId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_topicId).getCode();

 
    casFeat_topicModeId = jcas.getRequiredFeatureDE(casType, "topicModeId", "uima.cas.Integer", featOkTst);
    casFeatCode_topicModeId  = (null == casFeat_topicModeId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_topicModeId).getCode();

  }
}



    