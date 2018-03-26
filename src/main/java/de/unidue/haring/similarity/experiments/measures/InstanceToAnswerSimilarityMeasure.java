package de.unidue.haring.similarity.experiments.measures;

import org.apache.uima.cas.CAS;

import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;
import de.unidue.haring.similarity.experiments.utils.GeneralPipelineUtils;

public class InstanceToAnswerSimilarityMeasure
    extends EmbeddingsSimilarityMeasure
{
    private static final String MEASURE_METHOD_NAME = "InstanceToAnswerSimilarityMeasure";

    @Override
    public QuestionAnswerProblem measureSimilarity(CAS aCAS,
            QuestionAnswerProblem questionAnswerProblem)
    {
        QuestionAnswerPair questionAnswerPair1 = questionAnswerProblem.getPair1();
        QuestionAnswerPair questionAnswerPair2 = questionAnswerProblem.getPair2();

        double cosineSimPair1 = computeCosineSimilarity(
                questionAnswerPair1.getInstanceLemmasEmbeddingAnnotationsList(),
                questionAnswerPair1.getAnswerLemmasEmbeddingAnnotationsList());
        double cosineSimPair2 = computeCosineSimilarity(
                questionAnswerPair2.getInstanceLemmasEmbeddingAnnotationsList(),
                questionAnswerPair2.getAnswerLemmasEmbeddingAnnotationsList());

        setSemanticRelatedness(MEASURE_METHOD_NAME, questionAnswerPair1, questionAnswerPair2,
                cosineSimPair1, cosineSimPair2);
        return questionAnswerProblem;
    }

    @Override
    public String getMeasureMethodName()
    {
        return MEASURE_METHOD_NAME;
    }
}
