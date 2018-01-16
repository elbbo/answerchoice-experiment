package de.unidue.haring.similarity.experiments.measures;

import org.apache.uima.cas.CAS;

import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;

public abstract class AbstractSimilarityMeasure
{
    public abstract QuestionAnswerProblem measureSimilarity(CAS aCas,
            QuestionAnswerProblem questionAnswerProblem);

    public abstract String getMeasureMethodName();
}
