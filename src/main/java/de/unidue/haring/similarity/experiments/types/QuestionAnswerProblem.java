package de.unidue.haring.similarity.experiments.types;

public class QuestionAnswerProblem
{
    private String instanceText;
    private String questionText;
    private int questionId;
    private int questionAnswerProblemId;

    private String answerText1;
    private String answerText2;

    private int IDCorrectAnswer;
    private String questionType;

    private QuestionAnswerPair pair1;
    private QuestionAnswerPair pair2;

    public QuestionAnswerProblem()
    {

    }

    public QuestionAnswerProblem(String instanceText, String questionText, String answerText1,
            String answerText2, int IDCorrectAnswer)
    {
        this.questionText = questionText;
        this.answerText1 = answerText1;
        this.answerText2 = answerText2;
        this.IDCorrectAnswer = IDCorrectAnswer;
    }

    public String getInstanceText()
    {
        return instanceText;
    }

    public void setInstanceText(String instanceText)
    {
        this.instanceText = instanceText;
    }

    public int getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }

    public String getQuestionText()
    {
        return questionText;
    }

    public void setQuestionText(String questionText)
    {
        this.questionText = questionText;
    }

    public String getAnswerText1()
    {
        return answerText1;
    }

    public void setAnswerText1(String answerText1)
    {
        this.answerText1 = answerText1;
    }

    public String getAnswerText2()
    {
        return answerText2;
    }

    public void setAnswerText2(String answerText2)
    {
        this.answerText2 = answerText2;
    }

    public int getIDCorrectAnswer()
    {
        return IDCorrectAnswer;
    }

    public void setIDCorrectAnswer(int iDCorrectAnswer)
    {
        IDCorrectAnswer = iDCorrectAnswer;
    }

    public QuestionAnswerPair getPair1()
    {
        return pair1;
    }

    public void setPair1(QuestionAnswerPair pair1)
    {
        this.pair1 = pair1;
    }

    public QuestionAnswerPair getPair2()
    {
        return pair2;
    }

    public void setPair2(QuestionAnswerPair pair2)
    {
        this.pair2 = pair2;
    }

    public int getQuestionAnswerProblemId()
    {
        return questionAnswerProblemId;
    }

    public void setQuestionAnswerProblemId(int questionAnswerProblemId)
    {
        this.questionAnswerProblemId = questionAnswerProblemId;
    }

    public String getQuestionType()
    {
        return questionType;
    }

    public void setQuestionType(String questionType)
    {
        this.questionType = questionType;
    }
}
