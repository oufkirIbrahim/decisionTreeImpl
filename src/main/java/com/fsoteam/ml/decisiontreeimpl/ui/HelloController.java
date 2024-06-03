package com.fsoteam.ml.decisiontreeimpl.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    @FXML
    public AnchorPane sceneContainer;

    private Map<String, Node> scenes = new HashMap<>();

    @FXML
    public void initialize() throws IOException {
        loadAllScenes();
    }

    public void loadAllScenes() throws IOException {
        scenes.put("uploadData", FXMLLoader.load(getClass().getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/uploadData.fxml")));
        scenes.put("runTest", FXMLLoader.load(getClass().getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/runTest.fxml")));
        scenes.put("evaluate", FXMLLoader.load(getClass().getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/evaluate.fxml")));
        scenes.put("viewData", FXMLLoader.load(getClass().getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/viewData.fxml")));
        scenes.put("welcomeInterface", FXMLLoader.load(getClass().getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/welcome-interface.fxml")));
        // initially hide all scenes
        for(Node scene : scenes.values()) {
            scene.setVisible(false);
            sceneContainer.getChildren().add(scene);
        }
        showScene("welcomeInterface");
    }

    public void showScene(String sceneKey) {
        // hide all scenes
        for(Node scene : scenes.values()) {
            scene.setVisible(false);
        }

        // show selected scene
        scenes.get(sceneKey).setVisible(true);
    }

    @FXML
    public void loadUploadDataScene() {
        showScene("uploadData");
    }

    @FXML
    public void loadRunTestScene() {
        showScene("runTest");
    }

    @FXML
    public void loadEvaluateScene() {
        showScene("evaluate");
    }

    @FXML
    public void loadViewDataScene() {
        showScene("viewData");
    }
}