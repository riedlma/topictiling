
/* First created by JCasGen Fri Nov 08 16:28:12 CET 2013 */
package de.tudarmstadt.langtech.ml.lda.type;

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
 * Updated by JCasGen Fri Nov 08 16:59:29 CET 2013
 * @generated */
public class GibbsLdaTopic_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (GibbsLdaTopic_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = GibbsLdaTopic_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new GibbsLdaTopic(addr, GibbsLdaTopic_Type.this);
  			   GibbsLdaTopic_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new GibbsLdaTopic(addr, GibbsLdaTopic_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = GibbsLdaTopic.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
 
  /** @generated */
  final Feature casFeat_topic;
  /** @generated */
  final int     casFeatCode_topic;
  /** @generated */ 
  public int getTopic(int addr) {
        if (featOkTst && casFeat_topic == null)
      jcas.throwFeatMissing("topic", "de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
    return ll_cas.ll_getIntValue(addr, casFeatCode_topic);
  }
  /** @generated */    
  public void setTopic(int addr, int v) {
        if (featOkTst && casFeat_topic == null)
      jcas.throwFeatMissing("topic", "de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
    ll_cas.ll_setIntValue(addr, casFeatCode_topic, v);}
    
  
 
  /** @generated */
  final Feature casFeat_termId;
  /** @generated */
  final int     casFeatCode_termId;
  /** @generated */ 
  public int getTermId(int addr) {
        if (featOkTst && casFeat_termId == null)
      jcas.throwFeatMissing("termId", "de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
    return ll_cas.ll_getIntValue(addr, casFeatCode_termId);
  }
  /** @generated */    
  public void setTermId(int addr, int v) {
        if (featOkTst && casFeat_termId == null)
      jcas.throwFeatMissing("termId", "de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
    ll_cas.ll_setIntValue(addr, casFeatCode_termId, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public GibbsLdaTopic_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_topic = jcas.getRequiredFeatureDE(casType, "topic", "uima.cas.Integer", featOkTst);
    casFeatCode_topic  = (null == casFeat_topic) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_topic).getCode();

 
    casFeat_termId = jcas.getRequiredFeatureDE(casType, "termId", "uima.cas.Integer", featOkTst);
    casFeatCode_termId  = (null == casFeat_termId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_termId).getCode();

  }
}



    