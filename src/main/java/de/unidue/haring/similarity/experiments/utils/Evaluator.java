package de.unidue.haring.similarity.experiments.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.CasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.haring.similarity.experiments.measures.SimilarityMeasure;
import de.unidue.haring.similarity.experiments.measures.SimilarityMeasureFactory;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblemFactory;
import de.unidue.haring.similarity.experiments.types.SemanticRelatedness;
import de.unidue.haring.similarity.experiments.uima_types.QuestionAnswerProblemType;

public class Evaluator
    extends CasAnnotator_ImplBase
{
    private List<SimilarityMeasure> similarityMeasureMethods;

    private SimilarityMeasureFactory similarityMeasureFactory;
    private SimilarityMeasure defaultSimilarityMeasure;

    private static final String LF = System.getProperty("line.separator");

    private static final String RANDOM = "RandomSimilarityMeasure";
    private static final String INSTANCE_TO_ANSWER = "InstanceToAnswerSimilarityMeasure";
    private static final String QUESTION_TO_ANSWER = "QuestionToAnswerSimilarityMeasure";
    private static final String LAST_NOUN = "LastNounSimilarityMeasure";

    private static final boolean LEMMATA_TO_FILE = false;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException
    {
        similarityMeasureFactory = new SimilarityMeasureFactory();
        similarityMeasureMethods = new ArrayList<SimilarityMeasure>();
        defaultSimilarityMeasure = new SimilarityMeasure();

        // Initializes similarity measure methods which will be used
        similarityMeasureMethods = similarityMeasureFactory.initializeSimilarityMeasureMethods(
                RANDOM, INSTANCE_TO_ANSWER, QUESTION_TO_ANSWER, LAST_NOUN);
    }

    @Override
    public void process(CAS aCAS) throws AnalysisEngineProcessException
    {
        try {
            // Gets current QuestionAnswerProblem instance
            QuestionAnswerProblem questionAnswerProblem = getCurrentQuestionAnswerProblem(aCAS);
            defaultSimilarityMeasure.prepareQuestionAnswerPairs(aCAS, questionAnswerProblem);

            for (SimilarityMeasure similarityMeasure : similarityMeasureMethods) {
                similarityMeasure.measureSimilarity(aCAS, questionAnswerProblem);
            }
        }
        catch (CASException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException
    {
        super.collectionProcessComplete();
        String results = getEvaluationResults(false);
        System.out.println(results);
        
        if (LEMMATA_TO_FILE) {
            GeneralPipelineUtils.writeUsedWordsToFile();
        }
    }

    private String getEvaluationResults(boolean printDetailedProblems)
    {
        Map<Integer, QuestionAnswerProblem> questionAnswerProblems = QuestionAnswerProblemFactory
                .getQuestionAnswerProblems();
        StringBuilder sb = new StringBuilder();

        for (SimilarityMeasure similarityMeasure : similarityMeasureMethods) {
            int totalAnsweredQuestions = 0;
            int correctAnsweredQuestions = 0;
            boolean isCorrect;
            String similarityMeasureMethodName = similarityMeasure.getMeasureMethodName();

            sb.append(LF);
            sb.append("Measure Method: " + similarityMeasureMethodName);
            sb.append(LF);

            for (Entry<Integer, QuestionAnswerProblem> entry : questionAnswerProblems.entrySet()) {
                totalAnsweredQuestions++;
                QuestionAnswerProblem questionAnswerProblem = entry.getValue();
                // Checks if the answer prediction is correct
                isCorrect = isCorrectAnswer(similarityMeasure, questionAnswerProblem);
                if (isCorrect)
                    correctAnsweredQuestions++;

                // debugPrint(questionAnswerProblem);

                if (printDetailedProblems) {
                    if (questionAnswerProblem.getQuestionId() == 0) {
                        sb.append(LF);
                        sb.append("Instance Text: " + questionAnswerProblem.getInstanceText());
                        sb.append(LF);
                    }
                    sb.append(LF);
                    sb.append("Problem nr: " + entry.getKey() + " / Nr correct answer : "
                            + Integer.valueOf(questionAnswerProblem.getIDCorrectAnswer() + 1));
                    sb.append(LF);
                    sb.append("Question text: " + questionAnswerProblem.getQuestionText());
                    sb.append(LF);
                    sb.append("Answer text 1: " + questionAnswerProblem.getAnswerText1());
                    sb.append(LF);
                    sb.append("Answer text 2: " + questionAnswerProblem.getAnswerText2());
                    sb.append(LF);
                    sb.append("Prediction value for answer 1: " + String.format("%.4f.",
                            Double.valueOf(questionAnswerProblem.getPair1()
                                    .getRelatedness(similarityMeasureMethodName)
                                    .getSemanticRelatednessValue())));
                    sb.append(LF);
                    sb.append("Prediction value for answer 2: " + String.format("%.4f.",
                            Double.valueOf(questionAnswerProblem.getPair2()
                                    .getRelatedness(similarityMeasureMethodName)
                                    .getSemanticRelatednessValue())));
                    sb.append(LF);
                    sb.append("Accurate Prediction: " + isCorrect);
                    sb.append(LF);
                }
            }
            sb.append(LF);
            sb.append("Total answered Questions: " + totalAnsweredQuestions);
            sb.append(LF);
            sb.append("Correct answered Questions: " + correctAnsweredQuestions);
            sb.append(LF);
            sb.append("Score / Accuracy: " + String.format("%.2f%%.", Float.valueOf(
                    ((float) correctAnsweredQuestions / (float) totalAnsweredQuestions) * 100)));
            sb.append(LF);
        }

        return sb.toString();
    }

    private boolean isCorrectAnswer(SimilarityMeasure similarityMeasure,
            QuestionAnswerProblem questionAnswerProblem)
    {
        SemanticRelatedness semanticRelatednessPair1 = questionAnswerProblem.getPair1()
                .getRelatedness(similarityMeasure.getMeasureMethodName());
        SemanticRelatedness semanticRelatednessPair2 = questionAnswerProblem.getPair2()
                .getRelatedness(similarityMeasure.getMeasureMethodName());

        if (semanticRelatednessPair1.getSemanticRelatednessValue() > semanticRelatednessPair2
                .getSemanticRelatednessValue()
                && questionAnswerProblem.getPair1().getAnswer().isCorrect()) {
            return true;
        }
        else if (semanticRelatednessPair1.getSemanticRelatednessValue() < semanticRelatednessPair2
                .getSemanticRelatednessValue()
                && questionAnswerProblem.getPair2().getAnswer().isCorrect()) {
            return true;
        }
        return false;
    }

    /**
     * Gets the current QuestionAnswerProblem identified by Id
     * 
     * @param aCAS
     *            the current aCAS
     * @return the current QuestionAnswerProblem
     * @throws CASException
     */
    private QuestionAnswerProblem getCurrentQuestionAnswerProblem(CAS aCAS) throws CASException
    {
        QuestionAnswerProblemType goldQuestionAnswerProblem = JCasUtil.selectSingle(aCAS.getJCas(),
                QuestionAnswerProblemType.class);
        return QuestionAnswerProblemFactory.getQuestionAnswerProblemById(
                goldQuestionAnswerProblem.getQuestionAnswerProblemId());
    }

    private void debugPrint(QuestionAnswerProblem questionAnswerProblem)
    {
        QuestionAnswerPair pair1 = questionAnswerProblem.getPair1();
        QuestionAnswerPair pair2 = questionAnswerProblem.getPair2();
        System.out.println(
                "QuestionAnswerProblem questionText: " + questionAnswerProblem.getQuestionText());
        System.out.println("QuestionAnswerProblem Pair1 questionText: " + pair1.getQuestionText());
        System.out.println("QuestionAnswerProblem Pair1 questionText: " + pair2.getQuestionText());
        System.out.println(
                "QuestionAnswerProblem answerText1: " + questionAnswerProblem.getAnswerText1());
        System.out.println("QuestionAnswerPair1 answerText: " + pair1.getAnswerText());
        System.out.println(
                "QuestionAnswerProblem answerText2: " + questionAnswerProblem.getAnswerText2());
        System.out.println("QuestionAnswerPair2 answerText: " + pair2.getAnswerText());
        System.out.println("QuestionAnswerProblem IDCorrectAnswer: "
                + questionAnswerProblem.getIDCorrectAnswer());
        System.out.println("QuestionAnswerPair1 id: " + pair1.getAnswer().getId()
                + " answer is correct: " + pair1.getAnswer().isCorrect());
        System.out.println("QuestionAnswerPair1 id: " + pair2.getAnswer().getId()
                + " answer is correct: " + pair2.getAnswer().isCorrect());
    }
}
