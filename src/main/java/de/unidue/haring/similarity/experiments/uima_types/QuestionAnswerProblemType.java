

/* First created by JCasGen Mon Jan 15 21:21:13 CET 2018 */
package de.unidue.haring.similarity.experiments.uima_types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Jan 15 21:21:19 CET 2018
 * XML source: D:/workspace/de.unidue.haring.similarity.answerchoice-experiment/src/main/resources/desc/type/QuestionAnswerProblem.xml
 * @generated */
public class QuestionAnswerProblemType extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(QuestionAnswerProblemType.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected QuestionAnswerProblemType() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public QuestionAnswerProblemType(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public QuestionAnswerProblemType(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public QuestionAnswerProblemType(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: instanceText

  /** getter for instanceText - gets 
   * @generated
   * @return value of the feature 
   */
  public String getInstanceText() {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_instanceText == null)
      jcasType.jcas.throwFeatMissing("instanceText", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_instanceText);}
    
  /** setter for instanceText - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setInstanceText(String v) {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_instanceText == null)
      jcasType.jcas.throwFeatMissing("instanceText", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_instanceText, v);}    
   
    
  //*--------------*
  //* Feature: questionText

  /** getter for questionText - gets 
   * @generated
   * @return value of the feature 
   */
  public String getQuestionText() {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_questionText == null)
      jcasType.jcas.throwFeatMissing("questionText", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_questionText);}
    
  /** setter for questionText - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setQuestionText(String v) {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_questionText == null)
      jcasType.jcas.throwFeatMissing("questionText", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_questionText, v);}    
   
    
  //*--------------*
  //* Feature: answerText1

  /** getter for answerText1 - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAnswerText1() {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_answerText1 == null)
      jcasType.jcas.throwFeatMissing("answerText1", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_answerText1);}
    
  /** setter for answerText1 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAnswerText1(String v) {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_answerText1 == null)
      jcasType.jcas.throwFeatMissing("answerText1", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_answerText1, v);}    
   
    
  //*--------------*
  //* Feature: answerText2

  /** getter for answerText2 - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAnswerText2() {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_answerText2 == null)
      jcasType.jcas.throwFeatMissing("answerText2", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_answerText2);}
    
  /** setter for answerText2 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAnswerText2(String v) {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_answerText2 == null)
      jcasType.jcas.throwFeatMissing("answerText2", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_answerText2, v);}    
   
    
  //*--------------*
  //* Feature: QuestionAnswerProblemId

  /** getter for QuestionAnswerProblemId - gets 
   * @generated
   * @return value of the feature 
   */
  public int getQuestionAnswerProblemId() {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_QuestionAnswerProblemId == null)
      jcasType.jcas.throwFeatMissing("QuestionAnswerProblemId", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    return jcasType.ll_cas.ll_getIntValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_QuestionAnswerProblemId);}
    
  /** setter for QuestionAnswerProblemId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setQuestionAnswerProblemId(int v) {
    if (QuestionAnswerProblemType_Type.featOkTst && ((QuestionAnswerProblemType_Type)jcasType).casFeat_QuestionAnswerProblemId == null)
      jcasType.jcas.throwFeatMissing("QuestionAnswerProblemId", "de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType");
    jcasType.ll_cas.ll_setIntValue(addr, ((QuestionAnswerProblemType_Type)jcasType).casFeatCode_QuestionAnswerProblemId, v);}    
  }

    