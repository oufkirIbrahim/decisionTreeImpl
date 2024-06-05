package com.fsoteam.ml.decisiontreeimpl.model;

public class ConfusionMatrix {

    private int classCount;
    private int[][] matrix;

    public ConfusionMatrix(int[][] matrix, int classCount) {
        this.matrix = matrix;
        this.classCount = classCount;
    }

    // Getters
    public int getClassCount() {
        return classCount;
    }

    public int[][] getMatrix() {
        return matrix;
    }
    public int getElement(int row, int column) {
        return matrix[row][column];
    }
}