package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import uk.ac.bris.cs.scotlandyard.ai.Visualiser;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameTree;
import java.util.*;

/**
 * Controls the display and rendering for the GameMonitor.
 */
public class GameMonitorView {

    private Visualiser visualiser;
    private GameMonitorController controller;
    private Integer moveNum = 0;
    private XYChart.Series nextRoundSeries = new XYChart.Series();
    private XYChart.Series score = new XYChart.Series();
    private XYChart.Series prune = new XYChart.Series();
    private XYChart.Series treeFinish = new XYChart.Series();
    private DrawTree dt;
    private int windowX = 1400;
    private int windowY = 850;
    private int tabNumber = 0;

    public GameMonitorView(Visualiser visualiser, GameMonitorController controller) {
        this.visualiser = visualiser;
        this.controller = controller;
        Stage stage = (Stage) this.visualiser.surface().getScene().getWindow();
        stage.setResizable(false);

        visualiserInit();
        timeInitGraph();
    }

    private void visualiserInit() {
        Group root = new Group();
        Scene scene = new Scene(root, windowX, windowY);
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setBackground(new Background(new BackgroundFill(Color.web("#2a2a2a"), CornerRadii.EMPTY, Insets.EMPTY)));
        BorderPane borderPane = new BorderPane();

        String tabs[] = {"Tree", "Times", "Map", "Heatmap", "Route Tracer"};

        for(int i = 0; i < tabs.length; i++) {
            Tab tab = new Tab();
            tab.setText(tabs[i]);

            HBox hbox = new HBox();
            Label label = new Label("Tab" + i);
            label.setTextFill(Color.WHITE);
            hbox.getChildren().add(label);

            hbox.setAlignment(Pos.CENTER);
            tab.setContent(hbox);
            tabPane.getTabs().add(tab);
        }

        TabPane treeTabs = new TabPane();
        treeTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().get(0).setContent(treeTabs);

        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);

        visualiser.surface().getChildren().add(root);
    }

    private void timeInitGraph() {
        Tab tab = returnTab(1);

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Move no.");
        yAxis.setLabel("Time (ms)");

        ScatterChart<Number, Number> chart = new ScatterChart<Number, Number>(xAxis, yAxis);
        chart.setTitle("Graph Visitor Timing");

        Set<Node> axisNode = chart.lookupAll(".axis-label");
        for(final Node axis : axisNode) {
            axis.setStyle("-fx-text-fill: white;");
        }
        Node title = chart.lookup(".chart-title");
        title.setStyle("-fx-text-fill: white;");

        nextRoundSeries.setName("nextRound");
        score.setName("score");
        prune.setName("prune");
        treeFinish.setName("treeFinish");

        chart.getData().addAll(nextRoundSeries, score, prune, treeFinish);
        tab.setContent(chart);
    }

    private Tab returnTab(int index) {
        Group root = (Group) visualiser.surface().getChildren().get(0);
        BorderPane borderPane = (BorderPane) root.getChildren().get(0);
        TabPane tabPane = (TabPane) borderPane.getChildren().get(0);
        Tab tab = tabPane.getTabs().get(index);

        return tab;
    }

    public void nextRoundTime(long time) {
        this.moveNum++;
        Platform.runLater(() -> {
            this.nextRoundSeries.getData().add(new XYChart.Data(this.moveNum, time));
        });
    }

    public void scoreTime(long time) {
        Platform.runLater(() -> {
            this.score.getData().add(new XYChart.Data(this.moveNum, time));
        });
    }

    public void pruneTime(long time) {
        Platform.runLater(() -> {
            this.prune.getData().add(new XYChart.Data(this.moveNum, time));
        });
    }

    public void treeFinishTime(long time) {
        Platform.runLater(() -> {
            this.treeFinish.getData().add(new XYChart.Data(this.moveNum, time));
        });
    }

    public void drawTree(GameTree tree) {
        Tab tab = drawTreeTab();
        BorderPane bp = new BorderPane();
        Canvas canvas = new Canvas(windowX, windowY);
        bp.setCenter(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#2a2a2a"));
        gc.fillRect( 0, 0, canvas.getWidth(), canvas.getHeight());

        Button leftB = leftBSetup();
        Button rightB = rightBSetup();
        Button zoomOut = zoomOutSetup();
        Button zoomIn = zoomInSetup();
        Label clickedNode = clickedNodeLabelSetup();

        Pane pane = new Pane();
        pane.getChildren().addAll(leftB, rightB, zoomOut, zoomIn, clickedNode);
        bp.setTop(pane);

        this.dt = initialiseDrawTree(tree, (int) canvas.getWidth()/2);
        drawTreeFromGraph(dt, gc);

        Platform.runLater(() -> tab.setContent(bp));

        controller.viewAttributes(canvas, gc, dt, this);
        controller.leftBHandlerInit(leftB);
        controller.rightBHandlerInit(rightB);
        controller.zoomInHandlerInit(zoomIn);
        controller.zoomOutHandlerInit(zoomOut);
        controller.mouseClickHandlerInit(clickedNode);
    }

    private Label clickedNodeLabelSetup() {
        Label clickedNode = new Label("Clicked Node Score: ");
        clickedNode.setFont(new Font("Arial", 18));
        clickedNode.setLayoutX(250);
        clickedNode.setLayoutY(4);
        clickedNode.setTextFill(Color.WHITE);

        return clickedNode;
    }

    private Button leftBSetup() {
        Button leftB = new Button();
        leftB.setMinWidth(100);
        leftB.setText("Left");

        return leftB;
    }

    private Button rightBSetup() {
        Button rightB = new Button();
        rightB.setMinWidth(100);
        rightB.setLayoutX(100);
        rightB.setText("Right");

        return rightB;
    }

    private Button zoomOutSetup() {
        Button zoomOut = new Button();
        zoomOut.setLayoutX(200);
        zoomOut.setMinWidth(20);
        zoomOut.setText("-");

        return zoomOut;
    }

    private Button zoomInSetup() {
        Button zoomIn = new Button();
        zoomIn.setLayoutX(220);
        zoomIn.setMinWidth(20);
        zoomIn.setText("+");

        return zoomIn;
    }

    public void clearCanvas(javafx.scene.canvas.Canvas canvas, GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.web("#2a2a2a"));
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
    }

    private Tab drawTreeTab() {
        Tab mainTab = returnTab(0);
        TabPane tabs = (TabPane) mainTab.getContent();
        Tab tab = new Tab();
        tabNumber++;
        tab.setText(Integer.toString(tabNumber));
        Platform.runLater(() -> tabs.getTabs().add(tab));

        return tab;
    }

    private DrawTree initialiseDrawTree(GameTree tree, int x) {
        DrawTree dt = new DrawTree(tree, x, 100);

        SpaceNeededVisitor Ian = new SpaceNeededVisitor();
        XYVisitor Dave = new XYVisitor();

        dt.accept(Ian);     //space needed
        dt.accept(Dave);  //coordinates calculated

        return dt;
    }

    public void findNodeOnClick(DrawTree tree, int x, int y, Label clickedNode) {
        boolean located = false;
        int threshold = 5;

        for (DrawTree child : tree.getChildDrawTrees()) {
            if (x > child.getX()-threshold && x < child.getX()+threshold) {
                if (y < child.getY()+threshold && y > child.getY()-threshold) {
                    located = true;
                }
            }

            if(located) {
                located = false;
                clickedNode.setText("Clicked Node Score: " + child.getScore());
            }
            else findNodeOnClick(child, x, y, clickedNode);
        }
    }

    public void drawTreeFromGraph(DrawTree tree, GraphicsContext gc) {
        drawLines(tree, gc);
        drawCircles(tree, gc);
        highlightChosenMoves(tree, gc);

        //Draw first circle
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(dt.getX(), dt.getY(), 8 , 8);
    }

    private void drawCircles(DrawTree tree, GraphicsContext gc) {
        for (DrawTree child : tree.getChildDrawTrees()) {
            gc.setLineWidth(2);
            if(child.isDeadNode()) {gc.setFill(Color.RED);}
            else {gc.setFill(Color.WHITE);}
            gc.fillOval(child.getX(), child.getY(), 6, 6);

            drawCircles(child, gc);
        }
    }

    private void drawLines(DrawTree tree, GraphicsContext gc) {
        for (DrawTree child : tree.getChildDrawTrees()) {

            if(!child.getIsMrXRound()) {gc.setStroke(Color.BLUE);}
            else {gc.setStroke(Color.WHITE);}
            gc.setLineWidth(1);
            gc.strokeLine(tree.getX()+3, tree.getY()+3, child.getX()+3, child.getY()+3);

            drawLines(child, gc);
        }
    }

    private void highlightChosenMoves(DrawTree tree, GraphicsContext gc) {
        int chosenScore = 0;
        DrawTree chosenNode = null;

        if(!tree.getIsMrXRound()) {
            for (DrawTree child : tree.getChildDrawTrees()) {
                if (child.getScore() > chosenScore) {
                    chosenScore = child.getScore();
                    chosenNode = child;
                }
            }
        }
        else {
            for (DrawTree child : tree.getChildDrawTrees()) {
                if (child.getScore() < chosenScore) {
                    chosenScore = child.getScore();
                    chosenNode = child;
                }
            }
        }



        if(chosenNode != null) {
            highlightChosenMoves(chosenNode, gc);

            gc.setStroke(Color.GREEN);
            gc.strokeLine(tree.getX()+3, tree.getY()+3, chosenNode.getX()+3, chosenNode.getY()+3);

            gc.setFill(Color.GREEN);
            gc.fillOval(chosenNode.getX(), chosenNode.getY(), 6, 6);

        }
    }
}
