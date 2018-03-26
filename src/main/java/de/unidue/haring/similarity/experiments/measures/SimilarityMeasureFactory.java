package de.unidue.haring.similarity.experiments.measures;

import java.util.ArrayList;
import java.util.List;

public class SimilarityMeasureFactory
{
    private ArrayList<SimilarityMeasure> similarityMeasureMethods;

    public ArrayList<SimilarityMeasure> initializeSimilarityMeasureMethods(
            String... similarityMeasures)
    {
        similarityMeasureMethods = new ArrayList<SimilarityMeasure>();

        for (String similarityMeasure : similarityMeasures) {
            switch (similarityMeasure) {
            case "RandomSimilarityMeasure":
                similarityMeasureMethods.add(new RandomSimilarityMeasure());
                break;
            case "InstanceToAnswerSimilarityMeasure":
                similarityMeasureMethods.add(new InstanceToAnswerSimilarityMeasure());
                break;
            case "QuestionToAnswerSimilarityMeasure":
                similarityMeasureMethods.add(new QuestionToAnswerSimilarityMeasure());
                break;
            case "LastNounSimilarityMeasure":
                similarityMeasureMethods.add(new LastNounSimilarityMeasure());
                break;
            default:
                System.out.println("No similarity measure method found");
            }
        }

        return similarityMeasureMethods;
    }
}
