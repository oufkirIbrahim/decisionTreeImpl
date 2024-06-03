package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Branch;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.DecisionTree;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Node;
import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.model.TrainTest;
import com.fsoteam.ml.decisiontreeimpl.utils.ClassifierOutputHelper;
import com.fsoteam.ml.decisiontreeimpl.utils.CustomFileReader;
import com.fsoteam.ml.decisiontreeimpl.utils.DatasetInitializer;
import com.fsoteam.ml.decisiontreeimpl.utils.DetailedAccuracy;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

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

    private DatasetInitializer datasetInitializer;

    @FXML
    public void initialize() {

        this.sharedData = SharedData.getInstance();
        this.datasetInitializer = new DatasetInitializer();
        //this.datasetInitializer = sharedData.getDatasetInitializer();

        // Add change listeners to radio buttons
        addRadioButtonListener(testOptions_trainingSet, crossValidationFoldEt, percentageSplitEt);
        addRadioButtonListener(testOptions_crossValidation, crossValidationFoldEt, percentageSplitEt);
        addRadioButtonListener(testOptions_percentageSplit, crossValidationFoldEt, percentageSplitEt);

        populateDataset();

        // Populate testHistoryListView
        for (int i = 0; i < 20; i++) {
            testHistoryListView.getItems().add("DTree-id3-test-" + LocalTime.now().withNano(0));
        }

        // Create an instance of DetailedAccuracy
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
        fileContentArea.setText(output);

        // Add a click listener to the start button
        startTestButton.setOnAction(event -> handleStartButtonClick());


    }

    public void populateDataset(){

        String fileName = "contact-lenses.arff";

        List<Attribute> attributes = new ArrayList<Attribute>();
        List<DecisionTreeClass> decisionTreeClasses = new ArrayList<DecisionTreeClass>();

        CustomFileReader file = new CustomFileReader(fileName);



        attributes = file.getAttributs();

        int i=1;
        for(Branch b:attributes.get(attributes.size() - 1).getBranches()){
            decisionTreeClasses.add( new DecisionTreeClass(i,b.getValue(),0));
            i++;
        }
        attributes.remove(attributes.size() - 1);

        this.datasetInitializer.setDataSetSource(fileName);
        this.datasetInitializer = new DatasetInitializer();
        this.datasetInitializer.setAttributes(attributes);
        this.datasetInitializer.setInstanceData(file.getDataSet());
        this.datasetInitializer.setDecisionTreeClasses(decisionTreeClasses);

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
        if (testOptions_trainingSet.isSelected()) {
            // Option 1: The test corpus is equal to the train corpus
            trainTest = new TrainTest(datasetInitializer.getInstanceData(), datasetInitializer.getInstanceData());
        } else if (testOptions_crossValidation.isSelected()) {
            // Option 2: Select K (Folds) from train set to test them
            // You need to implement the method divideIntoKFolds
            trainTest = divideIntoKFolds(datasetInitializer.getInstanceData(), Integer.parseInt(crossValidationFoldEt.getText()));
        } else if (testOptions_percentageSplit.isSelected()) {
            // Option 3: Use percentage to divide the train set into train + test set
            // You need to implement the method divideByPercentage
            trainTest = divideByPercentage(datasetInitializer.getInstanceData(), Double.parseDouble(percentageSplitEt.getText()));
        } else {
            // No option selected
            return;
        }

        // Train the model using the training set
        this.sharedData.setTrainingData(trainTest.getTrain());
        this.sharedData.setTrainedModel(trainModel(trainTest.getTrain()));

        // Test the model using the testing set
        testModel(this.sharedData.getTrainedModel() , trainTest.getTest());
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
            System.out.println("loop1: " + randIdx);
            Instance tmp = data.get(randIdx);
            if(train.contains(tmp))
                continue;
            train.add(tmp);
            trainIdx++;
        }

        while(testIdx < testFoldSize) {
            int randIdx = random.nextInt(0, size);
            System.out.println("loop2: " + randIdx);
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
        DecisionTree model = new DecisionTree(new Node(), datasetInitializer.getDecisionTreeClasses());
        model.id3( datasetInitializer.getAttributes(), trainData);
        return model;
    }

    private void testModel(DecisionTree model, List<Instance> testData) {
        // Test the model using the testData
        for (Instance instance : testData) {
            String predictedClass = model.evaluateInstance(instance);
            // Compare the predicted class with the actual class of the instance
            // and update your metrics accordingly
        }
    }
}