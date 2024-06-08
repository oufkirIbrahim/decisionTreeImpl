package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class J48DecisionTreeImpl extends DecisionTree {


    public J48DecisionTreeImpl() {
        super();
    }

    public J48DecisionTreeImpl(Node root, List<DecisionTreeClass> classes, List<Attribute> attributes) {
        super(root, classes, attributes);
    }

    @Override
    public void train(List<Instance> instances){
        buildTreeJ48(super.getRoot(), super.getAttributes(), instances);
    }

    private void buildTreeJ48(Node currentNode, List<Attribute> attributesRestants, List<Instance> instancesRestantes) {
        calculateNp(instancesRestantes, attributesRestants);

        double maxGainRatio = 0;
        Attribute meilleurAttribute = null;

        for (Attribute a : attributesRestants) {
            double gainRatio = calculateGainRatio(a);
            if (gainRatio > maxGainRatio) {
                maxGainRatio = gainRatio;
                meilleurAttribute = a;
            }
        }

        if (meilleurAttribute == null) {
            currentNode.setLeaf(true);
            currentNode.setMajorClass(calculateMajorityClass(instancesRestantes));
            return;
        }

        Attribute attributeCopie = meilleurAttribute.clone();
        currentNode.setAttribute(attributeCopie);

        attributeCopie.getBranches().forEach(branche -> {
            List<Instance> instancesPartitionnees = instancesRestantes.stream()
                    .filter(instance -> Arrays.stream(branche.getInstanceIds())
                            .anyMatch(idInstanceBranche -> idInstanceBranche == instance.getInstanceId()))
                    .map(Instance::new) // Create a new Instance object
                    .collect(Collectors.toList());

            List<Attribute> attributesRestantsFils = attributesRestants.stream()
                    .filter(att -> !att.getAttributeName().equals(attributeCopie.getAttributeName()))
                    .map(Attribute::new) // Create a new Attribute object
                    .collect(Collectors.toList());

            Node noeudFils = new Node();
            branche.setChildNode(noeudFils);

            buildTreeJ48(noeudFils, attributesRestantsFils, instancesPartitionnees);
        });
    }

    private double calculateGainRatio(Attribute att) {
        double gain = calculateGain(att);
        double splitInformation = calculateSplitInformation(att);
        return splitInformation != 0 ? gain / splitInformation : 0;
    }

    private double calculateSplitInformation(Attribute att) {
        double total = 0;
        for (DecisionTreeClass arb : super.getClasses()) {
            total += arb.getAppearanceCount();
        }

        double splitInformation = 0;
        for (Branch b : att.getBranches()) {
            double ratio = (double) b.getTotalInstanceCount() / (double) (total);
            splitInformation += -1 * ratio * log2(ratio);
        }
        return splitInformation;
    }
}
