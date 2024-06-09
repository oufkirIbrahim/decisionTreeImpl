package com.fsoteam.ml.decisiontreeimpl.utils;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.ArrayList;
import java.util.List;

// Class to initialize and manage a dataset for decision tree learning
public class DatasetInitializer {

    // Source of the dataset
    private String dataSetSource;
    // List of instances in the dataset
    private List<Instance> instanceData;
    // List of attributes in the dataset
    private List<Attribute> attributes;
    // List of classes in the dataset
    private List<DecisionTreeClass> decisionTreeClasses;
    // Name of the class attribute
    private String className;

    // Default constructor
    public DatasetInitializer() {
        this.className = "";
        this.attributes = new ArrayList<Attribute>();
        this.decisionTreeClasses = new ArrayList<DecisionTreeClass>();
        this.instanceData = new ArrayList<Instance>();
        this.dataSetSource = "";
    }

    // Constructor with parameters
    public DatasetInitializer(String ataSetSource, List<Instance> instanceData, List<Attribute> attributes, List<DecisionTreeClass> decisionTreeClasses, String className) {
        this.instanceData = instanceData;
        this.attributes = attributes;
        this.decisionTreeClasses = decisionTreeClasses;
        this.className = className;
    }

    // Getter for dataSetSource
    public String getDataSetSource() {
        return dataSetSource;
    }

    // Setter for dataSetSource
    public void setDataSetSource(String dataSetSource) {
        this.dataSetSource = dataSetSource;
    }

    // Getter for instanceData
    public List<Instance> getInstanceData() {
        return instanceData;
    }

    // Setter for instanceData
    public void setInstanceData(List<Instance> instanceData) {
        this.instanceData = instanceData;
    }

    // Getter for attributes
    public List<Attribute> getAttributes() {
        return attributes;
    }

    // Setter for attributes
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    // Getter for decisionTreeClasses
    public List<DecisionTreeClass> getDecisionTreeClasses() {
        return decisionTreeClasses;
    }

    // Setter for decisionTreeClasses
    public void setDecisionTreeClasses(List<DecisionTreeClass> decisionTreeClasses) {
        this.decisionTreeClasses = decisionTreeClasses;
    }

    // Getter for className
    public String getClassName() {
        return className;
    }

    // Setter for className
    public void setClassName(String className) {
        this.className = className;
    }
}