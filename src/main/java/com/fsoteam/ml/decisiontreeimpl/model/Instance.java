package com.fsoteam.ml.decisiontreeimpl.model;

import java.util.List;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Branch;

public class Instance {

    private int instanceId;
    private List<String> attributeValues;
    private String classLabel;

    public Instance(int instanceId, List<String> attributeValues, String classLabel) {
        this.instanceId = instanceId;
        this.attributeValues = attributeValues;
        this.classLabel = classLabel;
    }

    public Instance(int instanceId, List<String> attributeValues) {
        this.instanceId = instanceId;
        this.attributeValues = attributeValues;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public List<String> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<String> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public String getSingleAttributeValue(Attribute att) {
        for (String attInstance : attributeValues) {
            for (Branch b : att.getBranches()) {
                if (attInstance.equals(b.getValue())) {
                    return attInstance;
                }
            }
        }
        return "";
    }

    public String getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(String classLabel) {
        this.classLabel = classLabel;
    }

}
