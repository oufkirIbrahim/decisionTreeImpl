package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.utils.LearningModel;

import java.util.*;
import java.util.stream.Collectors;

// Class implementing the Random Forest algorithm
public class RandomForest implements LearningModel, Cloneable {
    // List of decision trees in the forest
    private final List<DecisionTree> trees = new ArrayList<>();
    // List of attributes in the dataset
    private final List<Attribute> attributes;
    // List of classes in the dataset
    private final List<DecisionTreeClass> classes;

    // Constructor to initialize the random forest with a specified number of trees
    public RandomForest(int numberOfTrees, List<Attribute> attributes, List<DecisionTreeClass> classes) {
        this.attributes = attributes;
        this.classes = classes;

        Random random = new Random();
        int numAttributes = (int) Math.sqrt(attributes.size());
        for (int i = 0; i < numberOfTrees; i++) {
            // Select a random subset of attributes for each tree
            Set<Attribute> attributesSubset = new HashSet<>();
            int attempts = 0;
            while (attributesSubset.size() < numAttributes && attempts < 1000) {
                attributesSubset.add(attributes.get(random.nextInt(attributes.size())));
                attempts++;
            }
            if (attributesSubset.size() < numAttributes) {
                System.out.println("Unable to find unique attributes after 1000 attempts for tree " + (i + 1));
            }

            // Add a new decision tree to the forest
            trees.add(new J48DecisionTreeImpl(new Node(), classes, new ArrayList<>(attributesSubset)));
        }
    }

    // Method to train the random forest with a list of instances
    @Override
    public void train(List<Instance> instances) {
        Random random = new Random();
        for (int i = 0; i < trees.size(); i++) {
            // Create a bootstrap sample for each tree
            List<Instance> bootstrapSample = new ArrayList<>();
            for (int j = 0; j < instances.size(); j++) {
                bootstrapSample.add(instances.get(random.nextInt(instances.size())));
            }
            System.out.println("----------- BUILDING TREE-" + (i + 1) + " -----------");
            // Train each tree with the bootstrap sample
            trees.get(i).train(bootstrapSample);
        }
    }

    // Method to evaluate an instance by getting the majority vote from all trees
    @Override
    public String evaluate(Instance instance) {
        List<String> predictions = trees.stream()
                .map(tree -> tree.evaluate(instance))
                .collect(Collectors.toList());
        return getMajorityClass(predictions);
    }

    // Method to generate a confusion matrix for the random forest
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

    // Helper method to get an attribute by its name
    private Attribute getAttributeByName(String attributeName) {
        return attributes.stream()
                .filter(attribute -> attribute.getAttributeName().equals(attributeName))
                .findFirst()
                .orElse(null);
    }

    // Helper method to get the majority class from a list of classes
    private String getMajorityClass(List<String> classes) {
        return classes.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Method to clone the random forest
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

    // Method to clone the learning model
    @Override
    public LearningModel cloneModel() {
        return this.clone();
    }
}