package de.unidue.haring.similarity.experiments.types;

public class Answer
{
    private String answerText;
    private int Id;
    private boolean isCorrect;

    public Answer(String answerText, int Id, boolean isCorrect)
    {
        super();
        this.answerText = answerText;
        this.Id = Id;
        this.isCorrect = isCorrect;
    }

    public String getAnswerText()
    {
        return answerText;
    }

    public int getId()
    {
        return Id;
    }

    public boolean isCorrect()
    {
        return isCorrect;
    }
}
