package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Attribute;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.Branch;
import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.utils.CustomFileReader;
import com.fsoteam.ml.decisiontreeimpl.utils.DatasetInitializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private SharedData sharedData;

    @FXML
    private void initialize() {

        this.sharedData = SharedData.getInstance();

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

        this.populateDataset(file.getAbsolutePath());
        this.instanceData.clear();
        this.instanceData.addAll(this.sharedData.getDatasetInitializer().getInstanceData());
        String classLabel = this.sharedData.getDatasetInitializer().getClassName();
        System.out.println("Class label: " + classLabel);
        // Clear previous columns except for ID
        dataTable.getColumns().remove(0, dataTable.getColumns().size());


        // ID Column
        TableColumn<Instance, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(cellData.getValue()::getInstanceId));
        dataTable.getColumns().add(idColumn);

        // Use the attributes to create columns
        attributes = sharedData.getDatasetInitializer().getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
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
        TableColumn<Instance, String> classLabelColumn = new TableColumn<>(classLabel) ;
        classLabelColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(cellData.getValue()::getClassLabel));
        dataTable.getColumns().add(classLabelColumn);
    }

    private void populateDataset(String fileName){

        DatasetInitializer datasetInitializer = new DatasetInitializer();


        List<Attribute> attributes = new ArrayList<Attribute>();
        List<DecisionTreeClass> decisionTreeClasses = new ArrayList<DecisionTreeClass>();

        CustomFileReader file = new CustomFileReader(fileName);



        attributes = file.getAttributs();

        int i=1;
        for(Branch b:attributes.get(attributes.size() - 1).getBranches()){
            decisionTreeClasses.add( new DecisionTreeClass(i,b.getValue(),0));
            i++;
        }

        datasetInitializer.setDataSetSource(fileName);
        datasetInitializer.setAttributes(attributes);
        datasetInitializer.setClassName(attributes.remove(attributes.size() - 1).getAttributeName());
        datasetInitializer.setInstanceData(file.getDataSet());
        datasetInitializer.setDecisionTreeClasses(decisionTreeClasses);

        this.sharedData.setDatasetInitializer(datasetInitializer);
        if(sharedData.getDatasetInitializer() != null) {
            try {
                HelloController.dataHasLoaded();
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong while loading the scenes after data has been loaded");
            }
        }
    }
}
