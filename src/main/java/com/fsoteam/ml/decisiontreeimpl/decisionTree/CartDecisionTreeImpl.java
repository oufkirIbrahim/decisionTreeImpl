package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.*;
import java.util.stream.Collectors;

public class CartDecisionTreeImpl extends DecisionTree {

    public CartDecisionTreeImpl() {
        super();
    }

    public CartDecisionTreeImpl(Node root, List<DecisionTreeClass> classes, List<Attribute> attributes) {
        super(root, classes, attributes);
    }

    @Override
    public void train(List<Instance> instances) {
        buildTree(super.getRoot(), super.getAttributes(), instances);
    }

    private void buildTree(Node currentNode, List<Attribute> attributesRestants, List<Instance> instancesRestantes) {
        calculateNp(instancesRestantes, attributesRestants);

        Split bestSplit = findBestSplit(instancesRestantes, attributesRestants);
        if (bestSplit == null) {
            currentNode.setLeaf(true);
            currentNode.setMajorClass(calculateMajorityClass(instancesRestantes));
            return;
        }

        Attribute attributeCopy = bestSplit.attribute.clone();
        currentNode.setAttribute(attributeCopy);

        for (Branch branch : attributeCopy.getBranches()) {
            List<Instance> splitInstances = splitDataset(instancesRestantes, attributeCopy, branch.getValue());
            List<Attribute> remainingAttributes = attributesRestants.stream()
                    .filter(attr -> !attr.getAttributeName().equals(attributeCopy.getAttributeName()))
                    .map(Attribute::new)
                    .collect(Collectors.toList());

            Node childNode = new Node();
            branch.setChildNode(childNode);
            buildTree(childNode, remainingAttributes, splitInstances);
        }
    }

    private double calculateGiniIndex(List<Instance> instances) {
        int totalInstances = instances.size();
        Map<String, Integer> classCounts = new HashMap<>();

        for (Instance instance : instances) {
            classCounts.put(instance.getClassLabel(),
                    classCounts.getOrDefault(instance.getClassLabel(), 0) + 1);
        }

        double gini = 1.0;
        for (int count : classCounts.values()) {
            double probability = (double) count / totalInstances;
            gini -= probability * probability;
        }

        return gini;
    }

    private Split findBestSplit(List<Instance> instances, List<Attribute> attributes) {
        Split bestSplit = null;
        double bestGini = Double.MAX_VALUE;

        for (Attribute attribute : attributes) {
            for (Branch branch : attribute.getBranches()) {
                List<Instance> leftSplit = instances.stream()
                        .filter(instance -> instance.getSingleAttributeValue(attribute).equals(branch.getValue()))
                        .collect(Collectors.toList());
                List<Instance> rightSplit = instances.stream()
                        .filter(instance -> !instance.getSingleAttributeValue(attribute).equals(branch.getValue()))
                        .collect(Collectors.toList());

                double giniLeft = calculateGiniIndex(leftSplit);
                double giniRight = calculateGiniIndex(rightSplit);
                double weightedGini = ((double) leftSplit.size() / instances.size()) * giniLeft
                        + ((double) rightSplit.size() / instances.size()) * giniRight;

                if (weightedGini < bestGini) {
                    bestGini = weightedGini;
                    bestSplit = new Split(attribute, branch.getValue());
                }
            }
        }

        return bestSplit;
    }

    private List<Instance> splitDataset(List<Instance> instances, Attribute attribute, String value) {
        return instances.stream()
                .filter(instance -> instance.getSingleAttributeValue(attribute).equals(value))
                .collect(Collectors.toList());
    }

    private static class Split {
        private final Attribute attribute;
        private final String value;

        public Split(Attribute attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }
    }
}
