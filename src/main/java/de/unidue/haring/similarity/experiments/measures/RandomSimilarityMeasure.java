package de.unidue.haring.similarity.experiments.measures;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;

import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;
import de.unidue.haring.similarity.experiments.types.SemanticRelatedness;
import de.unidue.haring.similarity.experiments.utils.CustomXmlReader;

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

        SemanticRelatedness semanticRelatednessPair1 = new SemanticRelatedness(
                MEASURE_METHOD_NAME);
        SemanticRelatedness semanticRelatednessPair2 = new SemanticRelatedness(
                MEASURE_METHOD_NAME);
        System.out.println("rnd");
        if (Math.round(Math.random()) == 1) {
            semanticRelatednessPair1.setSemanticRelatednessValue(1.0);
            semanticRelatednessPair2.setSemanticRelatednessValue(0.0);
        }
        else {
            semanticRelatednessPair1.setSemanticRelatednessValue(0.0);
            semanticRelatednessPair2.setSemanticRelatednessValue(1.0);
        }
        questionAnswerPair1.setRelatedness(semanticRelatednessPair1);
        questionAnswerPair2.setRelatedness(semanticRelatednessPair2);
        return questionAnswerProblem;
    }

    @Override
    public String getMeasureMethodName()
    {
        return MEASURE_METHOD_NAME;
    }
}
