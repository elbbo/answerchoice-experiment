package de.unidue.haring.similarity.experiments.measures;

import org.apache.uima.cas.CAS;

import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;

public class RandomSimilarityMeasure
    extends SimilarityMeasure
{
    private static final String MEASURE_METHOD_NAME = "RandomSimilarityMeasure";

    @Override
    public QuestionAnswerProblem measureSimilarity(CAS aCAS,
            QuestionAnswerProblem questionAnswerProblem)
    {
        QuestionAnswerPair questionAnswerPair1 = questionAnswerProblem.getPair1();
        QuestionAnswerPair questionAnswerPair2 = questionAnswerProblem.getPair2();

        if (Math.round(Math.random()) == 1) {
            setSemanticRelatedness(MEASURE_METHOD_NAME, questionAnswerPair1, questionAnswerPair2,
                    1.0, 0.0);
        }
        else {
            setSemanticRelatedness(MEASURE_METHOD_NAME, questionAnswerPair1, questionAnswerPair2,
                    0.0, 1.0);
        }

        return questionAnswerProblem;
    }

    @Override
    public String getMeasureMethodName()
    {
        return MEASURE_METHOD_NAME;
    }
}
