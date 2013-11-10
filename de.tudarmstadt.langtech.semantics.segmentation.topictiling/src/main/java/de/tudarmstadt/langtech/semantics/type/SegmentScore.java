

/* First created by JCasGen Fri Nov 08 16:51:38 CET 2013 */
package de.tudarmstadt.langtech.semantics.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Nov 08 16:59:21 CET 2013
 * XML source: /Users/riedl/work/workspaces/intern4.2/de.tudarmstadt.langtech.semantics.segmentation.topictiling/src/main/resources/desc/type/SegmentScore.xml
 * @generated */
public class SegmentScore extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SegmentScore.class);
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
  protected SegmentScore() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SegmentScore(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SegmentScore(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SegmentScore(JCas jcas, int begin, int end) {
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
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (SegmentScore_Type.featOkTst && ((SegmentScore_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.semantics.type.SegmentScore");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((SegmentScore_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (SegmentScore_Type.featOkTst && ((SegmentScore_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.semantics.type.SegmentScore");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((SegmentScore_Type)jcasType).casFeatCode_score, v);}    
  }

    