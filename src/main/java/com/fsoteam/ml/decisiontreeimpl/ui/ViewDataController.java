package com.fsoteam.ml.decisiontreeimpl.ui;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.Node;
import com.fsoteam.ml.decisiontreeimpl.decisionTree.DecisionTree;
import com.fsoteam.ml.decisiontreeimpl.utils.TrainedModelObserver;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ViewDataController implements TrainedModelObserver {

    private SharedData sharedData;
    @FXML
    private AnchorPane canvasContainer;

    @FXML
    private ScrollPane scrollPane;

    private DecisionTree decisionTree;

    @FXML
    public void initialize() {
        this.sharedData = SharedData.getInstance();
        this.sharedData.addObserver(this);
        drawTree();
    }

    @Override
    public void onTrainedModelChanged() {
        drawTree();
    }

    private void drawTree() {
        decisionTree = this.sharedData.getTrainedModel();
        if (decisionTree != null) {
            Node root = decisionTree.getRoot();

            Canvas canvas = new Canvas();
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // Calculate the bounds of the tree
            double[] bounds = calculateBounds(root, 0, 0, 1000, 200);
            double canvasWidth = Math.max(bounds[1] - bounds[0], scrollPane.getWidth());
            double canvasHeight = Math.max(bounds[3] - bounds[2], scrollPane.getHeight());

            canvas.setWidth(canvasWidth);
            canvas.setHeight(canvasHeight);

            double startX = canvasWidth / 2;
            double startY = 50;
            double xSpacing = 1000;
            double ySpacing = 400;

            drawNode(gc, root, startX, startY, xSpacing, ySpacing);

            canvasContainer.getChildren().clear();
            canvasContainer.getChildren().add(canvas);

            // Adjust canvas size to fit the tree if necessary
            adjustCanvasSize(gc, root, canvas, startX, startY, xSpacing, ySpacing);
        }
    }

    private void drawNode(GraphicsContext gc, Node node, double x, double y, double xSpacing, double ySpacing) {
        if (node != null) {
            // Set color for node label (attribute name or class if leaf)
            gc.setFill(Color.BLUE);
            String label = node.isLeaf() ? node.getMajorClass() : node.getAttribute().getAttributeName();
            gc.fillText(label, x, y);

            if (!node.isLeaf()) {
                double childY = y + ySpacing;
                int childrenCount = node.getAttribute().getBranches().size();
                double childXSpacing = xSpacing / childrenCount;

                for (int i = 0; i < childrenCount; i++) {
                    Node child = node.getAttribute().getBranches().get(i).getChildNode();
                    double childX = x + (i - (childrenCount - 1) / 2.0) * childXSpacing;

                    // Draw line to child node
                    gc.setStroke(Color.BLACK);
                    gc.strokeLine(x, y, childX, childY);

                    // Set color for branch value
                    gc.setFill(Color.RED);
                    gc.fillText(node.getAttribute().getBranches().get(i).getValue(), (x + childX) / 2, (y + childY) / 2);

                    // Recursively draw child node
                    drawNode(gc, child, childX, childY, xSpacing * 2, ySpacing);
                }
            }
        }
    }

    private void adjustCanvasSize(GraphicsContext gc, Node node, Canvas canvas, double x, double y, double xSpacing, double ySpacing) {
        if (node != null) {
            double[] bounds = getTreeBounds(node, x, y, xSpacing, ySpacing);
            double minX = bounds[0];
            double maxX = bounds[1];
            double minY = bounds[2];
            double maxY = bounds[3];

            double canvasWidth = Math.max(canvas.getWidth(), maxX - minX + xSpacing);
            double canvasHeight = Math.max(canvas.getHeight(), maxY - minY + ySpacing);

            canvas.setWidth(canvasWidth);
            canvas.setHeight(canvasHeight);

            // Center the tree
            double offsetX = (canvasWidth - (maxX - minX)) / 2 - minX;
            double offsetY = 200;

            gc.clearRect(0, 0, canvasWidth, canvasHeight);
            drawNode(gc, node, x + offsetX, y + offsetY, xSpacing, ySpacing);
        }
    }

    private double[] getTreeBounds(Node node, double x, double y, double xSpacing, double ySpacing) {
        double minX = x;
        double maxX = x;
        double minY = y;
        double maxY = y;

        if (!node.isLeaf()) {
            double childY = y + ySpacing;
            int childrenCount = node.getAttribute().getBranches().size();
            double childXSpacing = xSpacing / childrenCount;

            for (int i = 0; i < childrenCount; i++) {
                Node child = node.getAttribute().getBranches().get(i).getChildNode();
                double childX = x + (i - (childrenCount - 1) / 2.0) * childXSpacing;

                double[] childBounds = getTreeBounds(child, childX, childY, xSpacing, ySpacing);

                minX = Math.min(minX, childBounds[0]);
                maxX = Math.max(maxX, childBounds[1]);
                minY = Math.min(minY, childBounds[2]);
                maxY = Math.max(maxY, childBounds[3]);
            }
        }

        return new double[]{minX, maxX, minY, maxY};
    }

    private double[] calculateBounds(Node node, double x, double y, double xSpacing, double ySpacing) {
        double minX = x;
        double maxX = x;
        double minY = y;
        double maxY = y;

        if (!node.isLeaf()) {
            double childY = y + ySpacing;
            int childrenCount = node.getAttribute().getBranches().size();
            double childXSpacing = xSpacing / childrenCount;

            for (int i = 0; i < childrenCount; i++) {
                Node child = node.getAttribute().getBranches().get(i).getChildNode();
                double childX = x + (i - (childrenCount - 1) / 2.0) * childXSpacing;

                double[] childBounds = calculateBounds(child, childX, childY, xSpacing / 2, ySpacing);

                minX = Math.min(minX, childBounds[0]);
                maxX = Math.max(maxX, childBounds[1]);
                minY = Math.min(minY, childBounds[2]);
                maxY = Math.max(maxY, childBounds[3]);
            }
        }

        return new double[]{minX, maxX, minY, maxY};
    }
}
