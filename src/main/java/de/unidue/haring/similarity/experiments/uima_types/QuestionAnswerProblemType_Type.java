
/* First created by JCasGen Mon Jan 15 21:21:13 CET 2018 */
package de.unidue.haring.similarity.experiments.uima_types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Mon Jan 15 21:21:20 CET 2018
 * @generated */
public class QuestionAnswerProblemType_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = QuestionAnswerProblemType.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
 
  /** @generated */
  final Feature casFeat_instanceText;
  /** @generated */
  final int     casFeatCode_instanceText;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getInstanceText(int addr) {
        if (featOkTst && casFeat_instanceText == null)
      jcas.throwFeatMissing("instanceText", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_instanceText);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setInstanceText(int addr, String v) {
        if (featOkTst && casFeat_instanceText == null)
      jcas.throwFeatMissing("instanceText", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    ll_cas.ll_setStringValue(addr, casFeatCode_instanceText, v);}
    
  
 
  /** @generated */
  final Feature casFeat_questionText;
  /** @generated */
  final int     casFeatCode_questionText;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getQuestionText(int addr) {
        if (featOkTst && casFeat_questionText == null)
      jcas.throwFeatMissing("questionText", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_questionText);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setQuestionText(int addr, String v) {
        if (featOkTst && casFeat_questionText == null)
      jcas.throwFeatMissing("questionText", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    ll_cas.ll_setStringValue(addr, casFeatCode_questionText, v);}
    
  
 
  /** @generated */
  final Feature casFeat_answerText1;
  /** @generated */
  final int     casFeatCode_answerText1;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAnswerText1(int addr) {
        if (featOkTst && casFeat_answerText1 == null)
      jcas.throwFeatMissing("answerText1", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_answerText1);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAnswerText1(int addr, String v) {
        if (featOkTst && casFeat_answerText1 == null)
      jcas.throwFeatMissing("answerText1", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    ll_cas.ll_setStringValue(addr, casFeatCode_answerText1, v);}
    
  
 
  /** @generated */
  final Feature casFeat_answerText2;
  /** @generated */
  final int     casFeatCode_answerText2;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAnswerText2(int addr) {
        if (featOkTst && casFeat_answerText2 == null)
      jcas.throwFeatMissing("answerText2", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_answerText2);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAnswerText2(int addr, String v) {
        if (featOkTst && casFeat_answerText2 == null)
      jcas.throwFeatMissing("answerText2", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    ll_cas.ll_setStringValue(addr, casFeatCode_answerText2, v);}
    
  
 
  /** @generated */
  final Feature casFeat_QuestionAnswerProblemId;
  /** @generated */
  final int     casFeatCode_QuestionAnswerProblemId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getQuestionAnswerProblemId(int addr) {
        if (featOkTst && casFeat_QuestionAnswerProblemId == null)
      jcas.throwFeatMissing("QuestionAnswerProblemId", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return ll_cas.ll_getIntValue(addr, casFeatCode_QuestionAnswerProblemId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setQuestionAnswerProblemId(int addr, int v) {
        if (featOkTst && casFeat_QuestionAnswerProblemId == null)
      jcas.throwFeatMissing("QuestionAnswerProblemId", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    ll_cas.ll_setIntValue(addr, casFeatCode_QuestionAnswerProblemId, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public QuestionAnswerProblemType_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_instanceText = jcas.getRequiredFeatureDE(casType, "instanceText", "uima.cas.String", featOkTst);
    casFeatCode_instanceText  = (null == casFeat_instanceText) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_instanceText).getCode();

 
    casFeat_questionText = jcas.getRequiredFeatureDE(casType, "questionText", "uima.cas.String", featOkTst);
    casFeatCode_questionText  = (null == casFeat_questionText) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_questionText).getCode();

 
    casFeat_answerText1 = jcas.getRequiredFeatureDE(casType, "answerText1", "uima.cas.String", featOkTst);
    casFeatCode_answerText1  = (null == casFeat_answerText1) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_answerText1).getCode();

 
    casFeat_answerText2 = jcas.getRequiredFeatureDE(casType, "answerText2", "uima.cas.String", featOkTst);
    casFeatCode_answerText2  = (null == casFeat_answerText2) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_answerText2).getCode();

 
    casFeat_QuestionAnswerProblemId = jcas.getRequiredFeatureDE(casType, "QuestionAnswerProblemId", "uima.cas.Integer", featOkTst);
    casFeatCode_QuestionAnswerProblemId  = (null == casFeat_QuestionAnswerProblemId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_QuestionAnswerProblemId).getCode();

  }
}



    