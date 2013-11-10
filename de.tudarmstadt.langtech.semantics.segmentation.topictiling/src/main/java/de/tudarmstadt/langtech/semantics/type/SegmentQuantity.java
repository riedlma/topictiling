

/* First created by JCasGen Fri Nov 08 16:28:43 CET 2013 */
package de.tudarmstadt.langtech.semantics.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Saves the number of segments a document should consist of according to a given gold-standard.
 * Updated by JCasGen Fri Nov 08 16:59:47 CET 2013
 * XML source: /Users/riedl/work/workspaces/intern4.2/de.tudarmstadt.langtech.semantics.segmentation.topictiling/src/main/resources/desc/type/Segment.xml
 * @generated */
public class SegmentQuantity extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SegmentQuantity.class);
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
  protected SegmentQuantity() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SegmentQuantity(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SegmentQuantity(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SegmentQuantity(JCas jcas, int begin, int end) {
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
  //* Feature: segmentCount

  /** getter for segmentCount - gets 
   * @generated */
  public int getSegmentCount() {
    if (SegmentQuantity_Type.featOkTst && ((SegmentQuantity_Type)jcasType).casFeat_segmentCount == null)
      jcasType.jcas.throwFeatMissing("segmentCount", "de.tudarmstadt.ukp.dkpro.semantics.type.SegmentQuantity");
    return jcasType.ll_cas.ll_getIntValue(addr, ((SegmentQuantity_Type)jcasType).casFeatCode_segmentCount);}
    
  /** setter for segmentCount - sets  
   * @generated */
  public void setSegmentCount(int v) {
    if (SegmentQuantity_Type.featOkTst && ((SegmentQuantity_Type)jcasType).casFeat_segmentCount == null)
      jcasType.jcas.throwFeatMissing("segmentCount", "de.tudarmstadt.ukp.dkpro.semantics.type.SegmentQuantity");
    jcasType.ll_cas.ll_setIntValue(addr, ((SegmentQuantity_Type)jcasType).casFeatCode_segmentCount, v);}    
  }

    