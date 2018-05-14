package de.unidue.haring.similarity.experiments.measures;

import java.util.List;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;

public class EmbeddingsSimilarityMeasure
    extends SimilarityMeasure
{
    public EmbeddingsSimilarityMeasure()
    {
        super();
    }

    /**
     * Computes the cosine similarity of given vector lists.
     * 
     * @param pair the QuestionAnswerPair
     * @return the cosine similarity value
     */
    protected double computeCosineSimilarity(List<float[]> listVector1, List<float[]> listVector2)
    {
        if (listVector1.size() < 1 || listVector2.size() < 1) {
            return 0.f;
        }
        INDArray averageVector1 = computeVectorAverage(listVector1);
        INDArray averageVector2 = computeVectorAverage(listVector2);

        double similarity = Transforms.cosineSim(averageVector1, averageVector2);

        return similarity;
    }

    /**
     * Computes the average of a given vector list. Precondition: the list contains more than one
     * element.
     * 
     * @param embeddingsList list containing word embeddings of a given sentence
     * @return the average vector
     */
    private INDArray computeVectorAverage(List<float[]> embeddingsList)
    {
        INDArray sum = Nd4j.zeros(embeddingsList.get(0).length);

        for (float[] vec : embeddingsList) {
            INDArray nd = Nd4j.create(vec);
            sum = sum.add(nd);
        }
        sum = sum.div(embeddingsList.size());
        return sum;
    }
}
