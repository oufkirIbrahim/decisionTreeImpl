package com.fsoteam.ml.decisiontreeimpl.evaluation;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.*;
import com.fsoteam.ml.decisiontreeimpl.model.*;
import com.fsoteam.ml.decisiontreeimpl.utils.CustomFileReader;

import java.io.*;
import java.util.*;

public class MainTest {
    protected static List<DecisionTreeClass> decisionTreeClasses = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String fileName = "weather.nominal.arff";

        Scanner scanner = new Scanner(System.in);
        List<Instance> datasets = new ArrayList<>();
        List<Attribute> attributes = new ArrayList<>();
        CustomFileReader fileReader = new CustomFileReader(fileName);
        DecisionTree decisionTree;
        Node rootNode = new Node();

        datasets = fileReader.getDataSet();
        attributes = fileReader.getAttributs();

        // Initialize decision tree classes based on the last attribute's branches
        int classIndex = 1;
        for (Branch branch : attributes.get(attributes.size() - 1).getBranches()) {
            decisionTreeClasses.add(new DecisionTreeClass(classIndex, branch.getValue(), 0));
            classIndex++;
        }
        int numberOfClasses = decisionTreeClasses.size();

        attributes.remove(attributes.size() - 1);
        System.out.println("Dataset size: " + datasets.size());
        System.out.println("Attributes size: " + attributes.size());
        System.out.println("Classes size: " + decisionTreeClasses.size());
        DataSet corpus = new DataSet(datasets);

        decisionTree = new Id3DecisionTreeImpl(rootNode, decisionTreeClasses, attributes);

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline left by nextInt()
            switch (choice) {
                case 1:
                    useTrainingDatasetForTesting(decisionTree, datasets, numberOfClasses);
                    break;
                case 2:
                    splitDataForTrainingAndTesting(scanner, corpus, decisionTree, numberOfClasses);
                    break;
                case 3:
                    crossValidationTesting(scanner, corpus, decisionTree, numberOfClasses);
                    break;
                case 4:
                    predictInstanceAttributes(scanner, decisionTree, attributes, datasets);
                    break;
                case 5:
                    System.out.println("Exiting the program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n==============================");
        System.out.println("       Decision Tree Menu      ");
        System.out.println("==============================");
        System.out.println("1 - Use the training dataset for testing");
        System.out.println("2 - Split a part for training and a part for testing");
        System.out.println("3 - Use cross-validation for testing");
        System.out.println("4 - Enter the attribute values of an instance to predict its class");
        System.out.println("5 - Exit");
        System.out.print("Enter your choice: ");
    }

    private static void useTrainingDatasetForTesting(DecisionTree decisionTree, List<Instance> datasets, int numberOfClasses) {
        System.out.println("You chose to use the training dataset for testing");
        decisionTree.train(datasets);
        displayConfusionMatrix(decisionTree.generateConfusionMatrix(datasets), numberOfClasses);
        decisionTree.displayTreeString(decisionTree.getRoot(), "   ");
    }

    private static void splitDataForTrainingAndTesting(Scanner scanner, DataSet corpus, DecisionTree decisionTree, int numberOfClasses) {
        System.out.println("You chose to split the data for training and testing");
        System.out.print("Enter the percentage for testing (integer between 1-99): ");
        int percentage = scanner.nextInt();
        scanner.nextLine(); // Consume the newline left by nextInt()
        double k = (double) percentage * corpus.getDataSet().size() / 100;
        TrainTest splitData = corpus.trainTest(k);
        List<Instance> trainData = splitData.getTrain();
        List<Instance> testData = splitData.getTest();
        System.out.println("Training set size: " + trainData.size());
        System.out.println("Testing set size: " + testData.size());
        decisionTree.train(trainData);
        displayConfusionMatrix(decisionTree.generateConfusionMatrix(testData), numberOfClasses);
        decisionTree.displayTreeString(decisionTree.getRoot(), "   ");
    }

    private static void crossValidationTesting(Scanner scanner, DataSet corpus, DecisionTree decisionTree, int numberOfClasses) {
        System.out.println("You chose to use cross-validation for testing");
        System.out.print("Enter the number of folds for cross-validation: ");
        int numberOfFolds = scanner.nextInt();
        scanner.nextLine(); // Consume the newline left by nextInt()
        if(numberOfFolds <= 1 || numberOfFolds > corpus.getDataSet().size()) {
            System.out.println("Invalid number of folds. Please try again.");
            return;
        }
        List<TrainTest> crossValidationSets = corpus.crossValidation(numberOfFolds);
        int foldNumber = 1;
        List<ConfusionMatrix> confusionMatrices = new ArrayList<>();
        for (TrainTest fold : crossValidationSets) {
            System.out.println("Fold " + foldNumber + "\n------------------------------------");
            List<Instance> trainFold = fold.getTrain();
            List<Instance> testFold = fold.getTest();
            System.out.println("Training set size: " + trainFold.size());
            System.out.println("Testing set size: " + testFold.size());
            foldNumber++;
            decisionTree.train(trainFold);
            confusionMatrices.add(new ConfusionMatrix(decisionTree.generateConfusionMatrix(testFold), numberOfClasses));
            decisionTree.displayTreeString(decisionTree.getRoot(), "   ");
        }
        displayAggregatedConfusionMatrix(confusionMatrices, numberOfClasses);
    }

    private static void predictInstanceAttributes(Scanner scanner, DecisionTree decisionTree, List<Attribute> attributes, List<Instance> datasets) {
        System.out.println("You chose to predict the class of an instance based on attribute values");
        System.out.println("Example data for weather nominal: sunny, cool, normal, FALSE");
        System.out.println("Example data for contact-lenses: young, myope, yes, normal");

        decisionTree.train(datasets);
        List<String> attributeValues = new ArrayList<>();
        for (int i = 0; i < attributes.size(); i++) {
            System.out.print("Enter the value for " + attributes.get(i).getAttributeName() + ": ");
            attributeValues.add(scanner.nextLine());
        }
        Instance instance = new Instance(99, attributeValues);
        System.out.println("Predicted class: " + decisionTree.evaluate(instance));
    }

    private static void displayConfusionMatrix(int[][] matrix, int numberOfClasses) {
        System.out.println("\nConfusion Matrix:");
        System.out.println("=================");
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    private static void displayAggregatedConfusionMatrix(List<ConfusionMatrix> confusionMatrices, int numberOfClasses) {
        int[][] aggregatedMatrix = new int[numberOfClasses][numberOfClasses];
        for (ConfusionMatrix cm : confusionMatrices) {
            int[][] matrix = cm.getMatrix();
            for (int i = 0; i < numberOfClasses; i++) {
                for (int j = 0; j < numberOfClasses; j++) {
                    aggregatedMatrix[i][j] += matrix[i][j];
                }
            }
        }
        displayConfusionMatrix(aggregatedMatrix, numberOfClasses);
    }
}
