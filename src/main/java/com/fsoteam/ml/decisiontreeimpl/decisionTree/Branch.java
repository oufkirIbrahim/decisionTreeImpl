package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a branch in a decision tree.
 * It implements Cloneable interface to create a copy of the branch object.
 */
public class Branch implements Cloneable {

    // The value or name of the branch
    private String value;

    // Node object used to link an internal node or a leaf to a Branch
    // during the decision tree creation process
    private Node childNode;

    // Array of instances where this branch appears in the dataset
    private int[] instanceIds = new int[0];

    // Array of counts of instances where this branch appears in each existing class
    private int[] instanceCountsInClasses = new int[0];

    // Total count of instances for this branch
    private int totalInstanceCount = 0;

    // Constructor with value and instanceIds parameters
    public Branch(String value, int[] instanceIds) {
        this.setValue(value);
        this.setInstanceIds(instanceIds);
    }

    // Constructor with value parameter
    public Branch(String value) {
        this.setValue(value);
    }

    // Getter for value
    public String getValue() {
        return value;
    }

    // Setter for value
    public void setValue(String value) {
        this.value = value;
    }

    // Getter for instanceIds
    public int[] getInstanceIds() {
        return instanceIds;
    }

    // Setter for instanceIds
    public void setInstanceIds(int[] instanceIds) {
        this.instanceIds = instanceIds;
    }

    // Getter for totalInstanceCount
    public int getTotalInstanceCount() {
        return totalInstanceCount;
    }

    // Setter for totalInstanceCount
    public void setTotalInstanceCount(int totalInstanceCount) {
        this.totalInstanceCount = totalInstanceCount;
    }

    // Getter for childNode
    public Node getChildNode() {
        return childNode;
    }

    // Setter for childNode
    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    // Getter for instanceCountsInClasses
    public int[] getInstanceCountsInClasses() {
        return instanceCountsInClasses;
    }

    // Setter for instanceCountsInClasses
    public void setInstanceCountsInClasses(int[] instanceCountsInClasses) {
        this.instanceCountsInClasses = instanceCountsInClasses;
    }

    /**
     * Creates and returns a copy of this object.
     * @return a clone of this instance.
     */
    @Override
    public Branch clone() {
        return clone(new HashMap<>());
    }

    // Method to clone the branch with a map of already cloned objects
    public Branch clone(Map<Object, Object> clonesMap) {
        if (clonesMap.containsKey(this)) {
            return (Branch) clonesMap.get(this);
        }

        try {
            Branch cloned = (Branch) super.clone();
            clonesMap.put(this, cloned);

            // Clone the childNode if it is not null
            cloned.childNode = this.childNode != null ? this.childNode.clone(clonesMap) : null;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // Method to reset the count of instances and classes
    public void resetCount(int count){
        this.totalInstanceCount = 0;
        this.instanceIds = new int[0];
        this.instanceCountsInClasses = new int[count];
    }

    // Method to increment the total instance count
    public void incrementTotalInstanceCount() {
        this.totalInstanceCount++;
    }

    // Method to get the class name of the child node
    public String getClassName() {
        return childNode.getMajorClass();
    }

    // Method to increment the instance count in a specific class
    public void incrementInstanceCountInClass(int i) {
        this.instanceCountsInClasses[i]++;
    }

    // Method to add an instance ID to the array of instance IDs
    public void addInstanceId(int instanceId) {
        int[] newIds = new int[instanceIds.length + 1];
        System.arraycopy(instanceIds, 0, newIds, 0, instanceIds.length);
        newIds[instanceIds.length] = instanceId;
        instanceIds = newIds;
    }
}