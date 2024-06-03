package com.fsoteam.ml.decisiontreeimpl.utils;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import javafx.collections.ObservableList;

import java.util.List;

public class DatasetInitializer {

    private String dataSetSource;
    private List<Instance> instanceData;
    private List<Attribute> attributes;
    private List<DecisionTreeClass> decisionTreeClasses;

    public DatasetInitializer() {
    }

    public DatasetInitializer(String ataSetSource, List<Instance> instanceData, List<Attribute> attributes, List<DecisionTreeClass> decisionTreeClasses) {
        this.instanceData = instanceData;
        this.attributes = attributes;
        this.decisionTreeClasses = decisionTreeClasses;
    }

    public String getDataSetSource() {
        return dataSetSource;
    }

    public void setDataSetSource(String dataSetSource) {
        this.dataSetSource = dataSetSource;
    }

    public List<Instance> getInstanceData() {
        return instanceData;
    }

    public void setInstanceData(List<Instance> instanceData) {
        this.instanceData = instanceData;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<DecisionTreeClass> getDecisionTreeClasses() {
        return decisionTreeClasses;
    }

    public void setDecisionTreeClasses(List<DecisionTreeClass> decisionTreeClasses) {
        this.decisionTreeClasses = decisionTreeClasses;
    }
}
