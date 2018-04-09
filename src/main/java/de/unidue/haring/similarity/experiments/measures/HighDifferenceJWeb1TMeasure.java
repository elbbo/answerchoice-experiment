package de.unidue.haring.similarity.experiments.measures;

import java.io.IOException;

import org.apache.uima.cas.CAS;

import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;

public class HighDifferenceJWeb1TMeasure
    extends JWeb1TMeasure
{
    private static final String MEASURE_METHOD_NAME = "HighDifferenceJWeb1TMeasure";
    private static final int THRESHOLD = 30;
    
    @Override
    public QuestionAnswerProblem measureSimilarity(CAS aCAS,
            QuestionAnswerProblem questionAnswerProblem)
    {
        QuestionAnswerPair questionAnswerPair1 = questionAnswerProblem.getPair1();
        QuestionAnswerPair questionAnswerPair2 = questionAnswerProblem.getPair2();

        try {
            if (web1t == null) {
                initJWeb1T();
            }
            double a1Freq = sumLemmataFrequency(questionAnswerPair1.getAnswerLemmas());
            double a2Freq = sumLemmataFrequency(questionAnswerPair2.getAnswerLemmas());

            // If the difference in frequencies exceeds a certain value, the more likely sentence is
            // selected as the answer prediction. Otherwise compute the cosine similarity between
            // averaged instance embeddings and averaged answer embeddings.
            if (Math.abs(a1Freq - a2Freq) > THRESHOLD) {
                setSemanticRelatedness(MEASURE_METHOD_NAME, questionAnswerPair1,
                        questionAnswerPair2, a1Freq, a2Freq);
            }
            else {
                double cosineSimPair1 = computeCosineSimilarity(
                        questionAnswerPair1.getInstanceLemmasEmbeddingAnnotationsList(),
                        questionAnswerPair1.getAnswerLemmasEmbeddingAnnotationsList());
                double cosineSimPair2 = computeCosineSimilarity(
                        questionAnswerPair2.getInstanceLemmasEmbeddingAnnotationsList(),
                        questionAnswerPair2.getAnswerLemmasEmbeddingAnnotationsList());

                setSemanticRelatedness(MEASURE_METHOD_NAME, questionAnswerPair1,
                        questionAnswerPair2, cosineSimPair1, cosineSimPair2);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return questionAnswerProblem;
    }

    @Override
    public String getMeasureMethodName()
    {
        return MEASURE_METHOD_NAME;
    }
}
