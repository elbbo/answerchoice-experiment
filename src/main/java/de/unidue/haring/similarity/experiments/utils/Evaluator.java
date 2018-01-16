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
    // private List<QuestionAnswerProblem> questionAnswerProblems;
    private List<SimilarityMeasure> similarityMeasureMethods;

    private SimilarityMeasureFactory similarityMeasureFactory;

    private static final String LF = System.getProperty("line.separator");

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException
    {
        similarityMeasureFactory = new SimilarityMeasureFactory();
        // questionAnswerProblems = new ArrayList<QuestionAnswerProblem>();
        similarityMeasureMethods = new ArrayList<SimilarityMeasure>();

        // Initializes similarity measure methods which will be used
        similarityMeasureMethods = similarityMeasureFactory.initializeSimilarityMeasureMethods();
    }

    @Override
    public void process(CAS aCAS) throws AnalysisEngineProcessException
    {
        try {
            // Gets current QuestionAnswerProblem instance
            QuestionAnswerProblem questionAnswerProblem = getCurrentQuestionAnswerProblem(aCAS);

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

    private String getEvaluationResults(boolean printProblems)
    {
        Map<Integer, QuestionAnswerProblem> questionAnswerProblems = QuestionAnswerProblemFactory
                .getQuestionAnswerProblems();
        StringBuilder sb = new StringBuilder();

        for (SimilarityMeasure similarityMeasure : similarityMeasureMethods) {
            int totalAnsweredQuestions = 0;
            int correctAnsweredQuestions = 0;
            boolean isCorrect;
            sb.append(LF);
            sb.append("Measure Method: " + similarityMeasure.getMeasureMethodName());
            sb.append(LF);

            for (Entry<Integer, QuestionAnswerProblem> entry : questionAnswerProblems.entrySet()) {
                totalAnsweredQuestions++;
                QuestionAnswerProblem questionAnswerProblem = entry.getValue();
                sb.append(LF);
                sb.append("Problem nr: " + entry.getKey());
                sb.append(LF);

                if (printProblems) {
                    sb.append(LF);
                    sb.append("Question: " + questionAnswerProblem.getQuestionText());
                    sb.append(LF);
                    sb.append("Answer 1: " + questionAnswerProblem.getAnswerText1());
                    sb.append(LF);
                    sb.append("Answer 2: " + questionAnswerProblem.getAnswerText2());
                    sb.append(LF);
                }

                sb.append(LF);
                isCorrect = isCorrectAnswer(similarityMeasure, questionAnswerProblem);
                if (isCorrect)
                    correctAnsweredQuestions++;
                sb.append("Correct answered: " + String.valueOf(isCorrect));
                sb.append(LF);
            }
            sb.append(LF);
            sb.append("Total answered Questions: " + totalAnsweredQuestions);
            sb.append(LF);
            sb.append("Correct answered Questions: " + correctAnsweredQuestions);
            sb.append(LF);
            sb.append("Score: " + String.format("%.2f%%.", Float.valueOf(
                    ((float) correctAnsweredQuestions / (float) totalAnsweredQuestions) * 100)));
            sb.append(LF);
        }

        return sb.toString();
    }

    private boolean isCorrectAnswer(SimilarityMeasure similarityMeasure,
            QuestionAnswerProblem questionAnswerProblem)
    {
        questionAnswerProblem.getIDCorrectAnswer();
        SemanticRelatedness semanticRelatednessPair1 = questionAnswerProblem.getPair1()
                .getRelatedness(similarityMeasure.getMeasureMethodName());
        SemanticRelatedness semanticRelatednessPair2 = questionAnswerProblem.getPair2()
                .getRelatedness(similarityMeasure.getMeasureMethodName());

        if (semanticRelatednessPair1.getSemanticRelatednessValue() >= semanticRelatednessPair2
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
