package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Branch;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.DecisionTree;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Node;
import com.fsoteam.ml.decisiontreeimpl.model.DataSet;
import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.utils.CustomFileReader;
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


public class EvaluateController {

    private SharedData sharedData;
    @FXML
    private VBox vboxInputs;
    @FXML
    private TableView<Instance> resultTable;

    @FXML
    private Button evaluateButton;
    private List<TextField> inputFields = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();

    @FXML
    public void initialize() {
        sharedData = SharedData.getInstance();
        loadDataFromFile();

    }

    private void loadDataFromFile() {

        attributes = this.sharedData.getDatasetInitializer().getAttributes();

        //Set up the input fields based on attributes
        inputFields.clear();
        vboxInputs.getChildren().clear();
        for (int i = 0; i < attributes.size(); i++) {
            Label label = new Label(attributes.get(i).getAttributeName());
            TextField textField = new TextField();
            inputFields.add(textField);

            label.setLayoutX(10);
            label.setLayoutY(40 + (i * 40));
            textField.setLayoutX(10);
            textField.setLayoutY(60 + (i * 40));

            vboxInputs.getChildren().add(label);
            vboxInputs.getChildren().add(textField);
        }

        // Add the evaluate button
        evaluateButton.setLayoutX(50);
        evaluateButton.setLayoutY(60 + (attributes.size() * 40));
        vboxInputs.getChildren().add(evaluateButton);



        //-----------------------------------


        //----------------------------------
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
        for (TextField field : inputFields) {
            attributeValues.add(field.getText());
        }

        Random rand  = new Random();
        Instance instance = new Instance(rand.nextInt(Integer.MAX_VALUE), attributeValues);
        String result = this.sharedData.getTrainedModel().evaluateInstance(instance);
        instance.setClassLabel(result);
        resultTable.getItems().add(instance);
    }
}
