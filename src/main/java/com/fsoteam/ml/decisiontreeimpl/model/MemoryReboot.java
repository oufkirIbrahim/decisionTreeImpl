package com.fsoteam.ml.decisiontreeimpl.model;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.DecisionTree;
import com.fsoteam.ml.decisiontreeimpl.ui.SharedData;

import java.util.List;

public class MemoryReboot {
    private String memoryName;
    private List<Instance> trainingData;
    private List<Instance> testingData;
    private DecisionTree trainedModel;
    private String memoryOutput;

    public MemoryReboot() {
        SharedData sharedData = SharedData.getInstance();

        this.trainingData = sharedData.getTrainingData();
        this.testingData = sharedData.getTestingData();
        this.trainedModel = sharedData.getTrainedModel().clone();
        this.memoryOutput = "";
        this.memoryName = "";
    }

    public MemoryReboot(List<Instance> trainingData, List<Instance> testingData, DecisionTree trainedModel) {
        this.trainingData = trainingData;
        this.testingData = testingData;
        this.trainedModel = trainedModel;
    }

    public List<Instance> getTrainingData() {
        return trainingData;
    }

    public void setTrainingData(List<Instance> trainingData) {
        this.trainingData = trainingData;
    }

    public List<Instance> getTestingData() {
        return testingData;
    }

    public void setTestingData(List<Instance> testingData) {
        this.testingData = testingData;
    }

    public DecisionTree getTrainedModel() {
        return trainedModel;
    }

    public void setTrainedModel(DecisionTree trainedModel) {
        this.trainedModel = trainedModel;
    }

    public String getMemoryName() {
        return memoryName;
    }

    public void setMemoryName(String memoryName) {
        this.memoryName = memoryName;
    }

    public String getMemoryOutput() {
        return memoryOutput;
    }

    public void setMemoryOutput(String memoryOutput) {
        this.memoryOutput = memoryOutput;
    }
}
