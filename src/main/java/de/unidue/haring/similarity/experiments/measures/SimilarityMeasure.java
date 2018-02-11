package de.unidue.haring.similarity.experiments.measures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;
import de.unidue.haring.similarity.experiments.utils.CustomXmlReader;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.mallet.type.WordEmbedding;

public class SimilarityMeasure
    extends AbstractSimilarityMeasure
{
    private static final String MEASURE_METHOD_NAME = "DefaultSimilarityMeasure";

    @Override
    public QuestionAnswerProblem measureSimilarity(CAS aCas,
            QuestionAnswerProblem questionAnswerProblem)
    {
        System.out.println("Default similarity measure.");
        return questionAnswerProblem;
    }

    /**
     * Prepares the QuestionAnswerPairs for further processing.
     * 
     * @param aCAS
     *            the current CAS
     * @param questionAnswerProblem
     *            the current QuestionAnswerProblem
     */
    public void prepareQuestionAnswerPairs(CAS aCAS, QuestionAnswerProblem questionAnswerProblem)
    {
        try {
            CAS instanceView = aCAS.getView(CustomXmlReader.INSTANCE_VIEW);
            JCas iJCas = instanceView.getJCas();
            CAS questionView = aCAS.getView(CustomXmlReader.QUESTION_VIEW);
            JCas qJCas = questionView.getJCas();
            CAS answerView1 = aCAS.getView(CustomXmlReader.ANSWER_VIEW_1);
            JCas a1JCas = answerView1.getJCas();
            CAS answerView2 = aCAS.getView(CustomXmlReader.ANSWER_VIEW_2);
            JCas a2JCas = answerView2.getJCas();

            prepareQuestionAnswerPairs(questionAnswerProblem, iJCas, qJCas, a1JCas, a2JCas);
        }
        catch (CASException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepares the QuestionAnswerPairs for further processing. Sets Lemmas for each
     * QuestionAnswerPair.
     * 
     * @param QuestionAnswerProblem
     *            the QuestionAnswerProblem
     * @param qJCas
     *            the JCas representing the QuestionView
     * @param a1JCas
     *            the JCas representing the AnswerView1
     * @param a2JCas
     *            the JCas representing the AnswerView2
     */
    private void prepareQuestionAnswerPairs(QuestionAnswerProblem questionAnswerProblem, JCas iCas,
            JCas qJCas, JCas a1JCas, JCas a2JCas)
    {
        setQuestionAnswerPairAnnotations(questionAnswerProblem.getPair1(), iCas, qJCas, a1JCas);
        setQuestionAnswerPairAnnotations(questionAnswerProblem.getPair2(), iCas, qJCas, a2JCas);
    }

    /**
     * Sets the annotations for a QuestionAnswerPair. First, the corresponding lemmas are annotated.
     * After that, the mallet embedding annotations are set.
     * 
     * @param questionAnswerPair
     *            the question
     * @param questionJcas
     * @param answerJcas
     */
    private void setQuestionAnswerPairAnnotations(QuestionAnswerPair questionAnswerPair,
            JCas instanceJCas, JCas questionJcas, JCas answerJcas)
    {
        questionAnswerPair.setInstanceLemmas(getLemmaList(instanceJCas));
        questionAnswerPair.setQuestionLemmas(getLemmaList(questionJcas));
        questionAnswerPair.setAnswerLemmas(getLemmaList(answerJcas));
        questionAnswerPair.setInstanceLemmasEmbeddingAnnotationsList(
                getMalletEmbeddingsAnnotations(instanceJCas));
        questionAnswerPair.setQuestionLemmasEmbeddingAnnotationsList(
                getMalletEmbeddingsAnnotations(questionJcas));
        questionAnswerPair.setAnswerLemmasEmbeddingAnnotationsList(
                getMalletEmbeddingsAnnotations(answerJcas));
    }

    /**
     * Gets the mallet embedding annotations from jcas.
     * 
     * @param jCas
     *            the jcas
     * @return List containing all embedding annotations
     */
    private List<float[]> getMalletEmbeddingsAnnotations(JCas jCas)
    {
        List<float[]> embeddingsAnnotationsList = new ArrayList<float[]>();
        Collection<Sentence> sentences = JCasUtil.select(jCas, Sentence.class);
        for (Sentence s : sentences) {
            List<Token> tokenOfSentence = JCasUtil.selectCovered(jCas, Token.class, s.getBegin(),
                    s.getEnd());
            for (Token token : tokenOfSentence) {
                embeddingsAnnotationsList.add(JCasUtil.selectCovered(WordEmbedding.class, token)
                        .get(0).getWordEmbedding().toArray());
            }
        }
        return embeddingsAnnotationsList;
    }

    /**
     * Gets all lemmas from jcas.
     * 
     * @param jCas
     *            the jcas
     * @return List containing all lemmas
     */
    private List<Lemma> getLemmaList(JCas jCas)
    {
        List<Lemma> lemmaList = new ArrayList<Lemma>();
        Collection<Sentence> sentences = JCasUtil.select(jCas, Sentence.class);

        for (Sentence s : sentences) {
            List<Token> tokenOfSentence = JCasUtil.selectCovered(jCas, Token.class, s.getBegin(),
                    s.getEnd());
            for (Token token : tokenOfSentence) {
                lemmaList.add(token.getLemma());
            }
        }
        return lemmaList;
    }

    @Override
    public String getMeasureMethodName()
    {
        return MEASURE_METHOD_NAME;
    }
}
