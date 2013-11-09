
/* First created by JCasGen Fri Nov 08 16:28:43 CET 2013 */
package de.tudarmstadt.ukp.dkpro.semantics.type;

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

/** Saves the number of segments a document should consist of according to a given gold-standard.
 * Updated by JCasGen Fri Nov 08 16:59:47 CET 2013
 * @generated */
public class SegmentQuantity_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SegmentQuantity_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SegmentQuantity_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SegmentQuantity(addr, SegmentQuantity_Type.this);
  			   SegmentQuantity_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SegmentQuantity(addr, SegmentQuantity_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = SegmentQuantity.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.semantics.type.SegmentQuantity");
 
  /** @generated */
  final Feature casFeat_segmentCount;
  /** @generated */
  final int     casFeatCode_segmentCount;
  /** @generated */ 
  public int getSegmentCount(int addr) {
        if (featOkTst && casFeat_segmentCount == null)
      jcas.throwFeatMissing("segmentCount", "de.tudarmstadt.ukp.dkpro.semantics.type.SegmentQuantity");
    return ll_cas.ll_getIntValue(addr, casFeatCode_segmentCount);
  }
  /** @generated */    
  public void setSegmentCount(int addr, int v) {
        if (featOkTst && casFeat_segmentCount == null)
      jcas.throwFeatMissing("segmentCount", "de.tudarmstadt.ukp.dkpro.semantics.type.SegmentQuantity");
    ll_cas.ll_setIntValue(addr, casFeatCode_segmentCount, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public SegmentQuantity_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_segmentCount = jcas.getRequiredFeatureDE(casType, "segmentCount", "uima.cas.Integer", featOkTst);
    casFeatCode_segmentCount  = (null == casFeat_segmentCount) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_segmentCount).getCode();

  }
}



    