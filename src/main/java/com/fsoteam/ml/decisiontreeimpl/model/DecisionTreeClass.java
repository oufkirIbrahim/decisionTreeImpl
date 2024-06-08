package com.fsoteam.ml.decisiontreeimpl.model;

public class DecisionTreeClass implements Cloneable {
    private int classId;
    private String className;
    private int appearanceCount;

    public DecisionTreeClass(int classId, String className, int appearanceCount) {
        this.classId = classId;
        this.className = className;
        this.appearanceCount = appearanceCount;
    }

    public int getClassId() {
        return this.classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAppearanceCount() {
        return this.appearanceCount;
    }

    public void setAppearanceCount(int appearanceCount) {
        this.appearanceCount = appearanceCount;
    }

    public void incrementAppearanceCount() {
        this.appearanceCount++;
    }

    public void  resetCount() {
        this.appearanceCount = 0;
    }

    @Override
    public DecisionTreeClass clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (DecisionTreeClass) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
