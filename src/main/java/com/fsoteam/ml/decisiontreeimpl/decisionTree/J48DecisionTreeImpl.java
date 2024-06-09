package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 *  This Class has not been implemented yet. Once it is implemented correctly it will be added to the Run Test Interface
 *  It is a placeholder for the J48 Decision Tree algorithm.
 *  The J48 Decision Tree algorithm is a popular decision tree algorithm that is used in data mining and machine learning.
 * */
public class J48DecisionTreeImpl extends DecisionTree {

    public J48DecisionTreeImpl() {
        super();
    }

    public J48DecisionTreeImpl(Node root, List<DecisionTreeClass> classes, List<Attribute> attributes) {
        super(root, classes, attributes);
    }

    @Override
    public void train(List<Instance> instances) {
        buildTreeJ48(super.getRoot(), super.getAttributes(), instances);
    }

    private void buildTreeJ48(Node currentNode, List<Attribute> remainingAttributes, List<Instance> remainingInstances) {
        calculateNp(remainingInstances, remainingAttributes);

        double maxGainRatio = 0;
        Attribute bestAttribute = null;

        for (Attribute a : remainingAttributes) {
            double gainRatio = calculateGainRatio(a);
            if (gainRatio > maxGainRatio) {
                maxGainRatio = gainRatio;
                bestAttribute = a;
            }
        }

        if (bestAttribute == null) {
            currentNode.setLeaf(true);
            currentNode.setMajorClass(calculateMajorityClass(remainingInstances));
            return;
        }

        Attribute attributeCopy = bestAttribute.clone();
        currentNode.setAttribute(attributeCopy);

        attributeCopy.getBranches().forEach(branch -> {
            List<Instance> partitionedInstances = remainingInstances.stream()
                    .filter(instance -> Arrays.stream(branch.getInstanceIds())
                            .anyMatch(idInstance -> idInstance == instance.getInstanceId()))
                    .collect(Collectors.toList());

            List<Attribute> remainingAttributesForChild = remainingAttributes.stream()
                    .filter(att -> !att.getAttributeName().equals(attributeCopy.getAttributeName()))
                    .collect(Collectors.toList());

            Node childNode = new Node();
            branch.setChildNode(childNode);

            buildTreeJ48(childNode, remainingAttributesForChild, partitionedInstances);
        });
    }

    private double calculateGainRatio(Attribute att) {
        double gain = calculateGain(att);
        double splitInformation = calculateSplitInformation(att);
        return splitInformation != 0 ? gain / splitInformation : 0;
    }

    private double calculateSplitInformation(Attribute att) {
        double total = super.getClasses().stream()
                .mapToInt(DecisionTreeClass::getAppearanceCount)
                .sum();

        return att.getBranches().stream()
                .mapToDouble(branch -> {
                    double ratio = (double) branch.getTotalInstanceCount() / total;
                    return -ratio * log2(ratio);
                })
                .sum();
    }
}
