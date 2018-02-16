package de.unidue.haring.similarity.experiments.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.CasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.haring.similarity.experiments.measures.SimilarityMeasure;
import de.unidue.haring.similarity.experiments.measures.SimilarityMeasureFactory;
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
        String results = getEvaluationResults(true);
        System.out.println(results);
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
}

// CAS view;
// JCas JCas;
// Collection<Sentence> sentences;
//
// Iterator<CAS> it = aCas.getViewIterator();
// while (it.hasNext()) {
// view = it.next();
// JCas = view.getJCas();
//
// sentences = JCasUtil.select(JCas, Sentence.class);
// for (Sentence s : sentences) {
// List<Token> tokenOfSentence = JCasUtil.selectCovered(JCas, Token.class,
// s.getBegin(), s.getEnd());
// for (Token token : tokenOfSentence) {
// token.getPosValue();
// token.getLemmaValue();
// }
// }
// }
// CAS questionView = aCAS.getView(CustomXmlReader.QUESTION_VIEW);
// JCas qJCas = questionView.getJCas();
// CAS answerView1 = aCAS.getView(CustomXmlReader.ANSWER_VIEW_1);
// JCas a1JCas = answerView1.getJCas();
// CAS answerView2 = aCAS.getView(CustomXmlReader.ANSWER_VIEW_2);
