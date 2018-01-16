package de.unidue.haring.similarity.experiments.types;

public class Question
{
    private String instanceText;
    private String questionText;

    private int Id;
    private int goldAnswerId;

    public Question(String instanceText, String questionText, int Id, int goldAnswerId)
    {
        super();
        this.instanceText = instanceText;
        this.questionText = questionText;
        this.Id = Id;
        this.goldAnswerId = goldAnswerId;
    }

}
