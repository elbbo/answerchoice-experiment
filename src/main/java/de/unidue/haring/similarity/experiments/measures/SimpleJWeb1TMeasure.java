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

public class SimpleJWeb1TMeasure
    extends JWeb1TMeasure
{
    private static final String MEASURE_METHOD_NAME = "SimpleJWeb1TMeasure";

    public SimpleJWeb1TMeasure()
    {
        super();
    }

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

            setSemanticRelatedness(MEASURE_METHOD_NAME, questionAnswerPair1, questionAnswerPair2,
                    a1Freq, a2Freq);
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
