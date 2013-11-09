

/* First created by JCasGen Fri Nov 08 16:28:12 CET 2013 */
package de.tudarmstadt.ukp.dkpro.ml.lda.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Nov 08 16:59:29 CET 2013
 * XML source: /Users/riedl/work/workspaces/intern4.2/de.tudarmstadt.langtech.semantics.segmentation.topictiling/src/main/resources/desc/type/GibbsLdaDescriptor.xml
 * @generated */
public class GibbsLdaTopic extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(GibbsLdaTopic.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected GibbsLdaTopic() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public GibbsLdaTopic(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public GibbsLdaTopic(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public GibbsLdaTopic(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: topic

  /** getter for topic - gets 
   * @generated */
  public int getTopic() {
    if (GibbsLdaTopic_Type.featOkTst && ((GibbsLdaTopic_Type)jcasType).casFeat_topic == null)
      jcasType.jcas.throwFeatMissing("topic", "de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
    return jcasType.ll_cas.ll_getIntValue(addr, ((GibbsLdaTopic_Type)jcasType).casFeatCode_topic);}
    
  /** setter for topic - sets  
   * @generated */
  public void setTopic(int v) {
    if (GibbsLdaTopic_Type.featOkTst && ((GibbsLdaTopic_Type)jcasType).casFeat_topic == null)
      jcasType.jcas.throwFeatMissing("topic", "de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
    jcasType.ll_cas.ll_setIntValue(addr, ((GibbsLdaTopic_Type)jcasType).casFeatCode_topic, v);}    
   
    
  //*--------------*
  //* Feature: termId

  /** getter for termId - gets 
   * @generated */
  public int getTermId() {
    if (GibbsLdaTopic_Type.featOkTst && ((GibbsLdaTopic_Type)jcasType).casFeat_termId == null)
      jcasType.jcas.throwFeatMissing("termId", "de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
    return jcasType.ll_cas.ll_getIntValue(addr, ((GibbsLdaTopic_Type)jcasType).casFeatCode_termId);}
    
  /** setter for termId - sets  
   * @generated */
  public void setTermId(int v) {
    if (GibbsLdaTopic_Type.featOkTst && ((GibbsLdaTopic_Type)jcasType).casFeat_termId == null)
      jcasType.jcas.throwFeatMissing("termId", "de.tudarmstadt.ukp.dkpro.ml.lda.type.GibbsLdaTopic");
    jcasType.ll_cas.ll_setIntValue(addr, ((GibbsLdaTopic_Type)jcasType).casFeatCode_termId, v);}    
  }

    