package com.fsoteam.ml.decisiontreeimpl.utils;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import java.util.List;

public class ClassifierOutputHelper {

    // === Run information ===
    private String scheme;
    private String relation;
    private int datasetSize;
    private List<Attribute> attributes;
    private String testMode;
    private String testAlgorithm;

    // === Evaluation on training set ===
    private String buildTime;
    private String testTime;
    // === Summary ===

    private int correct;
    private double correctPercentage;

    private int incorrect;
    private double incorrectPercentage;
    private double kappaStatistic;
    private double meanAbsoluteError;
    private double rootMeanSquaredError;
    private double relativeAbsoluteError;
    private double rootRelativeSquaredError;
    private int totalNumberOfInstances;

    // === Detailed Accuracy By Class ===
    List<DetailedAccuracy> detailedAccuracyByClass;

    // === Confusion Matrix ===
    private int[][] confusionMatrixValues;

    public ClassifierOutputHelper() {
    }

    public ClassifierOutputHelper(String scheme, String relation, int datasetSize, List<Attribute> attributes, String testMode, String testAlgorithm, String buildTime,String testTime,  int correct, double correctPercentage, int incorrect, double incorrectPercentage, double kappaStatistic, double meanAbsoluteError, double rootMeanSquaredError, double relativeAbsoluteError, double rootRelativeSquaredError, int totalNumberOfInstances, List<DetailedAccuracy> detailedAccuracyByClass, int[][] confusionMatrixValues) {
        this.scheme = scheme;
        this.relation = relation;
        this.datasetSize = datasetSize;
        this.attributes = attributes;
        this.testMode = testMode;
        this.testAlgorithm = testAlgorithm;
        this.buildTime = buildTime;
        this.testTime = testTime;
        this.correct = correct;
        this.correctPercentage = correctPercentage;
        this.incorrect = incorrect;
        this.incorrectPercentage = incorrectPercentage;
        this.kappaStatistic = kappaStatistic;
        this.meanAbsoluteError = meanAbsoluteError;
        this.rootMeanSquaredError = rootMeanSquaredError;
        this.relativeAbsoluteError = relativeAbsoluteError;
        this.rootRelativeSquaredError = rootRelativeSquaredError;
        this.totalNumberOfInstances = totalNumberOfInstances;
        this.detailedAccuracyByClass = detailedAccuracyByClass;
        this.confusionMatrixValues = confusionMatrixValues;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getDatasetSize() {
        return datasetSize;
    }

    public void setDatasetSize(int datasetSize) {
        this.datasetSize = datasetSize;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    public String getTestAlgorithm() {
        return testAlgorithm;
    }

    public void setTestAlgorithm(String testAlgorithm) {
        this.testAlgorithm = testAlgorithm;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public double getCorrectPercentage() {
        return correctPercentage;
    }

    public void setCorrectPercentage(double correctPercentage) {
        this.correctPercentage = correctPercentage;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public double getIncorrectPercentage() {
        return incorrectPercentage;
    }

    public void setIncorrectPercentage(double incorrectPercentage) {
        this.incorrectPercentage = incorrectPercentage;
    }

    public double getKappaStatistic() {
        return kappaStatistic;
    }

    public void setKappaStatistic(double kappaStatistic) {
        this.kappaStatistic = kappaStatistic;
    }

    public double getMeanAbsoluteError() {
        return meanAbsoluteError;
    }

    public void setMeanAbsoluteError(double meanAbsoluteError) {
        this.meanAbsoluteError = meanAbsoluteError;
    }

    public double getRootMeanSquaredError() {
        return rootMeanSquaredError;
    }

    public void setRootMeanSquaredError(double rootMeanSquaredError) {
        this.rootMeanSquaredError = rootMeanSquaredError;
    }

    public double getRelativeAbsoluteError() {
        return relativeAbsoluteError;
    }

    public void setRelativeAbsoluteError(double relativeAbsoluteError) {
        this.relativeAbsoluteError = relativeAbsoluteError;
    }

    public double getRootRelativeSquaredError() {
        return rootRelativeSquaredError;
    }

    public void setRootRelativeSquaredError(double rootRelativeSquaredError) {
        this.rootRelativeSquaredError = rootRelativeSquaredError;
    }

    public int getTotalNumberOfInstances() {
        return totalNumberOfInstances;
    }

    public void setTotalNumberOfInstances(int totalNumberOfInstances) {
        this.totalNumberOfInstances = totalNumberOfInstances;
    }

    public List<DetailedAccuracy> getDetailedAccuracyByClass() {
        return detailedAccuracyByClass;
    }

    public void setDetailedAccuracyByClass(List<DetailedAccuracy> detailedAccuracyByClass) {
        this.detailedAccuracyByClass = detailedAccuracyByClass;
    }

    public int[][] getConfusionMatrixValues() {
        return confusionMatrixValues;
    }

    public void setConfusionMatrixValues(int[][] confusionMatrixValues) {
        this.confusionMatrixValues = confusionMatrixValues;
    }

    public String generateOutput() {

        String output = String.format(
                "=== Run information ===\n\n" +
                        "Scheme:       %s\n" +
                        "Relation:     %s\n" +
                        "Instances:    %d\n" +
                        "Attributes:   %d\n" +
                        "%s\n" +
                        "Test mode:    %s\n\n" +
                        "=== Classifier model (full training set) ===\n\n" +
                        "Time taken to build model: %s seconds\n\n" +
                        "=== Evaluation on training set ===\n\n" +
                        "Time taken to test model on training data: %s seconds\n\n" +
                        "=== Summary ===\n\n" +
                        "Correctly Classified Instances             %d\n              %f\n" +
                        "Incorrectly Classified Instance            %d\n              %f\n" +
                        "Kappa statistic                            %f\n" +
                        "Mean absolute error                        %f\n" +
                        "Root mean squared error                    %f\n" +
                        "Relative absolute error                    %f     \n" +
                        "Root relative squared error                %f \n" +
                        "Total Number of Instances                  %d\n\n" +
                        "=== Detailed Accuracy By Class ===\n\n" +
                        "%s\n\n" +
                        "=== Confusion Matrix ===\n\n" +
                        "%s\n",
                scheme, relation, datasetSize, attributes.size(), getAttributesFormatted(),
                testMode,
                buildTime, testTime, correct, correctPercentage, incorrect, incorrectPercentage,
                kappaStatistic, meanAbsoluteError, rootMeanSquaredError, relativeAbsoluteError, rootRelativeSquaredError,
                totalNumberOfInstances, getDetailedAccuracyByClassFormatted(), getConfusionMatrixValuesFormatted()
        );

        return output;
    }

    public String getAttributesFormatted(){
        StringBuilder output = new StringBuilder();
        for (Attribute attribute : attributes) {
            output.append(String.format("              %s\n", attribute.getAttributeName()));
        }
        return output.toString();
    }

    public String getDetailedAccuracyByClassFormatted() {

        StringBuilder output = new StringBuilder("Tp Rate   Fp Rate   Precision   Recall   F-Measure   MCC   ROC Area   PRC Area     Class\n\n");

        double avgTpRate, avgFpRate, avgPrecision, avgRecall, avgFMeasure, avgMcc, avgRocArea, avgPrcArea;
        avgTpRate = avgFpRate = avgPrecision = avgRecall = avgFMeasure = avgMcc = avgRocArea = avgPrcArea = 0;

        for (DetailedAccuracy detailedAccuracy : detailedAccuracyByClass) {
            output.append(String.format(
                    "%f   %f   %f   %f   %f   %f   %f   %f     %s\n",
                    detailedAccuracy.getTpRate(), detailedAccuracy.getFpRate(), detailedAccuracy.getPrecision(), detailedAccuracy.getRecall(), detailedAccuracy.getfMeasure(), detailedAccuracy.getMcc(), detailedAccuracy.getRocArea(), detailedAccuracy.getPrcArea(), detailedAccuracy.getClassName()
            ));

            avgTpRate += detailedAccuracy.getTpRate();
            avgFpRate += detailedAccuracy.getFpRate();
            avgPrecision += detailedAccuracy.getPrecision();
            avgRecall += detailedAccuracy.getRecall();
            avgFMeasure += detailedAccuracy.getfMeasure();
            avgMcc += detailedAccuracy.getMcc();
            avgRocArea += detailedAccuracy.getRocArea();
            avgPrcArea += detailedAccuracy.getPrcArea();
        }

        avgTpRate /= detailedAccuracyByClass.size();
        avgFpRate /= detailedAccuracyByClass.size();
        avgPrecision /= detailedAccuracyByClass.size();
        avgRecall /= detailedAccuracyByClass.size();
        avgFMeasure /= detailedAccuracyByClass.size();
        avgMcc /= detailedAccuracyByClass.size();
        avgRocArea /= detailedAccuracyByClass.size();
        avgPrcArea /= detailedAccuracyByClass.size();
        output.append(String.format(
                "%f   %f   %f   %f   %f   %f   %f   %f     %s\n",
                avgTpRate, avgFpRate, avgPrecision, avgRecall, avgFMeasure, avgMcc, avgRocArea, avgPrcArea, "AVG"
        ));

        return output.toString();
    }

    public String getConfusionMatrixValuesFormatted() {
        StringBuilder output = new StringBuilder();
        for (int[] row : confusionMatrixValues) {
            for (int value : row) {
                output.append(value).append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }

}

