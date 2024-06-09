package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.*;
import java.util.stream.Collectors;

// Class implementing the CART decision tree algorithm
public class CartDecisionTreeImpl extends DecisionTree {

    // Default constructor
    public CartDecisionTreeImpl() {
        super();
    }

    // Constructor with parameters
    public CartDecisionTreeImpl(Node root, List<DecisionTreeClass> classes, List<Attribute> attributes) {
        super(root, classes, attributes);
    }

    // Method to train the decision tree with a list of instances
    @Override
    public void train(List<Instance> instances) {
        buildTree(super.getRoot(), super.getAttributes(), instances);
    }

    // Recursive method to build the decision tree
    private void buildTree(Node currentNode, List<Attribute> attributesRestants, List<Instance> instancesRestantes) {
        // Calculate the number of appearances of each class and branch
        calculateNp(instancesRestantes, attributesRestants);

        // Find the best split based on Gini index
        Split bestSplit = findBestSplit(instancesRestantes, attributesRestants);
        // If no best split found, make the current node a leaf node
        if (bestSplit == null) {
            currentNode.setLeaf(true);
            currentNode.setMajorClass(calculateMajorityClass(instancesRestantes));
            return;
        }

        // Clone the best attribute and set it as the attribute of the current node
        Attribute attributeCopy = bestSplit.attribute.clone();
        currentNode.setAttribute(attributeCopy);

        // For each branch of the best attribute, create a child node and build the subtree
        for (Branch branch : attributeCopy.getBranches()) {
            // Split the instances based on the branch value
            List<Instance> splitInstances = splitDataset(instancesRestantes, attributeCopy, branch.getValue());
            // Create a list of remaining attributes excluding the best attribute
            List<Attribute> remainingAttributes = attributesRestants.stream()
                    .filter(attr -> !attr.getAttributeName().equals(attributeCopy.getAttributeName()))
                    .map(Attribute::new)
                    .collect(Collectors.toList());

            // Create a new child node and set it as the child of the branch
            Node childNode = new Node();
            branch.setChildNode(childNode);
            // Build the subtree
            buildTree(childNode, remainingAttributes, splitInstances);
        }
    }

    // Method to calculate the Gini index of a list of instances
    private double calculateGiniIndex(List<Instance> instances) {
        int totalInstances = instances.size();
        Map<String, Integer> classCounts = new HashMap<>();

        // Count the number of instances for each class
        for (Instance instance : instances) {
            classCounts.put(instance.getClassLabel(),
                    classCounts.getOrDefault(instance.getClassLabel(), 0) + 1);
        }

        // Calculate the Gini index
        double gini = 1.0;
        for (int count : classCounts.values()) {
            double probability = (double) count / totalInstances;
            gini -= probability * probability;
        }

        return gini;
    }

    // Method to find the best split based on Gini index
    private Split findBestSplit(List<Instance> instances, List<Attribute> attributes) {
        Split bestSplit = null;
        double bestGini = Double.MAX_VALUE;

        // For each attribute and each branch, calculate the weighted Gini index
        for (Attribute attribute : attributes) {
            for (Branch branch : attribute.getBranches()) {
                // Split the instances into left and right splits based on the branch value
                List<Instance> leftSplit = instances.stream()
                        .filter(instance -> instance.getSingleAttributeValue(attribute).equals(branch.getValue()))
                        .collect(Collectors.toList());
                List<Instance> rightSplit = instances.stream()
                        .filter(instance -> !instance.getSingleAttributeValue(attribute).equals(branch.getValue()))
                        .collect(Collectors.toList());

                // Calculate the Gini index for the left and right splits
                double giniLeft = calculateGiniIndex(leftSplit);
                double giniRight = calculateGiniIndex(rightSplit);
                // Calculate the weighted Gini index
                double weightedGini = ((double) leftSplit.size() / instances.size()) * giniLeft
                        + ((double) rightSplit.size() / instances.size()) * giniRight;

                // If the weighted Gini index is less than the best Gini index found so far, update the best split
                if (weightedGini < bestGini) {
                    bestGini = weightedGini;
                    bestSplit = new Split(attribute, branch.getValue());
                }
            }
        }

        return bestSplit;
    }

    // Method to split a dataset based on an attribute value
    private List<Instance> splitDataset(List<Instance> instances, Attribute attribute, String value) {
        return instances.stream()
                .filter(instance -> instance.getSingleAttributeValue(attribute).equals(value))
                .collect(Collectors.toList());
    }

    // Inner class representing a split
    private static class Split {
        private final Attribute attribute;
        private final String value;

        // Constructor with parameters
        public Split(Attribute attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }
    }
}