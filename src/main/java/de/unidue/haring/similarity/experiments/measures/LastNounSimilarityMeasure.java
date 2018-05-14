package de.unidue.haring.similarity.experiments.measures;

import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.fit.util.JCasUtil;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.mallet.type.WordEmbedding;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;

public class LastNounSimilarityMeasure
    extends EmbeddingsSimilarityMeasure
{
    private static final String MEASURE_METHOD_NAME = "LastNounSimilarityMeasure";

    public LastNounSimilarityMeasure()
    {
        super();
    }

    @Override
    public QuestionAnswerProblem measureSimilarity(CAS aCAS,
            QuestionAnswerProblem questionAnswerProblem)
    {
        QuestionAnswerPair questionAnswerPair1 = questionAnswerProblem.getPair1();
        QuestionAnswerPair questionAnswerPair2 = questionAnswerProblem.getPair2();

        double cosineSimPair1 = computeTokenSimilarity(
                getLastTokenOfSentence(questionAnswerPair1.getQuestionToken()),
                getLastTokenOfSentence(questionAnswerPair1.getAnswerToken()));
        double cosineSimPair2 = computeTokenSimilarity(
                getLastTokenOfSentence(questionAnswerPair2.getQuestionToken()),
                getLastTokenOfSentence(questionAnswerPair2.getAnswerToken()));

        setSemanticRelatedness(MEASURE_METHOD_NAME, questionAnswerPair1, questionAnswerPair2,
                cosineSimPair1, cosineSimPair2);
        return questionAnswerProblem;
    }

    /**
     * Computes the similarity between to given tokens from its embedding annotations. If a token is
     * null, an array filled with ones will be used instead.
     * 
     * @param token1
     *            the first token
     * @param token2
     *            the second token
     * @return the cosine similarity of the annotated embeddings
     */
    private double computeTokenSimilarity(Token token1, Token token2)
    {
        INDArray annotationToken1;
        INDArray annotationToken2;

        if (token1 == null) {
            annotationToken1 = Nd4j.ones(300);
            // annotationToken1 = Nd4j.zeros(300);
        }
        else {
            annotationToken1 = Nd4j.create(JCasUtil.selectCovered(WordEmbedding.class, token1)
                    .get(0).getWordEmbedding().toArray());
        }
        if (token2 == null) {
            annotationToken2 = Nd4j.ones(300);
            // annotationToken2 = Nd4j.zeros(300);
        }
        else {
            annotationToken2 = Nd4j.create(JCasUtil.selectCovered(WordEmbedding.class, token2)
                    .get(0).getWordEmbedding().toArray());
        }

        return Transforms.cosineSim(annotationToken1, annotationToken2);
    }

    private Token getLastTokenOfSentence(List<Token> tokenList)
    {
        Token lastNoun = null;
        for (Token t : tokenList) {
            if (t.getPos().getPosValue().equals("NN")) {
                lastNoun = t;
            }
        }
        // If no noun was found
        if (lastNoun == null) {
            // Handle case if token list is empty
            if (tokenList.isEmpty()) {
                return null;
            }
            // If no noun was found within the token list return the last token
            return tokenList.get(tokenList.size() - 1);
        }
        return lastNoun;
    }

    @Override
    public String getMeasureMethodName()
    {
        return MEASURE_METHOD_NAME;
    }
}
