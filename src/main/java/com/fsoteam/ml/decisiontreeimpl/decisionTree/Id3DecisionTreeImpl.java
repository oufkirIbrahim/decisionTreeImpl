package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Class implementing the ID3 decision tree algorithm
public class Id3DecisionTreeImpl extends DecisionTree {

    // Default constructor
    public Id3DecisionTreeImpl() {
        super();
    }

    // Constructor with parameters
    public Id3DecisionTreeImpl(Node root, List<DecisionTreeClass> classes, List<Attribute> attributes) {
        super(root, classes, attributes);
    }

    // Method to train the decision tree with a list of instances
    @Override
    public void train(List<Instance> instances){
        buildTree(super.getRoot(), super.getAttributes(), instances);
    }

    // Recursive method to build the decision tree
    private void buildTree(Node currentNode, List<Attribute> attributesRestants, List<Instance> instancesRestantes) {
        // Calculate the number of appearances of each class and branch
        calculateNp(instancesRestantes, attributesRestants);

        double maxGain = 0;
        Attribute meilleurAttribute = null;

        // Find the attribute with the maximum gain
        for (Attribute a : attributesRestants) {
            double gain = calculateGain(a);
            if (gain > maxGain) {
                maxGain = gain;
                meilleurAttribute = a;
            }
        }

        // If no attribute has a positive gain, make the current node a leaf node
        if (meilleurAttribute == null) {
            currentNode.setLeaf(true);
            currentNode.setMajorClass(calculateMajorityClass(instancesRestantes));
            return;
        }

        // Clone the best attribute and set it as the attribute of the current node
        Attribute attributeCopie = meilleurAttribute.clone();
        currentNode.setAttribute(attributeCopie);

        // For each branch of the best attribute, create a child node and build the subtree
        attributeCopie.getBranches().forEach(branche -> {
            // Partition the instances based on the branch
            List<Instance> instancesPartitionnees = instancesRestantes.stream()
                    .filter(instance -> Arrays.stream(branche.getInstanceIds())
                            .anyMatch(idInstanceBranche -> idInstanceBranche == instance.getInstanceId()))
                    .map(Instance::new) // Create a new Instance object
                    .collect(Collectors.toList());

            // Create a list of remaining attributes excluding the best attribute
            List<Attribute> attributesRestantsFils = attributesRestants.stream()
                    .filter(att -> !att.getAttributeName().equals(attributeCopie.getAttributeName()))
                    .map(Attribute::new) // Create a new Attribute object
                    .collect(Collectors.toList());

            // Create a new child node and set it as the child of the branch
            Node noeudFils = new Node();
            branche.setChildNode(noeudFils);

            // Build the subtree
            buildTree(noeudFils, attributesRestantsFils, instancesPartitionnees);
        });
    }
}