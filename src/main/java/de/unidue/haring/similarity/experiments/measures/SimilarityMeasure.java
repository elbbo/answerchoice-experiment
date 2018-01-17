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
        System.out.println("Default similarity measure");
        return null;
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
            CAS questionView = aCAS.getView(CustomXmlReader.QUESTION_VIEW);
            JCas qJCas = questionView.getJCas();
            CAS answerView1 = aCAS.getView(CustomXmlReader.ANSWER_VIEW_1);
            JCas a1JCas = answerView1.getJCas();
            CAS answerView2 = aCAS.getView(CustomXmlReader.ANSWER_VIEW_2);
            JCas a2JCas = answerView2.getJCas();

            prepareQuestionAnswerPairs(questionAnswerProblem, qJCas, a1JCas, a2JCas);
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
    private void prepareQuestionAnswerPairs(QuestionAnswerProblem questionAnswerProblem, JCas qJCas,
            JCas a1JCas, JCas a2JCas)
    {
        setQuestionAnswerPairLemmas(questionAnswerProblem.getPair1(), qJCas, a1JCas);
        setQuestionAnswerPairLemmas(questionAnswerProblem.getPair2(), qJCas, a2JCas);
    }

    private void setQuestionAnswerPairLemmas(QuestionAnswerPair questionAnswerPair,
            JCas questionJcas, JCas answerJcas)
    {
        questionAnswerPair.setQuestionLemmas(getLemmaList(questionJcas));
        questionAnswerPair.setAnswerLemmas(getLemmaList(answerJcas));
        questionAnswerPair.setQuestionLemmasEmbeddingAnnotationsList(
                getMalletEmbeddingsAnnotations(questionJcas));
        questionAnswerPair.setAnswerLemmasEmbeddingAnnotationsList(
                getMalletEmbeddingsAnnotations(answerJcas));
    }

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
