package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Id3DecisionTreeImpl extends DecisionTree {

    public Id3DecisionTreeImpl() {
        super();
    }

    public Id3DecisionTreeImpl(Node root, List<DecisionTreeClass> classes, List<Attribute> attributes) {
        super(root, classes, attributes);
    }
    @Override
    public void train(List<Instance> instances){
        buildTree(super.getRoot(), super.getAttributes(), instances);
    }
    private void buildTree(Node currentNode, List<Attribute> attributesRestants, List<Instance> instancesRestantes) {
        calculateNp(instancesRestantes, attributesRestants);

        double maxGain = 0;
        Attribute meilleurAttribute = null;

        for (Attribute a : attributesRestants) {
            double gain = calculateGain(a);
            if (gain > maxGain) {
                maxGain = gain;
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

            buildTree(noeudFils, attributesRestantsFils, instancesPartitionnees);
        });
    }
}
