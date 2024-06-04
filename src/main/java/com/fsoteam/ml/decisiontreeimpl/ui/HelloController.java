package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.utils.DatasetInitializer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    @FXML
    private static AnchorPane sceneContainer;

    private static Map<String, Node> scenes = new HashMap<>();
    public AnchorPane containerPane;
    private SharedData sharedData;

    @FXML
    public void initialize() throws IOException {
        sharedData = SharedData.getInstance();
        sharedData.setDatasetInitializer(new DatasetInitializer());
        sceneContainer = new AnchorPane();
        AnchorPane.setLeftAnchor(sceneContainer, 223.0);
        AnchorPane.setRightAnchor(sceneContainer, 0.0);
        AnchorPane.setTopAnchor(sceneContainer, 0.0);
        AnchorPane.setBottomAnchor(sceneContainer, 0.0);
        containerPane.getChildren().add(sceneContainer);

        loadAllScenes();
    }
    public static void dataHasLoaded() throws IOException {
        if(scenes.containsKey("runTest")) return;
        scenes.put("runTest", FXMLLoader.load(HelloController.class.getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/runTest.fxml")));
        scenes.get("runTest").setVisible(false);
        sceneContainer.getChildren().add(scenes.get("runTest"));
    }

    public static void modelHasTrained() throws IOException {
        if(scenes.containsKey("evaluate") || scenes.containsKey("viewData")) return;
        scenes.put("evaluate", FXMLLoader.load(HelloController.class.getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/evaluate.fxml")));
        scenes.put("viewData", FXMLLoader.load(HelloController.class.getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/viewData.fxml")));
        scenes.get("evaluate").setVisible(false);
        scenes.get("viewData").setVisible(false);
        sceneContainer.getChildren().add(scenes.get("evaluate"));
        sceneContainer.getChildren().add(scenes.get("viewData"));
    }



    public void loadAllScenes() throws IOException {
        scenes.put("uploadData", FXMLLoader.load(getClass().getResource("/com/fsoteam/ml/decisiontreeimpl/subScenes/uploadData.fxml")));
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

        System.out.println("_______________Showing scene: " + sceneKey);
        System.out.println("Scenes size: " + scenes.size());
        System.out.println("Scene container size: " + sceneContainer.getChildren().size());

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