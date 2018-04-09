package de.unidue.haring.similarity.experiments.measures;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.googlecode.jweb1t.JWeb1TAggregator;
import com.googlecode.jweb1t.JWeb1TSearcher;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;

public class JWeb1TMeasure
    extends EmbeddingsSimilarityMeasure
{
    private static final String DATA_DIR = "src/test/resources/Web1t/";
    private static final String INDEX_FILE_1 = "src/test/resources/Web1t/index-1gms";
    protected static JWeb1TSearcher web1t;
    private static JWeb1TAggregator aggregator;

    private static long normalization;
    private static final boolean NORMALIZE_FREQUENCY = true;

    protected void initJWeb1T()
    {
        try {
            web1t = new JWeb1TSearcher(INDEX_FILE_1);

            aggregator = new JWeb1TAggregator(DATA_DIR, 1);
            aggregator.create();
            final File countFile = new File(DATA_DIR, JWeb1TAggregator.AGGREGATED_COUNTS_FILE);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Only using unigrams
        normalization = web1t.getNrOfNgrams(1);
    }

    /**
     * Summarizes over the frequencies of the respective lemmas. Normalizes the values, if desired.
     * 
     * @param lemmata
     *            list containing all answer lemmata
     * @return the (normalized) frequency
     * @throws IOException
     */
    protected double sumLemmataFrequency(List<Lemma> lemmata) throws IOException
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
}
