package de.unidue.haring.similarity.experiments.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.unidue.haring.similarity.experiments.types.Instance;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;

public class InstanceHandler
    extends DefaultHandler
{
    // List to hold QuestionAnswerProblem objects
    private List<Instance> iList = null;
    private List<QuestionAnswerProblem> qList = null;
    private QuestionAnswerProblem questionAnswerProblem = null;
    private QuestionAnswerPair questionAnswerPair1 = null;
    private QuestionAnswerPair questionAnswerPair2 = null;

    // Accumulator for getting the instance text
    private StringBuffer accumulator = new StringBuffer(1024);
    // Text relating to the current question
    String instanceText;
    String instanceScenario;
    int instanceId;
    // Current texts
    String questionText;

    // Current question ID, relative to current instance
    int questionId;
    int idCorrectAnswer;
    String questionType;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        // Initialize questionAnswerProblem list, if not yet
        if (qList == null) {
            qList = new ArrayList<QuestionAnswerProblem>();
        }
        if (iList == null) {
            iList = new ArrayList<Instance>();
        }
        if (qName.equalsIgnoreCase("instance")) {
            instanceScenario = attributes.getValue("scenario");
            instanceId = Integer.valueOf(attributes.getValue("id"));
        }
        if (qName.equalsIgnoreCase("text")) {
            accumulator.setLength(0);
            instanceText = "";
        }
        if (qName.equalsIgnoreCase("question")) {
            // extract attributes from document
            questionText = attributes.getValue("text");
            questionId = Integer.valueOf(attributes.getValue("id"));
            questionType = attributes.getValue("type");
            // initialize new QuestionAnswerProblem
            questionAnswerProblem = new QuestionAnswerProblem();
            questionAnswerProblem.setInstanceText(instanceText);
            questionAnswerProblem.setQuestionText(questionText);
            questionAnswerProblem.setQuestionId(questionId);
            questionAnswerProblem.setQuestionType(questionType);
        }
        if (qName.equalsIgnoreCase("answer")) {
            // handle answer 1
            if (Integer.valueOf(attributes.getValue("id")) == 0) {
                String answerText1 = attributes.getValue("text");

                questionAnswerProblem.setAnswerText1(answerText1);
                // sets id of correct answer. If attribute is true => answer1 is correct (id=0)
                idCorrectAnswer = Boolean.valueOf(attributes.getValue("correct")) ? 0 : 1;
                questionAnswerProblem.setIDCorrectAnswer(idCorrectAnswer);

                questionAnswerPair1 = new QuestionAnswerPair(questionText, answerText1,
                        instanceText, questionId, 0, idCorrectAnswer,
                        Boolean.valueOf(attributes.getValue("correct")));
                questionAnswerProblem.setPair1(questionAnswerPair1);
            }
            // handle answer 2
            else if (Integer.valueOf(attributes.getValue("id")) == 1) {
                String answerText2 = attributes.getValue("text");

                questionAnswerProblem.setAnswerText2(answerText2);
                questionAnswerPair2 = new QuestionAnswerPair(questionText, answerText2,
                        instanceText, questionId, 1, idCorrectAnswer,
                        Boolean.valueOf(attributes.getValue("correct")));
                questionAnswerProblem.setPair2(questionAnswerPair2);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equalsIgnoreCase("text")) {
            instanceText = accumulator.toString().trim();
        }
        if (qName.equalsIgnoreCase("question")) {
            qList.add(questionAnswerProblem);
        }
        if (qName.equalsIgnoreCase("instance")) {
            Instance instance = new Instance(instanceText, instanceScenario, instanceId, qList);
            iList.add(instance);
            qList = new ArrayList<QuestionAnswerProblem>();
        }
    }

    @Override
    public void characters(char[] buffer, int start, int length) throws SAXException
    {
        accumulator.append(buffer, start, length);
    }

    public List<Instance> getInstanceList()
    {
        return iList;
    }
}