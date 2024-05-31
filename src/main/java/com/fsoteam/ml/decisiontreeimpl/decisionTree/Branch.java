package com.fsoteam.ml.decisiontreeimpl.decisionTree;


/**
 * This class represents a branch in a decision tree.
 * It implements Cloneable interface to create a copy of the branch object.
 */
public class Branch implements Cloneable {

    // The value or name of the branch
    private String value;

    // Object of type NoeudDecision used to link an internal node or a leaf to a Branch
    // In the step of creating the decision tree
    private Node childNode;

    // Arrays of instances where this branch repeats in the dataset
    private int[] instanceIds = new int[0];

    // Arrays of Numbers of instances where this branch repeats in each existing class
    private int[] instanceCountsInClasses = new int[0];

    private int totalInstanceCount = 0;

    public Branch(String value, int[] instanceIds) {
        this.setValue(value);
        this.setInstanceIds(instanceIds);
    }

    public Branch(String value) {
        this.setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int[] getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(int[] instanceIds) {
        this.instanceIds = instanceIds;
    }

    public int getTotalInstanceCount() {
        return totalInstanceCount;
    }

    public void setTotalInstanceCount(int totalInstanceCount) {
        this.totalInstanceCount = totalInstanceCount;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    public int[] getInstanceCountsInClasses() {
        return instanceCountsInClasses;
    }

    public void setInstanceCountsInClasses(int[] instanceCountsInClasses) {
        this.instanceCountsInClasses = instanceCountsInClasses;
    }

    /**
     * Creates and returns a copy of this object.
     * @return a clone of this instance.
     */
    @Override
    public Branch clone() {
        try {
            return (Branch) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen as we implement Cloneable
        }
    }

    public void resetCount(int count){
        this.totalInstanceCount = 0;
        this.instanceIds = new int[0];
        this.instanceCountsInClasses = new int[count];
    }
}