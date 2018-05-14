package de.unidue.haring.similarity.experiments.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class ResultsTablePrinter
{
    private final String resultsFilePath = "src/main/resources/results/";
    private final String resultsFileName = "evaluation_results_table.txt";

    private final String missingAnnotationsFilePath = "src/main/resources/results/random/";
    private final String missingAnnotationsFileName = "missing_embedding_annotations.txt";

    private StringBuilder resultsSb;
    private StringBuilder missingAnnotationsSb;

    private static final String LF = System.getProperty("line.separator");

    public ResultsTablePrinter()
    {
        resultsSb = new StringBuilder();
        missingAnnotationsSb = new StringBuilder();
    }

    public void printMissingAnnotationsHead()
    {
        missingAnnotationsSb.append(String.format("%32s%32s%50s%16s%16s%20s", "Dataset", "Embeddings",
                "Method", "Total tokens", "Total missing", "Percentage Missing"));
        missingAnnotationsSb.append(LF);
    }
    
    public void printResultsHead()
    {
        resultsSb.append(String.format("%32s%32s%40s%16s%32s%20s%32s%32s", "Dataset", "Embeddings",
                "Method", "Accuracy", "Accuracy on \"commonsense\"", "Accuracy on \"text\"",
                "Total questions", "total commonsense questions"));
        resultsSb.append(LF);
    }

    public void printResultsColumn(String usedData, String usedEmbeddings, String usedMethod,
            int totalAnsweredQuestions, int correctAnsweredQuestions, int totalCommonsenseQuestions,
            int correctCommonsenseQuestions, int totalTextQuestions, int correctTextQuestions)
    {
        float accuracy = Float
                .valueOf(((float) correctAnsweredQuestions / (float) totalAnsweredQuestions) * 100);
        float csAccuracy = Float.valueOf(
                ((float) correctCommonsenseQuestions / (float) totalCommonsenseQuestions) * 100);
        float textAccuracy = Float
                .valueOf(((float) correctTextQuestions / (float) totalTextQuestions) * 100);

        resultsSb.append(String.format("%32s%32s%40s%11s%.2f%27s%.2f%15s%.2f%32d%32d", usedData,
                usedEmbeddings, usedMethod, "", accuracy, "", csAccuracy, "", textAccuracy,
                totalAnsweredQuestions, totalCommonsenseQuestions));
        resultsSb.append(LF);
    }

    public void printMissingAnnotationsColumn(String usedData, String usedEmbeddings,
            String measureMethodName, List<String> missingEmbeddingsAnnotation,
            List<String> totalTokens)
    {
        float percentageMissingAnnotations = Float.valueOf(
                ((float) missingEmbeddingsAnnotation.size() / (float) totalTokens.size()) * 100);

        missingAnnotationsSb.append(String.format("%32s%32s%50s%16d%16d%16s%.2f%8s", usedData,
                usedEmbeddings, measureMethodName, totalTokens.size(),
                missingEmbeddingsAnnotation.size(), "", percentageMissingAnnotations, ""));
        missingAnnotationsSb.append(missingEmbeddingsAnnotation.toString());
        missingAnnotationsSb.append(LF);
    }

    public void printMissingAnnotationsTableToFile()
    {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(missingAnnotationsFilePath + missingAnnotationsFileName, true), "utf-8"))) {
            writer.write(missingAnnotationsSb.toString() + "\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }   
    
    public void printResultsTableToFile()
    {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(resultsFilePath + resultsFileName, true), "utf-8"))) {
            writer.write(resultsSb.toString() + "\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
