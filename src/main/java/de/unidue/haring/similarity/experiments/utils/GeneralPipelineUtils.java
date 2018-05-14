package de.unidue.haring.similarity.experiments.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * Utility class that provides various basic functionalities, such as loading and writing used word
 * set.
 */
public class GeneralPipelineUtils
{
    // Set storing all words which are proceeded in pipeline
    private static Set<String> usedWords = new HashSet<String>();
    public static final String USED_WORDS_FILE_PATH = "src/main/resources/used_words/";
    public static String USED_WORDS_FILE_NAME;

    private static final String EVALUATION_RESULTS_FILE_PATH = "src/main/resources/results/";
    public static final String EVALUATION_RESULTS_FILE_NAME = "evaluation_results.txt";

    /**
     * Writes an string to evaluation results file and System.out
     * 
     * @param w the text
     */
    public static void printEvaluationResult(String w)
    {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                EVALUATION_RESULTS_FILE_PATH + EVALUATION_RESULTS_FILE_NAME, true),
                        "utf-8"))) {
            System.out.println(w);
            writer.write(w + "\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the evaluation results file, if exists
     */
    public static void deleteEvaluationResultsFileIfExists()
    {
        try {
            Files.deleteIfExists(
                    new File(EVALUATION_RESULTS_FILE_PATH + EVALUATION_RESULTS_FILE_NAME).toPath());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes every lemma of the document and adds it to the set if necessary.
     * 
     * @param lemmaList list containing the lemmata
     */
    public static void addLemmaListToUsedWordSet(List<Token> lemmaList)
    {

        for (Token l : lemmaList) {
            addWordToUsedWordSet(l.getCoveredText());
        }
    }

    /**
     * Processes every word of the document text and adds it to the set if necessary.
     * 
     * @param documentText the document text
     */
    public static void addDocumentTextToUsedWordSet(String documentText)
    {
        String[] wordsOfText = documentText.split(" ");
        for (String s : wordsOfText) {
            addWordToUsedWordSet(s);
        }
    }

    /**
     * Writes used words to a file.
     */
    public static void writeUsedWordsToFile()
    {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(USED_WORDS_FILE_PATH + USED_WORDS_FILE_NAME), "utf-8"))) {
            for (String w : usedWords) {
                writer.write(w + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes any given text to a file. Mainly used for debug purposes.
     * 
     * @param w
     *            the text
     */
    public static void writeToFile(String w)
    {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(USED_WORDS_FILE_PATH + "debug_lemmalist_3.txt", true),
                "utf-8"))) {
            writer.write(w + "\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes any given text to a file. Mainly used for debug purposes.
     * 
     * @param w
     *            the text
     */
    public static void writeToFileWithFileName(String w, String filePath, String fileName)
    {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath + fileName +".txt", true),
                "utf-8"))) {
            writer.write(w + "\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load the used words from a file.
     */
    public static void loadUsedWordsFromFile()
    {
        String line;
        try {
            BufferedReader bufferreader = new BufferedReader(
                    new FileReader(USED_WORDS_FILE_PATH + USED_WORDS_FILE_NAME));
            line = bufferreader.readLine();
            while (line != null) {
                addWordToUsedWordSet(line);
                line = bufferreader.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a word in any valid notation to the used word set.
     * 
     * @param w
     */
    public static void addWordToUsedWordSet(String w)
    {
        if (!usedWords.contains(w))
            usedWords.add(w);
        usedWords.add(w.toLowerCase());
        usedWords.add(w.substring(0, 1).toUpperCase() + w.substring(1));
    }

    public static void setUsedWordsFileName(String fileName)
    {
        USED_WORDS_FILE_NAME = fileName;
    }

    public static Set<String> getUsedWords()
    {
        return usedWords;
    }
}
