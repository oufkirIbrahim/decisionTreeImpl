package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;

import java.util.HashMap;
import java.util.Map;

// Class representing a node in the decision tree
public class Node implements Cloneable{

    // Attribute of the node
    private Attribute attribute;
    // Boolean flag indicating if the node is a leaf node
    private boolean isLeaf;
    // The major class of the node, used when the node is a leaf node
    private String majorClass;

    // Default constructor
    public Node() {
    }

    // Getter for attribute
    public Attribute getAttribute() {
        return attribute;
    }

    // Setter for attribute
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    // Method to check if the node is a leaf node
    public boolean isLeaf() {
        return isLeaf;
    }

    // Setter for isLeaf
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    // Getter for majorClass
    public String getMajorClass() {
        return majorClass;
    }

    // Setter for majorClass
    public void setMajorClass(String majorClass) {
        this.majorClass = majorClass;
    }

    // Method to get the attribute name of the node
    public String getAttributeName(){
        return attribute.getAttributeName();
    }

    // Method to clone the node
    @Override
    public Node clone() {
        return clone(new HashMap<>());
    }

    // Method to clone the node with a map of already cloned objects
    public Node clone(Map<Object, Object> clonesMap) {
        if (clonesMap.containsKey(this)) {
            return (Node) clonesMap.get(this);
        }

        try {
            Node cloned = (Node) super.clone();
            clonesMap.put(this, cloned);

            // Clone the attribute if it is not null
            if (this.attribute != null) {
                cloned.attribute = this.attribute.clone(clonesMap);
            }

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}