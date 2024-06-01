package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.utils.CustomFileReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class UploadDataController {

    @FXML
    private TextField filePathField;

    @FXML
    private Button chooseFileButton;

    @FXML
    private TableView<Instance> dataTable;

    private ObservableList<Instance> instanceData;
    private List<Attribute> attributes;

    @FXML
    private void initialize() {
        instanceData = FXCollections.observableArrayList();
        dataTable.setItems(instanceData);

        // File chooser button action
        chooseFileButton.setOnAction(event -> handleChooseFile());
    }

    @FXML
    private void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ARFF files", "*.arff"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            loadDataFromFile(selectedFile);
        }
    }

    private void loadDataFromFile(File file) {
        CustomFileReader fileReader = new CustomFileReader(file.getAbsolutePath());
        instanceData.clear();
        instanceData.addAll(fileReader.dataSet);
        // Clear previous columns except for ID
        dataTable.getColumns().remove(0, dataTable.getColumns().size());


        // ID Column
        TableColumn<Instance, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(cellData.getValue()::getInstanceId));
        dataTable.getColumns().add(idColumn);

        // Use the attributes to create columns
        attributes = fileReader.attributs;
        for (int i = 0; i < attributes.size() - 1; i++) {
            Attribute attribute = attributes.get(i);
            TableColumn<Instance, String> attrColumn = new TableColumn<>(attribute.getAttributeName());
            int finalI = i;
            attrColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> {
                List<String> values = cellData.getValue().getAttributeValues();
                return finalI < values.size() ? values.get(finalI) : "";
            }));
            dataTable.getColumns().add(attrColumn);
        }

        // Add class label column
        TableColumn<Instance, String> classLabelColumn = new TableColumn<>(attributes.get(attributes.size() - 1).getAttributeName()) ;
        classLabelColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(cellData.getValue()::getClassLabel));
        dataTable.getColumns().add(classLabelColumn);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("subScenes/runTest.fxml"));
        RunTestController controller = fxmlLoader.getController();
        controller.setAttributes(attributes);
        controller.setInstanceData(instanceData);
    }
}
