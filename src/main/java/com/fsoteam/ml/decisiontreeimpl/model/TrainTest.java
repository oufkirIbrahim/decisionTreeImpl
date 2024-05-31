package com.fsoteam.ml.decisiontreeimpl.model;

import java.util.ArrayList;
import java.util.List;

public class TrainTest {

    private List<Instance> train = new ArrayList<Instance>();
    private List<Instance> test = new ArrayList<Instance>();

    public TrainTest() {
    }

    public TrainTest(List<Instance> train, List<Instance> test) {
        this.train = train;
        this.test = test;
    }

    public void setTrain(List<Instance> train) {
        this.train = train;
    }

    public void setTest(List<Instance> test) {
        this.test = test;
    }

    public List<Instance> getTrain() {
        return train;
    }

    public List<Instance> getTest() {
        return test;
    }

}