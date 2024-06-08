package com.fsoteam.ml.decisiontreeimpl.decisionTree;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Attribute(Attribute other) {
        this.attributeName = other.attributeName;
        this.branches = other.branches.stream()
                .map(Branch::clone)
                .collect(Collectors.toList());
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
        return clone(new HashMap<>());
    }

    public Attribute clone(Map<Object, Object> clonesMap) {
        if (clonesMap.containsKey(this)) {
            return (Attribute) clonesMap.get(this);
        }

        try {
            Attribute cloned = (Attribute) super.clone();
            clonesMap.put(this, cloned);

            List<Branch> clonedBranches = new ArrayList<>();
            for (Branch branch : this.branches) {
                clonedBranches.add(branch.clone(clonesMap));
            }
            cloned.setBranches(clonedBranches);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
