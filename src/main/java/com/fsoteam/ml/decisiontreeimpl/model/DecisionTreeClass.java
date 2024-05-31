package com.fsoteam.ml.decisiontreeimpl.model;

public class DecisionTreeClass {
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
}
