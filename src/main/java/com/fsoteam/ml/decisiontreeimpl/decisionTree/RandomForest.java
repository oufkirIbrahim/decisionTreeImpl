package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.utils.LearningModel;

import java.util.*;
import java.util.stream.Collectors;

public class RandomForest implements LearningModel, Cloneable {
    private final List<DecisionTree> trees = new ArrayList<>();
    private final List<Attribute> attributes;
    private final List<DecisionTreeClass> classes;

    public RandomForest(int numberOfTrees, List<Attribute> attributes, List<DecisionTreeClass> classes) {
        this.attributes = attributes;
        this.classes = classes;

        Random random = new Random();
        int numAttributes = (int) Math.sqrt(attributes.size());
        for (int i = 0; i < numberOfTrees; i++) {

            Set<Attribute> attributesSubset = new HashSet<>();
            int attempts = 0;
            while (attributesSubset.size() < numAttributes && attempts < 1000) {
                attributesSubset.add(attributes.get(random.nextInt(attributes.size())));
                attempts++;
            }
            if (attributesSubset.size() < numAttributes) {
                System.out.println("Unable to find unique attributes after 1000 attempts for tree " + (i + 1));
            }

            trees.add(new J48DecisionTreeImpl(new Node(), classes, new ArrayList<>(attributesSubset)));
        }
    }

    @Override
    public void train(List<Instance> instances) {
        Random random = new Random();
        for (int i = 0; i < trees.size(); i++) {
            List<Instance> bootstrapSample = new ArrayList<>();
            for (int j = 0; j < instances.size(); j++) {
                bootstrapSample.add(instances.get(random.nextInt(instances.size())));
            }
            System.out.println("----------- BUILDING TREE-" + (i + 1) + " -----------");
            trees.get(i).train(bootstrapSample);
        }
    }

    @Override
    public String evaluate(Instance instance) {
        List<String> predictions = trees.stream()
                .map(tree -> tree.evaluate(instance))
                .collect(Collectors.toList());
        return getMajorityClass(predictions);
    }



    @Override
    public int[][] generateConfusionMatrix(List<Instance> dataTest) {
        int[][] matrix = new int[classes.size()][classes.size()];

        Map<String, Integer> classIndexMap = new HashMap<>();
        for (DecisionTreeClass dtClass : classes) {
            classIndexMap.put(dtClass.getClassName().toLowerCase(), dtClass.getClassId() - 1);
        }

        for (Instance instance : dataTest) {
            String actualClass = instance.getClassLabel().toLowerCase();
            String predictedClass = evaluate(instance).toLowerCase();
            int actualIndex = classIndexMap.getOrDefault(actualClass, -1);
            int predictedIndex = classIndexMap.getOrDefault(predictedClass, -1);

            if (actualIndex != -1 && predictedIndex != -1) {
                matrix[actualIndex][predictedIndex]++;
            }
        }
        return matrix;
    }

    private Attribute getAttributeByName(String attributeName) {
        return attributes.stream()
                .filter(attribute -> attribute.getAttributeName().equals(attributeName))
                .findFirst()
                .orElse(null);
    }

    private String getMajorityClass(List<String> classes) {
        return classes.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public RandomForest clone() {
        try {
            RandomForest clone = (RandomForest) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public LearningModel cloneModel() {
        return this.clone();
    }
}
