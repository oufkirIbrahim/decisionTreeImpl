package com.fsoteam.ml.decisiontreeimpl.evaluation;

public class ConfusionMatrix {

    private int classCount;
    private int[][] elements;
    private double errorRate;
    private double accuracy;
    private double precision;
    private double recall;
    private double[] classPrecision;
    private double[] classRecall;
    private double[] classFMeasure;

    public ConfusionMatrix(int[][] matrix, int classCount) {
        this.elements = matrix;
        this.classCount = classCount;
        this.classPrecision = new double[classCount];
        this.classRecall = new double[classCount];
        this.classFMeasure = new double[classCount];
    }

    public double calculateClassPrecision(int classIndex) {
        int elementCount = 0;
        for (int i = 0; i < classCount; i++) {
            elementCount += elements[i][classIndex];
        }
        double precision = elementCount != 0 ? (double) elements[classIndex][classIndex] / elementCount : 0;
        this.classPrecision[classIndex] = precision;
        return precision;
    }

    public double calculateClassRecall(int classIndex) {
        int elementCount = 0;
        for (int i = 0; i < classCount; i++) {
            elementCount += elements[classIndex][i];
        }
        double recall = elementCount != 0 ? (double) elements[classIndex][classIndex] / elementCount : 0;
        this.classRecall[classIndex] = recall;
        return recall;
    }

    public double calculateClassFMeasure(int classIndex) {
        double precision = this.classPrecision[classIndex];
        double recall = this.classRecall[classIndex];
        this.classFMeasure[classIndex] = (precision + recall) != 0 ? 2 * precision * recall / (precision + recall) : 0;
        return this.classFMeasure[classIndex];
    }

    public double calculateErrorRate() {
        int errorCount = 0, totalCount = 0;
        for (int i = 0; i < classCount; i++) {
            for (int j = 0; j < classCount; j++) {
                if (i != j) {
                    errorCount += elements[i][j];
                }
                totalCount += elements[i][j];
            }
        }
        this.errorRate = totalCount != 0 ? (double) errorCount / totalCount : 0;
        return this.errorRate;
    }

    public double calculateAccuracy() {
        int correctCount = 0, totalCount = 0;
        for (int i = 0; i < classCount; i++) {
            for (int j = 0; j < classCount; j++) {
                if (i == j) {
                    correctCount += elements[i][j];
                }
                totalCount += elements[i][j];
            }
        }
        this.accuracy = totalCount != 0 ? (double) correctCount / totalCount : 0;
        return this.accuracy;
    }

    public double calculatePrecision() {
        double precisionSum = 0;
        for (double precision : classPrecision) {
            precisionSum += precision;
        }
        this.precision = classCount != 0 ? precisionSum / classCount : 0;
        return this.precision;
    }

    public double calculateRecall() {
        double recallSum = 0;
        for (double recall : classRecall) {
            recallSum += recall;
        }
        this.recall = classCount != 0 ? recallSum / classCount : 0;
        return this.recall;
    }

    public double calculateFMeasure() {
        return (this.precision + this.recall) != 0 ? 2 * (this.precision * this.recall) / (this.precision + this.recall) : 0;
    }

    // Getters
    public int getClassCount() {
        return classCount;
    }

    public int[][] getElements() {
        return elements;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double[] getClassPrecision() {
        return classPrecision;
    }

    public double[] getClassRecall() {
        return classRecall;
    }

    public double[] getClassFMeasure() {
        return classFMeasure;
    }
}