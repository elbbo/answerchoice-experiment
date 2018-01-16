package de.unidue.haring.similarity.experiments.types;

import java.util.Map;
import java.util.TreeMap;


public class QuestionAnswerProblemFactory
{
    private static Map<Integer, QuestionAnswerProblem> qList;

    public QuestionAnswerProblemFactory()
    {
        qList = new TreeMap<Integer, QuestionAnswerProblem>();
    }

    /**
     * Adds a new QuestionAnswerProblem
     * 
     * @param id
     *            the problem id
     * @param questionAnswerProblem
     *            the actual problem
     */
    public void addQuestionAnswerProblem(int id, QuestionAnswerProblem questionAnswerProblem)
    {
        qList.put((Integer) id, questionAnswerProblem);
    }

    /**
     * Gets a QuestionAnswerProblem by its id
     * 
     * @param id
     *            the problem id
     * @return the QuestionAnswerProblem
     */
    public static QuestionAnswerProblem getQuestionAnswerProblemById(int id)
    {
        return qList.get(id);
    }

    /**
     * Gets all QuestionAnswerProblems.
     * 
     * @return a Map storing all QuestionAnswerProblems
     */
    public static Map<Integer, QuestionAnswerProblem> getQuestionAnswerProblems()
    {
        return qList;
    }
}
