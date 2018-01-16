package de.unidue.haring.similarity.experiments.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;

public class QuestionAnswerPair
{
    private String questionText;
    private String answerText;
    private Question question;
    private Answer answer;

    private List<Lemma> questionLemmas;
    private List<Lemma> answerLemmas;

    private Map<String, SemanticRelatedness> semanticRelatednessList;

    public QuestionAnswerPair(String questionText, String answerText, String instanceText,
            int questionId, int answerId, int goldAnswerId, boolean isCorrectAnswer)
    {
        semanticRelatednessList = new HashMap<String, SemanticRelatedness>();
        this.questionText = questionText;
        this.answerText = answerText;
        question = new Question(instanceText, questionText, questionId, goldAnswerId);
        answer = new Answer(answerText, answerId, isCorrectAnswer);
    }

    public String getQuestionText()
    {
        return questionText;
    }

    public String getAnswerText()
    {
        return answerText;
    }

    public Question getQuestion()
    {
        return question;
    }

    public Answer getAnswer()
    {
        return answer;
    }

    public List<Lemma> getQuestionLemmas()
    {
        return questionLemmas;
    }

    public void setQuestionLemmas(List<Lemma> questionLemmas)
    {
        this.questionLemmas = questionLemmas;
    }

    public List<Lemma> getAnswerLemmas()
    {
        return answerLemmas;
    }

    public void setAnswerLemmas(List<Lemma> answerLemmas)
    {
        this.answerLemmas = answerLemmas;
    }

    public Map<String, SemanticRelatedness> getRelatednessMap()
    {
        return semanticRelatednessList;
    }

    public SemanticRelatedness getRelatedness(String similarityMeasureMethod)
    {
        return semanticRelatednessList.get(similarityMeasureMethod);
    }

    public void setRelatedness(SemanticRelatedness relatedness)
    {
        semanticRelatednessList.put(relatedness.getMeasureMethod(), relatedness);
    }
}
