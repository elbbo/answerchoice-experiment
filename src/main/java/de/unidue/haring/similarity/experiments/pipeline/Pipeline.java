package de.unidue.haring.similarity.experiments.pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;
import de.unidue.haring.similarity.experiments.customAnnotators.CustomMalletEmbeddingsAnnotator;
import de.unidue.haring.similarity.experiments.utils.CustomXmlReader;
import de.unidue.haring.similarity.experiments.utils.Evaluator;
import de.unidue.haring.similarity.experiments.utils.GeneralPipelineUtils;

public class Pipeline
{
    static Map<String, Boolean> embeddingsMap;

    public static void main(String[] args) throws Exception
    {
        GeneralPipelineUtils.deleteEvaluationResultsFileIfExists();
        prepareEmbeddingsMap();

        // runPipelineWithAllEmbeddings("src/test/resources/data/extracted-test-data.xml");

        // Run pipeline for test data all
        runPipelineWithAllEmbeddings("src/test/resources/data/test-data.xml");
        // Run pipeline for test data yes/no questions only
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_yes_no.xml");
        // Run pipeline for test data without yes/no questions
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_without_yes_no.xml");
        // Run pipeline for test data how questions only
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_how.xml");
        // Run pipeline for test data what/which questions only
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_what_which.xml");
        // Run pipeline for test data when/how questions only
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_when_how.xml");
        // Run pipeline for test data when questions only
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_when.xml");
        // Run pipeline for test data where questions only
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_where.xml");
        // Run pipeline for test data who/whose questions only
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_who_whose.xml");
        // Run pipeline for test data why questions only
        runPipelineWithAllEmbeddings("src/test/resources/data/testdata_subset_why.xml");
    }

    private static void runPipelineWithAllEmbeddings(String testDataFilePath) throws Exception
    {
        for (Map.Entry<String, Boolean> entry : embeddingsMap.entrySet()) {
            runPipeline(testDataFilePath, entry.getKey(), entry.getValue().booleanValue());
        }
    }

    private static void runPipeline(String testDataFilePath, String wordEmbeddingsData,
            boolean paramLowerCase)
        throws Exception
    {
        CollectionReader xmlReader = createReader(CustomXmlReader.class, "TestDataInputFile",
                testDataFilePath);
        GeneralPipelineUtils.setUsedWordsFileName("test_words.txt");

        AnalysisEngineDescription stanfordSegmenter = createEngineDescription(
                StanfordSegmenter.class, StanfordSegmenter.PARAM_LANGUAGE, "en",
                StanfordSegmenter.PARAM_BOUNDARY_TOKEN_REGEX, "#",
                StanfordSegmenter.PARAM_TOKEN_REGEXES_TO_DISCARD, "#");
        AggregateBuilder builder = new AggregateBuilder();
        builder.add(stanfordSegmenter, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.INSTANCE_VIEW);
        builder.add(stanfordSegmenter, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.QUESTION_VIEW);
        builder.add(stanfordSegmenter, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.ANSWER_VIEW_1);
        builder.add(stanfordSegmenter, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.ANSWER_VIEW_2);
        AnalysisEngineDescription aggr_seg = builder.createAggregateDescription();

        AnalysisEngineDescription posTagger = createEngineDescription(OpenNlpPosTagger.class,
                OpenNlpPosTagger.PARAM_LANGUAGE, "en");
        builder = new AggregateBuilder();
        builder.add(posTagger, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.INSTANCE_VIEW);
        builder.add(posTagger, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.QUESTION_VIEW);
        builder.add(posTagger, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.ANSWER_VIEW_1);
        builder.add(posTagger, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.ANSWER_VIEW_2);
        AnalysisEngineDescription aggr_pos = builder.createAggregateDescription();

        AnalysisEngineDescription stopWordRemover = createEngineDescription(StopWordRemover.class,
                StopWordRemover.PARAM_MODEL_LOCATION, "src/test/resources/stopwords_en.txt");
        builder = new AggregateBuilder();
        builder.add(stopWordRemover, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.INSTANCE_VIEW);
        builder.add(stopWordRemover, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.QUESTION_VIEW);
        builder.add(stopWordRemover, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.ANSWER_VIEW_1);
        builder.add(stopWordRemover, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.ANSWER_VIEW_2);
        AnalysisEngineDescription aggr_stpwords = builder.createAggregateDescription();

        AnalysisEngineDescription lemmatizer = createEngineDescription(
                LanguageToolLemmatizer.class);
        builder = new AggregateBuilder();
        builder.add(lemmatizer, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.INSTANCE_VIEW);
        builder.add(lemmatizer, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.QUESTION_VIEW);
        builder.add(lemmatizer, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.ANSWER_VIEW_1);
        builder.add(lemmatizer, CustomXmlReader.INITIAL_VIEW, CustomXmlReader.ANSWER_VIEW_2);
        AnalysisEngineDescription aggr_lem = builder.createAggregateDescription();

        AnalysisEngineDescription malletEmbeddingsAnnotator = createEngineDescription(
                CustomMalletEmbeddingsAnnotator.class,
                CustomMalletEmbeddingsAnnotator.PARAM_MODEL_LOCATION, wordEmbeddingsData,
                CustomMalletEmbeddingsAnnotator.PARAM_MODEL_IS_BINARY, false,
                CustomMalletEmbeddingsAnnotator.PARAM_ANNOTATE_UNKNOWN_TOKENS, true,
                CustomMalletEmbeddingsAnnotator.PARAM_LOWERCASE, paramLowerCase,
                CustomMalletEmbeddingsAnnotator.PARAM_ONLY_LOAD_USED_TOKENS, true);
        builder = new AggregateBuilder();
        builder.add(malletEmbeddingsAnnotator, CustomXmlReader.INITIAL_VIEW,
                CustomXmlReader.INSTANCE_VIEW);
        builder.add(malletEmbeddingsAnnotator, CustomXmlReader.INITIAL_VIEW,
                CustomXmlReader.QUESTION_VIEW);
        builder.add(malletEmbeddingsAnnotator, CustomXmlReader.INITIAL_VIEW,
                CustomXmlReader.ANSWER_VIEW_1);
        builder.add(malletEmbeddingsAnnotator, CustomXmlReader.INITIAL_VIEW,
                CustomXmlReader.ANSWER_VIEW_2);
        AnalysisEngineDescription aggr_mal = builder.createAggregateDescription();

        AnalysisEngineDescription evaluator = createEngineDescription(Evaluator.class,
                Evaluator.PARAM_TEST_DATA_FILE_PATH, testDataFilePath,
                Evaluator.PARAM_USED_WORD_EMBEDDINGS, wordEmbeddingsData);

        System.out.println("Running Pipeline on data: " + testDataFilePath + ". Used embeddings: "
                + wordEmbeddingsData);
        SimplePipeline.runPipeline(xmlReader, aggr_seg, aggr_stpwords, aggr_pos, aggr_lem, aggr_mal,
                evaluator);
    }

    private static void prepareEmbeddingsMap()
    {
        embeddingsMap = new LinkedHashMap<String, Boolean>();
        // embeddingsMap.put("src/test/resources/embeddings/embeddings_test.txt", Boolean.valueOf(true));
        embeddingsMap.put("src/test/resources/embeddings/glove.6B.50d.txt", Boolean.valueOf(true));
        embeddingsMap.put("src/test/resources/embeddings/glove.6B.100d.txt", Boolean.valueOf(true));
        embeddingsMap.put("src/test/resources/embeddings/glove.6B.200d.txt", Boolean.valueOf(true));
        embeddingsMap.put("src/test/resources/embeddings/glove.6B.300d.txt", Boolean.valueOf(true));
        embeddingsMap.put("src/test/resources/embeddings/glove.42B.300d.txt", Boolean.valueOf(true));
        embeddingsMap.put("src/test/resources/embeddings/glove.840B.300d.txt", Boolean.valueOf(false));
        embeddingsMap.put("src/test/resources/embeddings/GoogleNews-vectors-negative300.txt", Boolean.valueOf(false));
        embeddingsMap.put("src/test/resources/embeddings/wiki.de.vec", Boolean.valueOf(true));
    }
}
