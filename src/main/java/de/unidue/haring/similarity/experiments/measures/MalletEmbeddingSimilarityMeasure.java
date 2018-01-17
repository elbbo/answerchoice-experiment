package de.unidue.haring.similarity.experiments.measures;

import org.apache.uima.cas.CAS;

import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;

public class MalletEmbeddingSimilarityMeasure
    extends SimilarityMeasure
{
    private static final String MEASURE_METHOD_NAME = "MalletEmbeddingSimilarityMeasure";

    @Override
    public QuestionAnswerProblem measureSimilarity(CAS aCAS,
            QuestionAnswerProblem questionAnswerProblem)
    {
        return questionAnswerProblem;
    }

    @Override
    public String getMeasureMethodName()
    {
        return MEASURE_METHOD_NAME;
    }
}
