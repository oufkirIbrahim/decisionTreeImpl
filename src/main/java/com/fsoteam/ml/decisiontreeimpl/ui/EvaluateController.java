package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Branch;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.DecisionTree;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Node;
import com.fsoteam.ml.decisiontreeimpl.model.DataSet;
import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.utils.CustomFileReader;
import com.fsoteam.ml.decisiontreeimpl.utils.TrainedModelObserver;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class EvaluateController implements TrainedModelObserver {

    private SharedData sharedData;
    @FXML
    private VBox vboxInputs;
    @FXML
    private TableView<Instance> resultTable;

    @FXML
    private Button evaluateButton;
    private List<ChoiceBox> inputFields = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();

    @FXML
    public void initialize() {
        this.sharedData = SharedData.getInstance();
        this.sharedData.addObserver(this);
        evaluate();

    }
    @Override
    public void onTrainedModelChanged() {
        evaluate();
    }
    private void evaluate() {

        attributes = this.sharedData.getDatasetInitializer().getAttributes();

        //Set up the input fields based on attributes
        inputFields.clear();
        vboxInputs.getChildren().clear();
        for (int i = 0; i < attributes.size(); i++) {
            Label label = new Label(attributes.get(i).getAttributeName());
            ChoiceBox<String> choiceBox = new ChoiceBox<>();
            inputFields.add(choiceBox);


            // Populate the ChoiceBox with the branches of the attribute
            for (Branch branch : attributes.get(i).getBranches()) {
                choiceBox.getItems().add(branch.getValue());
            }

            label.setLayoutX(10);
            label.setLayoutY(40 + (i * 40));
            choiceBox.setLayoutX(10);
            choiceBox.setLayoutY(60 + (i * 40));
            choiceBox.setMinWidth(180);
            choiceBox.setMaxWidth(180);

            vboxInputs.getChildren().add(label);
            vboxInputs.getChildren().add(choiceBox);
        }

        // Add the evaluate button
        evaluateButton.setLayoutX(50);
        evaluateButton.setLayoutY(60 + (attributes.size() * 40));
        vboxInputs.getChildren().add(evaluateButton);


        // Setup table columns for attributes
        resultTable.getColumns().clear();
        for (int j = 0; j < attributes.size(); j++) {
            TableColumn<Instance, String> column = new TableColumn<>(attributes.get(j).getAttributeName());
            final int index = j;
            column.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() ->
                    cellData.getValue().getAttributeValues().get(index)));
            resultTable.getColumns().add(column);
            column.setMinWidth(120);
        }
        TableColumn<Instance, String> classLabelColumn = new TableColumn<>(this.sharedData.getDatasetInitializer().getClassName());
        classLabelColumn.setCellValueFactory(new PropertyValueFactory<>("classLabel"));
        resultTable.getColumns().add(classLabelColumn);
    }

    @FXML
    private void handleEvaluate() {

        List<String> attributeValues = new ArrayList<>();
        for (ChoiceBox choiceBox : inputFields) {
            attributeValues.add((String) choiceBox.getValue());
        }

        Random rand  = new Random();
        Instance instance = new Instance(rand.nextInt(Integer.MAX_VALUE), attributeValues);
        String result = this.sharedData.getTrainedModel().evaluateInstance(instance);
        instance.setClassLabel(result);
        resultTable.getItems().add(instance);
    }
}
