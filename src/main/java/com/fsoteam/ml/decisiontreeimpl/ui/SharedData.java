package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.DecisionTree;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.utils.DatasetInitializer;
import com.fsoteam.ml.decisiontreeimpl.utils.LearningModel;
import com.fsoteam.ml.decisiontreeimpl.utils.TrainedModelObserver;

import java.util.ArrayList;
import java.util.List;

public class SharedData {
    private static SharedData instance = null;
    private DatasetInitializer datasetInitializer;
    private LearningModel trainedModel;
    private List<Instance> trainingData;
    private List<Instance> testingData;
    private String learningAlgorithm;
    private List<TrainedModelObserver> observers = new ArrayList<>();

    private SharedData() {
        // Private constructor to prevent creating multiple instances
    }

    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public DatasetInitializer getDatasetInitializer() {
        return datasetInitializer;
    }

    public void setDatasetInitializer(DatasetInitializer datasetInitializer) {
        this.datasetInitializer = datasetInitializer;
    }

    public LearningModel getTrainedModel() {
        return trainedModel;
    }

    public void setTrainedModel(LearningModel trainedModel) {
        this.trainedModel = trainedModel;
        notifyObservers();
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

    public String getLearningAlgorithm() {
        return learningAlgorithm;
    }

    public void setLearningAlgorithm(String learningAlgorithm) {
        this.learningAlgorithm = learningAlgorithm;
    }

    public void addObserver(TrainedModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TrainedModelObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (TrainedModelObserver observer : observers) {
            observer.onTrainedModelChanged();
        }
    }
}
