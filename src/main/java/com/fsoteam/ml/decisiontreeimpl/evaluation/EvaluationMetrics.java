package com.fsoteam.ml.decisiontreeimpl.evaluation;

import com.fsoteam.ml.decisiontreeimpl.model.ConfusionMatrix;

import java.util.Arrays;

public class EvaluationMetrics {

    private ConfusionMatrix confusionMatrix;
    private double errorRate;
    private double accuracy;
    private double precision;
    private double recall;
    private double[] classPrecision;
    private double[] classRecall;
    private double[] classFMeasure;
    private int totalNumberOfInstances;

    public EvaluationMetrics(ConfusionMatrix confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
        int classCount = confusionMatrix.getClassCount();
        this.classPrecision = new double[classCount];
        this.classRecall = new double[classCount];
        this.classFMeasure = new double[classCount];
        this.totalNumberOfInstances = Arrays.stream(confusionMatrix.getMatrix())
                .flatMapToInt(Arrays::stream)
                .sum();
    }

    public double calculateClassPrecision(int classIndex) {
        int elementCount = 0;
        for (int i = 0; i < confusionMatrix.getClassCount(); i++) {
            elementCount += confusionMatrix.getElement(i, classIndex);
        }
        double precision = elementCount != 0 ? (double) confusionMatrix.getElement(classIndex, classIndex) / elementCount : 0;
        this.classPrecision[classIndex] = precision;
        return precision;
    }

    public double calculateClassRecall(int classIndex) {
        int elementCount = 0;
        for (int i = 0; i < confusionMatrix.getClassCount(); i++) {
            elementCount += confusionMatrix.getElement(classIndex, i);
        }
        double recall = elementCount != 0 ? (double) confusionMatrix.getElement(classIndex, classIndex) / elementCount : 0;
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
        for (int i = 0; i < confusionMatrix.getClassCount(); i++) {
            for (int j = 0; j < confusionMatrix.getClassCount(); j++) {
                if (i != j) {
                    errorCount += confusionMatrix.getElement(i, j);
                }
                totalCount += confusionMatrix.getElement(i, j);
            }
        }
        this.errorRate = totalCount != 0 ? (double) errorCount / totalCount : 0;
        return this.errorRate;
    }

    public double calculateAccuracy() {
        int correctCount = 0, totalCount = 0;
        for (int i = 0; i < confusionMatrix.getClassCount(); i++) {
            for (int j = 0; j < confusionMatrix.getClassCount(); j++) {
                if (i == j) {
                    correctCount += confusionMatrix.getElement(i, j);
                }
                totalCount += confusionMatrix.getElement(i, j);
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
        this.precision = confusionMatrix.getClassCount() != 0 ? precisionSum / confusionMatrix.getClassCount() : 0;
        return this.precision;
    }

    public double calculateRecall() {
        double recallSum = 0;
        for (double recall : classRecall) {
            recallSum += recall;
        }
        this.recall = confusionMatrix.getClassCount() != 0 ? recallSum / confusionMatrix.getClassCount() : 0;
        return this.recall;
    }

    public double calculateFMeasure() {
        return (this.precision + this.recall) != 0 ? 2 * (this.precision * this.recall) / (this.precision + this.recall) : 0;
    }

    private double calculateExpectedAccuracy() {
        double[] rowSums = new double[confusionMatrix.getClassCount()];
        double[] colSums = new double[confusionMatrix.getClassCount()];

        for (int i = 0; i < confusionMatrix.getClassCount(); i++) {
            for (int j = 0; j < confusionMatrix.getClassCount(); j++) {
                rowSums[i] += confusionMatrix.getElement(i, j);
                colSums[j] += confusionMatrix.getElement(i, j);
            }
        }

        double expectedAccuracy = 0;
        for (int i = 0; i < confusionMatrix.getClassCount(); i++) {
            expectedAccuracy += rowSums[i] * colSums[i];
        }

        return totalNumberOfInstances != 0 ? expectedAccuracy / (totalNumberOfInstances * totalNumberOfInstances) : 0;
    }

    public double calculateTruePositiveRate(int classIndex) {
        return calculateClassRecall(classIndex);
    }

    public double calculateFalsePositiveRate(int classIndex) {
        int falsePositiveCount = 0;
        int trueNegativeCount = 0;
        for (int i = 0; i < confusionMatrix.getClassCount(); i++) {
            if (i != classIndex) {
                falsePositiveCount += confusionMatrix.getElement(i, classIndex);
                trueNegativeCount += confusionMatrix.getElement(i, i);
            }
        }
        return (falsePositiveCount + trueNegativeCount) != 0 ? (double) falsePositiveCount / (falsePositiveCount + trueNegativeCount) : 0;
    }

    public double calculateMatthewsCorrelationCoefficient(int classIndex) {
        double tp = confusionMatrix.getElement(classIndex, classIndex);
        double tn = 0, fp = 0, fn = 0;

        for (int i = 0; i < confusionMatrix.getClassCount(); i++) {
            for (int j = 0; j < confusionMatrix.getClassCount(); j++) {
                if (i != classIndex && j != classIndex) {
                    tn += confusionMatrix.getElement(i, j);
                } else if (i != classIndex && j == classIndex) {
                    fp += confusionMatrix.getElement(i, j);
                } else if (i == classIndex && j != classIndex) {
                    fn += confusionMatrix.getElement(i, j);
                }
            }
        }

        return (tp * tn - fp * fn) / Math.sqrt((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn));
    }

    public double calculateRocArea(int classIndex) {
        double[] tpr = new double[confusionMatrix.getClassCount()];
        double[] fpr = new double[confusionMatrix.getClassCount()];

        tpr[classIndex] = calculateTruePositiveRate(classIndex);
        fpr[classIndex] = calculateFalsePositiveRate(classIndex);

        Arrays.sort(fpr);
        Arrays.sort(tpr);

        double rocArea = 0;
        for (int i = 1; i < fpr.length; i++) {
            rocArea += (fpr[i] - fpr[i - 1]) * (tpr[i] + tpr[i - 1]) / 2;
        }
        return rocArea;
    }

    public double calculatePrcArea(int classIndex) {
        double[] precision = new double[confusionMatrix.getClassCount()];
        double[] recall = new double[confusionMatrix.getClassCount()];

        precision[classIndex] = calculateClassPrecision(classIndex);
        recall[classIndex] = calculateClassRecall(classIndex);

        Arrays.sort(recall);
        Arrays.sort(precision);

        double prcArea = 0;
        for (int i = 1; i < recall.length; i++) {
            prcArea += (recall[i] - recall[i - 1]) * (precision[i] + precision[i - 1]) / 2;
        }
        return prcArea;
    }

    public double calculateKappa() {
        int[][] matrix = confusionMatrix.getMatrix();
        int total = totalNumberOfInstances;

        double sumDiagonal = 0.0;
        for (int i = 0; i < matrix.length; i++) {
            sumDiagonal += matrix[i][i];
        }
        double Po = sumDiagonal / total;

        double sumRowCol = 0.0;
        for (int i = 0; i < matrix.length; i++) {
            int sumRow = 0;
            int sumCol = 0;
            for (int j = 0; j < matrix.length; j++) {
                sumRow += matrix[i][j];
                sumCol += matrix[j][i];
            }
            sumRowCol += sumRow * sumCol;
        }
        double Pe = sumRowCol / (total * total);

        return (Po - Pe) / (1 - Pe);
    }
    /**
     * The metrics below must be Mapped to integers in case of strings.
     * */
    public double calculateMeanAbsoluteError(int[] predictedValues, int[] actualValues) {
        double sumAbsoluteErrors = 0.0;
        for (int i = 0; i < predictedValues.length; i++) {
            sumAbsoluteErrors += Math.abs(predictedValues[i] - actualValues[i]);
        }
        return predictedValues.length != 0 ? sumAbsoluteErrors / predictedValues.length : 0;
    }

    public double calculateRootMeanSquaredError(int[] predictedValues, int[] actualValues) {
        double sumSquaredErrors = 0.0;
        for (int i = 0; i < predictedValues.length; i++) {
            sumSquaredErrors += Math.pow(predictedValues[i] - actualValues[i], 2);
        }
        return predictedValues.length != 0 ? Math.sqrt(sumSquaredErrors / predictedValues.length) : 0;
    }

    public double calculateRelativeAbsoluteError(int[] predictedValues, int[] actualValues) {
        double sumAbsoluteErrors = 0.0;
        double meanActualValues = Arrays.stream(actualValues).average().orElse(0);

        double sumAbsoluteDifferencesFromMean = 0.0;
        for (int i = 0; i < actualValues.length; i++) {
            sumAbsoluteErrors += Math.abs(predictedValues[i] - actualValues[i]);
            sumAbsoluteDifferencesFromMean += Math.abs(actualValues[i] - meanActualValues);
        }

        return sumAbsoluteDifferencesFromMean != 0 ? sumAbsoluteErrors / sumAbsoluteDifferencesFromMean : 0;
    }

    public double calculateRootRelativeSquaredError(int[] predictedValues, int[] actualValues) {
        double sumSquaredErrors = 0.0;
        double meanActualValues = Arrays.stream(actualValues).average().orElse(0);

        double sumSquaredDifferencesFromMean = 0.0;
        for (int i = 0; i < actualValues.length; i++) {
            sumSquaredErrors += Math.pow(predictedValues[i] - actualValues[i], 2);
            sumSquaredDifferencesFromMean += Math.pow(actualValues[i] - meanActualValues, 2);
        }

        return sumSquaredDifferencesFromMean != 0 ? Math.sqrt(sumSquaredErrors / sumSquaredDifferencesFromMean) : 0;
    }


    // Getters
    public ConfusionMatrix getConfusionMatrix() {
        return confusionMatrix;
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
