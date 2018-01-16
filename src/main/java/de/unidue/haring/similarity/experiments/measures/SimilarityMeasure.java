package de.unidue.haring.similarity.experiments.measures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

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
    public void prepareQuestionAnswerPairs(QuestionAnswerProblem questionAnswerProblem, JCas qJCas,
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
