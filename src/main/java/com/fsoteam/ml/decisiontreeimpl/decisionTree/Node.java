package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;

import java.util.HashMap;
import java.util.Map;

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
        return clone(new HashMap<>());
    }

    public Node clone(Map<Object, Object> clonesMap) {
        if (clonesMap.containsKey(this)) {
            return (Node) clonesMap.get(this);
        }

        try {
            Node cloned = (Node) super.clone();
            clonesMap.put(this, cloned);

            if (this.attribute != null) {
                cloned.attribute = this.attribute.clone(clonesMap);
            }

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}