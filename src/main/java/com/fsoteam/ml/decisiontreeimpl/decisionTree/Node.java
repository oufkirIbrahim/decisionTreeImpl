package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;

public class Node implements Cloneable{

    private Attribute attribute;
    private boolean isLeaf;
    private String majorClass;

    public Node() {
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getMajorClass() {
        return majorClass;
    }

    public void setMajorClass(String majorClass) {
        this.majorClass = majorClass;
    }

    public String getAttributeName(){
        return attribute.getAttributeName();
    }

    @Override
    public Node clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}