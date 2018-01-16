package de.unidue.haring.similarity.experiments.types;

public class SemanticRelatedness
{
    // the used measure method
    private String measureMethod;
    // the actual relatedness value
    private double semanticRelatednessValue;
    
    public SemanticRelatedness(String measureMethod) {
        this.measureMethod = measureMethod;
    }

    public double getSemanticRelatednessValue()
    {
        return semanticRelatednessValue;
    }

    public void setSemanticRelatednessValue(double semanticRelatednessValue)
    {
        this.semanticRelatednessValue = semanticRelatednessValue;
    }
    
    public String getMeasureMethod() {
        return measureMethod;
    }
}
