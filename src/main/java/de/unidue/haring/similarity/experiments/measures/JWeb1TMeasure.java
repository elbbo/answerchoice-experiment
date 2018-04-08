package de.unidue.haring.similarity.experiments.measures;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.uima.cas.CAS;

import com.googlecode.jweb1t.FrequencyDistribution;
import com.googlecode.jweb1t.JWeb1TAggregator;
import com.googlecode.jweb1t.JWeb1TSearcher;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.provider.FrequencyCountProvider;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerPair;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;

public class JWeb1TMeasure
    extends SimilarityMeasure
{
    private static final String MEASURE_METHOD_NAME = "JWeb1TMeasure";
    private static final String DATA_DIR = "src/test/resources/Web1t/";
    private static final String INDEX_FILE_1 = "src/test/resources/Web1t/index-1gms";
    private static JWeb1TSearcher web1t;
    private static JWeb1TAggregator aggregator;

    private static final boolean NORMALIZE_FREQUENCY = true;
    private static long normalization;

    @Override
    public QuestionAnswerProblem measureSimilarity(CAS aCAS,
            QuestionAnswerProblem questionAnswerProblem)
    {
        QuestionAnswerPair questionAnswerPair1 = questionAnswerProblem.getPair1();
        QuestionAnswerPair questionAnswerPair2 = questionAnswerProblem.getPair2();

        try {
            if (web1t == null) {
                web1t = new JWeb1TSearcher(INDEX_FILE_1);

                aggregator = new JWeb1TAggregator(DATA_DIR, 1);
                aggregator.create();
                final File countFile = new File(DATA_DIR, JWeb1TAggregator.AGGREGATED_COUNTS_FILE);

                // Only using unigrams
                normalization = web1t.getNrOfNgrams(1);
            }
            double a1Freq = sumLemmataFrequency(questionAnswerPair1.getAnswerLemmas());
            double a2Freq = sumLemmataFrequency(questionAnswerPair2.getAnswerLemmas());

            setSemanticRelatedness(MEASURE_METHOD_NAME, questionAnswerPair1, questionAnswerPair2,
                    a1Freq, a2Freq);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return questionAnswerProblem;
    }

    /**
     * Summarizes over the frequencies of the respective lemmas. Normalizes the values, if desired.
     * 
     * @param lemmata
     *            list containing all answer lemmata
     * @return the (normalized) frequency
     * @throws IOException
     */
    private double sumLemmataFrequency(List<Lemma> lemmata) throws IOException
    {
        double aFreq = 0.0;
        int weighting = 1;

        for (Lemma l : lemmata) {
            long frequency = web1t.getFrequency(l.getCoveredText());
            if (NORMALIZE_FREQUENCY) {
                if (frequency > 0) {
                    double logCount = Math.log(weighting * ((double) frequency) / normalization);
                    aFreq += logCount;
                }
                else {
                    aFreq += Math.log(1.0 / normalization);
                }
            }
            else {
                aFreq += frequency;
            }
        }
        if (NORMALIZE_FREQUENCY) {
            return aFreq * -1;
        }
        else {
            return aFreq;
        }
    }

    @Override
    public String getMeasureMethodName()
    {
        return MEASURE_METHOD_NAME;
    }
}
