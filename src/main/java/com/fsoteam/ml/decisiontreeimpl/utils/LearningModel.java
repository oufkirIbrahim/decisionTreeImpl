package com.fsoteam.ml.decisiontreeimpl.utils;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Node;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.List;

public interface LearningModel extends Cloneable {

    public void train(List<Instance> instanceList);
    public int[][] generateConfusionMatrix(List<Instance> dataTest);
    public String evaluate(Instance instance);
    public LearningModel cloneModel();
}
