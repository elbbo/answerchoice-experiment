package de.unidue.haring.similarity.experiments.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.CasCollectionReader_ImplBase;

import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblemFactory;
import de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType;

public class CustomXmlReader
    extends CasCollectionReader_ImplBase
{
    /**
     * Test data input file
     */
    public static final String PARAM_TEST_DATA_INPUT_FILE = "TestDataInputFile";
    @ConfigurationParameter(name = PARAM_TEST_DATA_INPUT_FILE, mandatory = true)
    private File testDataInputFile;
    // The language used for processing
    private static final String LANGUAGE = "en";

    // List storing all QuestionAnswerProblems
    private List<QuestionAnswerProblem> qList;
    private int currentQuestionAnswerProblem = 0;

    private QuestionAnswerProblemFactory questionAnswerProblemFactory;

    // Pattern matching punctuation marks except "'"
    private static String PUNCTUATION_PATTERN = "(?!\')\\p{Punct}";

    public static final String INITIAL_VIEW = "_InitialView";
    public static final String INSTANCE_VIEW = "InstanceView";
    public static final String QUESTION_VIEW = "QuestionView";
    public static final String ANSWER_VIEW_1 = "AnswerView1";
    public static final String ANSWER_VIEW_2 = "AnswerView2";

    private String instanceText;
    private String questionText;
    private String answerText1;
    private String answerText2;

    /**
     * Initializes the reader
     */
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException
    {
        super.initialize(context);

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            QuestionHandler qHandler = new QuestionHandler();
            saxParser.parse(testDataInputFile, qHandler);

            questionAnswerProblemFactory = new QuestionAnswerProblemFactory();
            qList = qHandler.getQuestionAnswerProblemList();
        }
        catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Progress[] getProgress()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasNext() throws IOException, CollectionException
    {
        if (currentQuestionAnswerProblem < qList.size()) {
            return true;
        }
        return false;
    }

    @Override
    public void getNext(CAS aCAS) throws IOException, CollectionException
    {
        try {
            // Gets the current QuestionAnswerProblem
            QuestionAnswerProblem q = qList.get(currentQuestionAnswerProblem);
            // Sets document text
            instanceText = q.getInstanceText();
            questionText = q.getQuestionText();
            answerText1 = q.getAnswerText1();
            answerText2 = q.getAnswerText2();

            // Creates a new QuestionAnswerProblem in the factory
            questionAnswerProblemFactory.addQuestionAnswerProblem(currentQuestionAnswerProblem, q);

            // Create views for pipeline processing
            JCas jcas = aCAS.getJCas();
            JCas instanceView = jcas.createView(INSTANCE_VIEW);
            JCas questionView = jcas.createView(QUESTION_VIEW);
            JCas answerView1 = jcas.createView(ANSWER_VIEW_1);
            JCas answerView2 = jcas.createView(ANSWER_VIEW_2);

            // Adds document text to views
            instanceView.setDocumentText(preprocessInput(instanceText));
            instanceView.setDocumentLanguage(LANGUAGE);
            
            questionView.setDocumentText(preprocessInput(questionText));
            questionView.setDocumentLanguage(LANGUAGE);

            answerView1.setDocumentText(preprocessInput(answerText1));
            answerView1.setDocumentLanguage(LANGUAGE);

            answerView2.setDocumentText(preprocessInput(answerText2));
            answerView2.setDocumentLanguage(LANGUAGE);

            // Creates gold QuestionAnswerProblem
            QuestionAnswerProblemType goldQuestionAnswerProblem = new QuestionAnswerProblemType(
                    jcas);
            goldQuestionAnswerProblem.setInstanceText(instanceText);
            goldQuestionAnswerProblem.setQuestionText(questionText);
            goldQuestionAnswerProblem.setAnswerText1(answerText1);
            goldQuestionAnswerProblem.setAnswerText2(answerText2);
            goldQuestionAnswerProblem.setQuestionAnswerProblemId(currentQuestionAnswerProblem);
            goldQuestionAnswerProblem.addToIndexes();

            currentQuestionAnswerProblem++;
        }
        catch (CASException e) {
            e.printStackTrace();
        }
    }

    /**
     * Preprocessing for input text. Removes punctuation and multiple appearances of blanks from the
     * document text. In addition, the text is set to lowercase letters.
     * 
     * @param input
     *            the document text
     * @return the preprocessed document text.
     */
    private String preprocessInput(String input)
    {
        // Removes punctuation
        input = input.replaceAll(PUNCTUATION_PATTERN, " ");
        // input = input.replaceAll("#", "");
        input.toLowerCase();
        // Removes double occurrences of blanks
        input = input.replaceAll("\\s+", " ");
        return input.trim();
    }

    private class QuestionHandler
        extends DefaultHandler
    {
        // List to hold QuestionAnswerProblem objects
        private List<QuestionAnswerProblem> qList = null;
        private QuestionAnswerProblem questionAnswerProblem = null;
        private QuestionAnswerPair questionAnswerPair1 = null;
        private QuestionAnswerPair questionAnswerPair2 = null;

        // Accumulator for getting the instance text
        private StringBuffer accumulator = new StringBuffer(1024);
        // Text relating to the current question
        String instanceText;
        // Current question text
        String questionText;
        // Current question ID, relative to current instance
        int questionId;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            // Initialize questionAnswerProblem list, if not yet
            if (qList == null) {
                qList = new ArrayList<QuestionAnswerProblem>();
            }
            if (qName.equalsIgnoreCase("text")) {
                accumulator.setLength(0);
                instanceText = "";
            }
            if (qName.equalsIgnoreCase("question")) {
                questionAnswerProblem = new QuestionAnswerProblem();
                questionAnswerProblem.setInstanceText(instanceText);
                questionAnswerProblem.setQuestionText(attributes.getValue("text"));
                questionAnswerProblem.setQuestionId(Integer.valueOf(attributes.getValue("id")));
            }
            if (qName.equalsIgnoreCase("answer")) {
                if (Integer.valueOf(attributes.getValue("id")) == 0) {
                    questionAnswerProblem.setAnswerText1(attributes.getValue("text"));
                    questionAnswerProblem.setIDCorrectAnswer(
                            Boolean.valueOf(attributes.getValue("correct")) ? 0 : 1);
                    questionAnswerPair1 = new QuestionAnswerPair(questionText,
                            attributes.getValue("text"), instanceText, questionId,
                            Boolean.valueOf(attributes.getValue("correct")) ? 0 : 1, 0,
                            Boolean.valueOf(attributes.getValue("correct")));
                    questionAnswerProblem.setPair1(questionAnswerPair1);
                }
                else if (Integer.valueOf(attributes.getValue("id")) == 1) {
                    questionAnswerProblem.setAnswerText2(attributes.getValue("text"));
                    questionAnswerPair2 = new QuestionAnswerPair(questionText,
                            attributes.getValue("text"), instanceText, questionId,
                            Boolean.valueOf(attributes.getValue("correct")) ? 1 : 0, 0,
                            Boolean.valueOf(attributes.getValue("correct")));
                    questionAnswerProblem.setPair2(questionAnswerPair2);
                }
            }
        }

        @Override
        public void characters(char[] buffer, int start, int length) throws SAXException
        {
            accumulator.append(buffer, start, length);
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
        }

        public List<QuestionAnswerProblem> getQuestionAnswerProblemList()
        {
            return qList;
        }
    }
}
