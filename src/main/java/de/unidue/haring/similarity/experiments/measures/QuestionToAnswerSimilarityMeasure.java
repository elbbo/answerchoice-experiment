package de.unidue.haring.similarity.experiments.measures;

import static org.nd4j.linalg.ops.transforms.Transforms.*;
import java.util.Arrays;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.distances.CosineSimilarity;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;
import de.unidue.haring.similarity.experiments.types.SemanticRelatedness;

public class QuestionToAnswerSimilarityMeasure
    extends EmbeddingsSimilarityMeasure
{
    private static final String MEASURE_METHOD_NAME = "QuestionToAnswerSimilarityMeasure";

    @Override
    public QuestionAnswerProblem measureSimilarity(CAS aCAS,
            QuestionAnswerProblem questionAnswerProblem)
    {
        QuestionAnswerPair questionAnswerPair1 = questionAnswerProblem.getPair1();
        QuestionAnswerPair questionAnswerPair2 = questionAnswerProblem.getPair2();

        double cosineSimPair1 = computeCosineSimilarity(
                questionAnswerPair1.getQuestionLemmasEmbeddingAnnotationsList(),
                questionAnswerPair1.getAnswerLemmasEmbeddingAnnotationsList());
        double cosineSimPair2 = computeCosineSimilarity(
                questionAnswerPair2.getQuestionLemmasEmbeddingAnnotationsList(),
                questionAnswerPair2.getAnswerLemmasEmbeddingAnnotationsList());
        System.out.println("Cosine QA1: " + cosineSimPair1);
        System.out.println("Cosine QA2: " + cosineSimPair2);

        SemanticRelatedness semanticRelatednessPair1 = new SemanticRelatedness(MEASURE_METHOD_NAME);
        SemanticRelatedness semanticRelatednessPair2 = new SemanticRelatedness(MEASURE_METHOD_NAME);

        semanticRelatednessPair1.setSemanticRelatednessValue(cosineSimPair1);
        semanticRelatednessPair2.setSemanticRelatednessValue(cosineSimPair2);
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
