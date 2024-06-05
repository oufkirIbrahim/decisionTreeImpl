package com.fsoteam.ml.decisiontreeimpl.utils;

public class DetailedAccuracy {


    private double tpRate;
    private double fpRate;
    private double precision;
    private double recall;
    private double fMeasure;
    private double mcc;
    private double rocArea;
    private double prcArea;
    private String className;

    public DetailedAccuracy() {
        this.setClassName("");
        this.setTpRate(0);
        this.setFpRate(0);
        this.setPrecision(0);
        this.setRecall(0);
        this.setfMeasure(0);
        this.setMcc(0);
        this.setRocArea(0);
        this.setPrcArea(0);
    }

    public DetailedAccuracy(double tpRate, double fpRate, double precision, double recall, double fMeasure, double mcc, double rocArea, double prcArea, String className) {
        this.tpRate = tpRate;
        this.fpRate = fpRate;
        this.precision = precision;
        this.recall = recall;
        this.fMeasure = fMeasure;
        this.mcc = mcc;
        this.rocArea = rocArea;
        this.prcArea = prcArea;
        this.className = className;
    }

    public double getTpRate() {
        return tpRate;
    }

    public void setTpRate(double tpRate) {
        this.tpRate = tpRate;
    }

    public double getFpRate() {
        return fpRate;
    }

    public void setFpRate(double fpRate) {
        this.fpRate = fpRate;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    public void setfMeasure(double fMeasure) {
        this.fMeasure = fMeasure;
    }

    public double getMcc() {
        return mcc;
    }

    public void setMcc(double mcc) {
        this.mcc = mcc;
    }

    public double getRocArea() {
        return rocArea;
    }

    public void setRocArea(double rocArea) {
        this.rocArea = rocArea;
    }

    public double getPrcArea() {
        return prcArea;
    }

    public void setPrcArea(double prcArea) {
        this.prcArea = prcArea;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
