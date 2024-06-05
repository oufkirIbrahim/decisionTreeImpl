package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.DecisionTree;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Node;
import com.fsoteam.ml.decisiontreeimpl.evaluation.EvaluationMetrics;
import com.fsoteam.ml.decisiontreeimpl.model.ConfusionMatrix;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.model.MemoryReboot;
import com.fsoteam.ml.decisiontreeimpl.model.TrainTest;
import com.fsoteam.ml.decisiontreeimpl.utils.ClassifierOutputHelper;
import com.fsoteam.ml.decisiontreeimpl.utils.DatasetInitializer;
import com.fsoteam.ml.decisiontreeimpl.utils.DetailedAccuracy;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class RunTestController {
    @FXML
    private Button startTestButton;
    @FXML
    private Button stopTestButton;
    @FXML
    private RadioButton testOptions_trainingSet, testOptions_crossValidation, testOptions_percentageSplit;
    @FXML
    private TextField crossValidationFoldEt, percentageSplitEt;
    @FXML
    private ListView<String> testHistoryListView;
    @FXML
    private TextArea fileContentArea;

    private SharedData sharedData;
    private List<Instance> predictedInstances;
    private Map<String, MemoryReboot> history;
    @FXML
    public void initialize() {

        this.sharedData = SharedData.getInstance();
        this.predictedInstances = new ArrayList<>();
        this.history = new HashMap<String, MemoryReboot>();

        // Add change listeners to radio buttons
        addRadioButtonListener(testOptions_trainingSet, crossValidationFoldEt, percentageSplitEt);
        addRadioButtonListener(testOptions_crossValidation, crossValidationFoldEt, percentageSplitEt);
        addRadioButtonListener(testOptions_percentageSplit, crossValidationFoldEt, percentageSplitEt);


        /*// Create an instance of DetailedAccuracy
        DetailedAccuracy detailedAccuracy1 = new DetailedAccuracy(0.8, 0.2, 0.75, 0.8, 0.77, 0.6, 0.85, 0.8, "ClassA");
        DetailedAccuracy detailedAccuracy2 = new DetailedAccuracy(0.8, 0.8, 0.75, 0.8, 0.77, 0.6, 0.85, 0.8, "ClassB");
        DetailedAccuracy detailedAccuracy3 = new DetailedAccuracy(0.8, 0.8, 0.75, 0.8, 0.77, 0.6, 0.85, 0.8, "ClassC");

        // Add the DetailedAccuracy instance to a list
        List<DetailedAccuracy> detailedAccuracyList = new ArrayList<>();
        detailedAccuracyList.add(detailedAccuracy1);
        detailedAccuracyList.add(detailedAccuracy2);
        detailedAccuracyList.add(detailedAccuracy3);

        // Create a confusion matrix
        int[][] confusionMatrix = {{5, 3, 0}, {0, 2, 7}, {0, 0, 8}};
        List<Attribute> attributes = new ArrayList<>();
        Attribute attr1 = new Attribute("Attr1", new ArrayList<>());
        Attribute attr2 = new Attribute("Attr2", new ArrayList<>());
        Attribute attr3 = new Attribute("Attr3", new ArrayList<>());
        attributes.add(attr1);
        attributes.add(attr2);
        attributes.add(attr3);

        // Create an instance of ClassifierOutputHelper
        ClassifierOutputHelper classifierOutputHelper = new ClassifierOutputHelper("SchemeA", "RelationA", 100, attributes, "TestModeA", "TestAlgorithmA", "2 seconds", "3 seconds", 80, 0.8, 20, 0.2, 0.6, 0.1, 0.3, 0.2, 0.4, 100, detailedAccuracyList, confusionMatrix);

        // Display the information
        String output = classifierOutputHelper.generateOutput();
        output = output.replaceAll("^\\s+", "\u00A0").replaceAll("\\s+$", "\u00A0");
        System.out.println(output);
        fileContentArea.setFont(new Font("Monospaced", 12));
        fileContentArea.setText(output);*/

        // Add a click listener to the start button
        startTestButton.setOnAction(event -> handleStartButtonClick());

        testHistoryListView.setOnMouseClicked(event -> handleHistoryReboot() );


    }

    private void handleHistoryReboot() {

        String selectedMemory = testHistoryListView.getSelectionModel().getSelectedItem();
        MemoryReboot memory = history.get(selectedMemory);
        sharedData.setTrainingData(memory.getTrainingData());
        sharedData.setTestingData(memory.getTestingData());
        sharedData.setTrainedModel(memory.getTrainedModel().clone());
        fileContentArea.setFont(Font.font("Monospaced", FontWeight.NORMAL, 12));
        fileContentArea.setText(memory.getMemoryOutput());
        fileContentArea.setWrapText(false);
        fileContentArea.setStyle("-fx-white-space: pre;");
    }
    private void addRadioButtonListener(RadioButton radioButton, TextField... textFields) {
        radioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                for (TextField textField : textFields) {
                    textField.setDisable(!newValue);
                }
            }
        });
    }
    private void handleStartButtonClick() {
        TrainTest trainTest;
        String testMode = "";
        if (testOptions_trainingSet.isSelected()) {
            // Option 1: The test corpus is equal to the train corpus
            testMode = "Using Training set";
            trainTest = new TrainTest(this.sharedData.getDatasetInitializer().getInstanceData(), this.sharedData.getDatasetInitializer().getInstanceData());
        } else if (testOptions_crossValidation.isSelected()) {
            // Option 2: Select K (Folds) from train set to test them
            int folds = Integer.parseInt(crossValidationFoldEt.getText());
            testMode = "Using Cross-Validation " + folds + " folds";
            trainTest = divideIntoKFolds(this.sharedData.getDatasetInitializer().getInstanceData(), folds);
        } else if (testOptions_percentageSplit.isSelected()) {
            // Option 3: Use percentage to divide the train set into train + test set
            double percentage = Double.parseDouble(percentageSplitEt.getText());
            testMode = "Using Percentage Split " + percentage + "%";
            trainTest = divideByPercentage(this.sharedData.getDatasetInitializer().getInstanceData(), percentage);
        } else {
            // No option selected
            return;
        }

        this.sharedData.setTrainingData(trainTest.getTrain());
        this.sharedData.setTestingData(trainTest.getTest());

        // Capture the start time before training
        long trainStartTime = System.currentTimeMillis();

        // Train the model using the training set
        this.sharedData.setTrainedModel(trainModel(trainTest.getTrain()));

        // Capture the end time after training
        long trainEndTime = System.currentTimeMillis();

        // Calculate the training duration
        long trainDuration = trainEndTime - trainStartTime;

        // Capture the start time before testing
        long testStartTime = System.currentTimeMillis();

        // Test the model using the testing set
        testModel(this.sharedData.getTrainedModel() , trainTest.getTest());

        // Capture the end time after testing
        long testEndTime = System.currentTimeMillis();

        // Calculate the testing duration
        long testDuration = testEndTime - testStartTime;

        String output = generateClassifierOutput(testMode, trainDuration, testDuration);
        String labelTitle = "DTree-test-"+ testMode.trim().replace(" ", "-") +"-"  + LocalTime.now().withNano(0);
        MemoryReboot memory = new MemoryReboot();
        memory.setTrainingData(sharedData.getTrainingData());
        memory.setTestingData(sharedData.getTestingData());
        memory.setTrainedModel(sharedData.getTrainedModel().clone());
        memory.setMemoryName(labelTitle);
        memory.setMemoryOutput(output);
        testHistoryListView.getItems().add(labelTitle);
        testHistoryListView.getSelectionModel().select(labelTitle);

        if(!history.containsKey(memory.getMemoryName())){
            history.put(memory.getMemoryName(), memory);
        }

        if(sharedData.getTrainedModel() != null) {
            System.out.println("Model trained successfully");
            try {
                HelloController.modelHasTrained();
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong while loading the modelHasTrained scene");
            }
        }
    }

    private TrainTest divideIntoKFolds(List<Instance> data, int testFoldSize) {

        // Divide the data into K folds
        if(data.size() < testFoldSize) throw new RuntimeException("can't have more folds than instances!");

        int trainFoldSize = data.size()  -  testFoldSize;
        List<Instance> train = new ArrayList<>();
        List<Instance> test = new ArrayList<>();

        Random random = new Random();
        int size = data.size();

        int trainIdx = 0, testIdx = 0;

        while(trainIdx < trainFoldSize) {
            int randIdx = random.nextInt(0, size);
            Instance tmp = data.get(randIdx);
            if(train.contains(tmp))
                continue;
            train.add(tmp);
            trainIdx++;
        }

        while(testIdx < testFoldSize) {
            int randIdx = random.nextInt(0, size);
            Instance tmp = data.get(randIdx);
            if(test.contains(tmp) && train.contains(tmp))
                continue;
            test.add(tmp);
            testIdx++;
        }

        return new TrainTest(train, test);
    }

    private TrainTest divideByPercentage(List<Instance> data, double percentage) {
        // Divide the data by percentage
        int trainSize = (int) (data.size() * percentage) / 100;
        List<Instance> train = new ArrayList<>();
        List<Instance> test = new ArrayList<>();
        int trainIdx, testIdx;
        trainIdx = testIdx = 0;
        int dataSize = data.size();
        Random random  = new Random();
        while(trainIdx < trainSize) {
            int randIdx = random.nextInt(0, dataSize);
            Instance tmp = data.get(randIdx);
            if(train.contains(tmp))
                continue;
            train.add(tmp);
            trainIdx++;
        }

        while (testIdx < dataSize - trainSize) {
            int randIdx = random.nextInt(0, dataSize);
            Instance tmp = data.get(randIdx);
            if(test.contains(tmp) && train.contains(tmp))
                continue;
            test.add(tmp);
            testIdx++;
        }

        return new TrainTest(train, test);
    }

    private DecisionTree trainModel(List<Instance> trainData) {
        // Train the model using the trainData
        DecisionTree model = new DecisionTree(new Node(), this.sharedData.getDatasetInitializer().getDecisionTreeClasses());
        model.id3( this.sharedData.getDatasetInitializer().getAttributes(), trainData);
        return model;
    }

    private void testModel(DecisionTree model, List<Instance> testData) {
        this.predictedInstances.clear();

        // Test the model using the testData
        for (Instance instance : testData) {
            String predictedClass = model.evaluateInstance(instance);
            this.predictedInstances.add(new Instance(instance.getInstanceId(), instance.getAttributeValues(), predictedClass));
        }
    }

    public String generateClassifierOutput(String testMode, long trainDuration, long testDuration) {
        // Get the trained model
        DecisionTree trainedModel = this.sharedData.getTrainedModel();

        // Get the training data
        List<Instance> trainingData = this.sharedData.getTrainingData();

        // Get the testing data
        List<Instance> testingData = this.sharedData.getTestingData();

        // Get the dataset initializer
        DatasetInitializer datasetInitializer = this.sharedData.getDatasetInitializer();

        // Generate the confusion matrix
        ConfusionMatrix confusionMatrix = new ConfusionMatrix(trainedModel.generateConfusionMatrix(trainingData), datasetInitializer.getDecisionTreeClasses().size());

        EvaluationMetrics evaluationMetrics = new EvaluationMetrics(confusionMatrix);
        // Calculate the metrics
        double accuracy = evaluationMetrics.calculateAccuracy();
        double errorRate = evaluationMetrics.calculateErrorRate();

        List<DetailedAccuracy> detailedAccuracyList = new ArrayList<DetailedAccuracy>();
        for (int i = 0; i < datasetInitializer.getDecisionTreeClasses().size(); i++) {
            DetailedAccuracy tmp = new DetailedAccuracy();
            tmp.setClassName(datasetInitializer.getDecisionTreeClasses().get(i).getClassName());
            tmp.setTpRate(evaluationMetrics.calculateTruePositiveRate(i));
            tmp.setFpRate(evaluationMetrics.calculateFalsePositiveRate(i));
            tmp.setPrecision(evaluationMetrics.calculateClassPrecision(i));
            tmp.setRecall(evaluationMetrics.calculateClassRecall(i));
            tmp.setfMeasure(evaluationMetrics.calculateClassFMeasure(i));
            tmp.setMcc(evaluationMetrics.calculateMatthewsCorrelationCoefficient(i));
            tmp.setRocArea(evaluationMetrics.calculateRocArea(i));
            tmp.setPrcArea(evaluationMetrics.calculatePrcArea(i));
            detailedAccuracyList.add(tmp);
        }
        // Create the ClassifierOutputHelper
        ClassifierOutputHelper outputHelper = new ClassifierOutputHelper();
        outputHelper.setScheme(DecisionTree.class.getName());
        outputHelper.setRelation(datasetInitializer.getDataSetSource());
        outputHelper.setDatasetSize(trainingData.size());
        outputHelper.setAttributes(datasetInitializer.getAttributes());
        outputHelper.setTestMode(testMode);
        outputHelper.setTestAlgorithm(DecisionTree.class.getSimpleName());
        outputHelper.setBuildTime(String.valueOf(trainDuration / 1000.0));
        outputHelper.setTestTime(String.valueOf(testDuration / 1000.0));
        outputHelper.setCorrect((int) (accuracy * trainingData.size()));
        outputHelper.setCorrectPercentage(accuracy * 100);
        outputHelper.setIncorrect((int) (errorRate * trainingData.size()));
        outputHelper.setIncorrectPercentage(errorRate * 100);
        outputHelper.setKappaStatistic(evaluationMetrics.calculateKappa());

        /* Cannot Calculate in case of strings before conversion to numbers/Mappings */
        outputHelper.setMeanAbsoluteError(0);
        outputHelper.setRootMeanSquaredError(0);
        outputHelper.setRelativeAbsoluteError(0);
        outputHelper.setRootRelativeSquaredError(0);

        outputHelper.setTotalNumberOfInstances(trainingData.size());
        outputHelper.setDetailedAccuracyByClass(detailedAccuracyList);
        outputHelper.setConfusionMatrixValues(confusionMatrix.getMatrix());

        // Display the information
        String output = outputHelper.generateOutput();
        output = output.replaceAll("^\\s+", "\u00A0").replaceAll("\\s+$", "\u00A0");
        System.out.println(output);
        fileContentArea.setFont(Font.font("Monospaced", FontWeight.NORMAL, 12));
        fileContentArea.setText(output);
        fileContentArea.setWrapText(false);
        fileContentArea.setStyle("-fx-white-space: pre;");
        return output;
    }
}