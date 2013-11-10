

/* First created by JCasGen Tue Feb 21 09:57:17 CET 2012 */
package de.tudarmstadt.langtech.lda.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Apr 12 12:36:02 CEST 2012
 * XML source: /home/riedl/work/workspace/de.tudarmstadt.ukp.dkpro.lda/src/main/resources/desc/type/gibbsldatypes.xml
 * @generated */
public class Topic extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Topic.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Topic() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Topic(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Topic(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Topic(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
  //*--------------*
  //* Feature: topicId

  /** getter for topicId - gets 
   * @generated */
  public int getTopicId() {
    if (Topic_Type.featOkTst && ((Topic_Type)jcasType).casFeat_topicId == null)
      jcasType.jcas.throwFeatMissing("topicId", "de.tudarmstadt.ukp.dkpro.lda.type.Topic");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Topic_Type)jcasType).casFeatCode_topicId);}
    
  /** setter for topicId - sets  
   * @generated */
  public void setTopicId(int v) {
    if (Topic_Type.featOkTst && ((Topic_Type)jcasType).casFeat_topicId == null)
      jcasType.jcas.throwFeatMissing("topicId", "de.tudarmstadt.ukp.dkpro.lda.type.Topic");
    jcasType.ll_cas.ll_setIntValue(addr, ((Topic_Type)jcasType).casFeatCode_topicId, v);}    
   
    
  //*--------------*
  //* Feature: topicModeId

  /** getter for topicModeId - gets 
   * @generated */
  public int getTopicModeId() {
    if (Topic_Type.featOkTst && ((Topic_Type)jcasType).casFeat_topicModeId == null)
      jcasType.jcas.throwFeatMissing("topicModeId", "de.tudarmstadt.ukp.dkpro.lda.type.Topic");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Topic_Type)jcasType).casFeatCode_topicModeId);}
    
  /** setter for topicModeId - sets  
   * @generated */
  public void setTopicModeId(int v) {
    if (Topic_Type.featOkTst && ((Topic_Type)jcasType).casFeat_topicModeId == null)
      jcasType.jcas.throwFeatMissing("topicModeId", "de.tudarmstadt.ukp.dkpro.lda.type.Topic");
    jcasType.ll_cas.ll_setIntValue(addr, ((Topic_Type)jcasType).casFeatCode_topicModeId, v);}    
  }

    