package com.fsoteam.ml.decisiontreeimpl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataSet {

    private List<Instance> dataSet;

    public DataSet() {
    }

    public DataSet(List<Instance> dataSet) {
        this.dataSet = dataSet;
    }

    public TrainTest trainTest(double percentage) {
        int testSize = (int) (percentage * dataSet.size() / 100);
        List<Instance> test = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < testSize; i++) {
            int randomIndex = random.nextInt(dataSet.size());
            test.add(dataSet.get(randomIndex));
            dataSet.remove(randomIndex);
        }
        return new TrainTest(new ArrayList<>(dataSet), test);
    }

    public List<TrainTest> crossValidation(int k) {
        int dataSize = dataSet.size();
        int foldSize = dataSize / k;
        List<TrainTest> divises = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int startIndex = i * foldSize;
            int endIndex = (i + 1) * foldSize;
            List<Instance> test = new ArrayList<>(dataSet.subList(startIndex, endIndex));
            List<Instance> train = new ArrayList<>(dataSet.subList(0, startIndex));
            train.addAll(new ArrayList<>(dataSet.subList(endIndex, dataSize)));
            TrainTest divise = new TrainTest(train, test);
            divises.add(divise);
        }
        return divises;
    }
}