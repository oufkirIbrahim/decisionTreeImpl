package com.fsoteam.ml.decisiontreeimpl.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HelloController {
    @FXML
    public AnchorPane sceneContainer;

    public void loadScene(String fxml) throws IOException {
        Node newScene = FXMLLoader.load(getClass().getResource("/" + fxml));
        sceneContainer.getChildren().setAll(newScene);
    }

    @FXML
    public void loadUploadDataScene() throws IOException {
        loadScene("com/fsoteam/ml/decisiontreeimpl/subScenes/uploadData.fxml");
    }

    @FXML
    public void loadRunTestScene() throws IOException {
        loadScene("com/fsoteam/ml/decisiontreeimpl/subScenes/runTest.fxml");
    }

    @FXML
    public void loadEvaluateScene() throws IOException {
        loadScene("com/fsoteam/ml/decisiontreeimpl/subScenes/evaluate.fxml");
    }

    @FXML
    public void loadViewDataScene() throws IOException {
        loadScene("com/fsoteam/ml/decisiontreeimpl/subScenes/viewData.fxml");
    }
}