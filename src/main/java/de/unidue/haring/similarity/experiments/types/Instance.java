package de.unidue.haring.similarity.experiments.types;

import java.util.List;

public class Instance
{
    private String instanceText;
    private String instanceScenario;
    private int instanceId;
    private List<QuestionAnswerProblem> qList;

    public Instance(String instanceText, String instanceScenario, int instanceId,
            List<QuestionAnswerProblem> qList)
    {
        this.instanceText = instanceText;
        this.instanceScenario = instanceScenario;
        this.instanceId = instanceId;
        this.qList = qList;
    }

    public String getInstanceText()
    {
        return instanceText;
    }

    public void setInstanceText(String instanceText)
    {
        this.instanceText = instanceText;
    }

    public List<QuestionAnswerProblem> getqList()
    {
        return qList;
    }

    public void setqList(List<QuestionAnswerProblem> qList)
    {
        this.qList = qList;
    }

    public String getInstanceScenario()
    {
        return instanceScenario;
    }

    public void setInstanceScenario(String instanceScenario)
    {
        this.instanceScenario = instanceScenario;
    }

    public int getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(int instanceId)
    {
        this.instanceId = instanceId;
    }
}
