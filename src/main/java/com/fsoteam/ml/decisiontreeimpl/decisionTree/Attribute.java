package com.fsoteam.ml.decisiontreeimpl.decisionTree;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an attribute in the decision tree.
 * Each attribute has a name and a list of branches.
 */
public class Attribute implements Cloneable {

    private final String attributeName;
    private List<Branch> branches;

    public Attribute(String attributeName, List<Branch> branches) {
        this.attributeName = attributeName;
        this.branches = new ArrayList<>(branches);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    @Override
    public Attribute clone() {
        try {
            Attribute cloned = (Attribute) super.clone();
            cloned.setBranches(new ArrayList<>(this.branches));
            return cloned;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
