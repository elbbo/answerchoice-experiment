package de.unidue.haring.similarity.experiments.pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.mallet.wordembeddings.MalletEmbeddingsAnnotator;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;
import de.unidue.haring.similarity.experiments.utils.CustomXmlReader;
import de.unidue.haring.similarity.experiments.utils.Evaluator;

public class Pipeline
{

    public static void main(String[] args) throws Exception
    {
        // CollectionReader xmlReader = createReader(CustomXmlReader.class, "TestDataInputFile",
        // "src/test/resources/data/extracted-test-data.xml");
        CollectionReader xmlReader = createReader(CustomXmlReader.class, "TestDataInputFile",
                "src/test/resources/data/test-data.xml");

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
                MalletEmbeddingsAnnotator.class, MalletEmbeddingsAnnotator.PARAM_MODEL_LOCATION,
                "src/test/resources/embeddings/glove.6B.300d.txt",
                // "src/test/resources/embeddings/glove.42B.300d.txt",
                // "src/test/resources/embeddings/GoogleNews-vectors-negative300.txt",
                MalletEmbeddingsAnnotator.PARAM_MODEL_IS_BINARY, false,
                MalletEmbeddingsAnnotator.PARAM_ANNOTATE_UNKNOWN_TOKENS, true,
                MalletEmbeddingsAnnotator.PARAM_LOWERCASE, true);
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

        // AnalysisEngineDescription customAnnotator =
        // createEngineDescription(CustomClassifier.class);

        AnalysisEngineDescription evaluator = createEngineDescription(Evaluator.class);

        SimplePipeline.runPipeline(xmlReader, aggr_seg, aggr_stpwords, aggr_pos, aggr_lem, aggr_mal,
                // customAnnotator
                evaluator);
    }
}
