package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import uk.ac.bris.cs.scotlandyard.ai.Visualiser;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameTree;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Adam on 18/04/2017.
 */
public class GameMonitorView {

    private Visualiser visualiser;
    private Integer moveNum = 0;
    private XYChart.Series nextRoundSeries = new XYChart.Series();
    private XYChart.Series score = new XYChart.Series();
    private XYChart.Series prune = new XYChart.Series();
    private XYChart.Series treeFinish = new XYChart.Series();
    private DrawTree dt;
    private int originX;

    public GameMonitorView(Visualiser visualiser) {
        this.visualiser = visualiser;

        visualiserInit();
        timeInitGraph();
    }

    private void visualiserInit() {
        Group root = new Group();
        Scene scene = new Scene(root, 1400, 850);
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
        chart.setTitle("Calculation Time Again...");

        nextRoundSeries.setName("nextRound");
        score.setName("score");
        prune.setName("prune");
        treeFinish.setName("treeFinish");

        chart.getData().addAll(nextRoundSeries, score, prune, treeFinish);
        //chart.setStyle("-fx-font-color: white;");         TODO Graph text colour :/
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
        Tab tab = returnTab(0);
        BorderPane bp = new BorderPane();
        ScrollPane sp = new ScrollPane();
        Canvas canvas = new Canvas(5000, 1000);
        bp.setCenter(sp);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#2a2a2a"));
        gc.fillRect( 0, 0, canvas.getWidth(), canvas.getHeight());
        sp.setHvalue(0.5);

        Button leftB = new Button();
        leftB.setMinWidth(100);
        leftB.setText("Left");
        Button rightB = new Button();
        rightB.setMinWidth(100);
        rightB.setLayoutX(100);
        rightB.setText("Right");
        Button zoomOut = new Button();
        zoomOut.setLayoutX(200);
        zoomOut.setMinWidth(20);
        zoomOut.setText("-");
        Button zoomIn = new Button();
        zoomIn.setLayoutX(220);
        zoomIn.setMinWidth(20);
        zoomIn.setText("+");

        Pane pane = new Pane();
        pane.getChildren().addAll(leftB, rightB, zoomOut, zoomIn);
        bp.setTop(pane);

        this.dt = new DrawTree(tree, (int) canvas.getWidth()/2, 100);

        SpaceNeededVisitor Ian = new SpaceNeededVisitor();
        XYVisitor Dave = new XYVisitor();

        dt.accept(Ian);     //space needed
        dt.accept(Dave);  //coordinates calculated

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(dt.getX(), dt.getY(), 8 , 8);

        gc.setFill(Color.WHITE);
        drawTreeFromGraph(dt, gc, canvas);

        Platform.runLater(() -> tab.setContent(bp));
        Platform.runLater(() -> sp.setContent(canvas));

        TransformVisitor leftV = new TransformVisitor(200);
        TransformVisitor rightV = new TransformVisitor(-200);

        leftB.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                dt.accept(leftV);
                gc.setFill(Color.web("#2a2a2a"));
                gc.fillRect( 0, 0, canvas.getWidth(), canvas.getHeight());
                drawTreeFromGraph(dt, gc, canvas);
            }
        });

        rightB.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                dt.accept(rightV);
                gc.setFill(Color.web("#2a2a2a"));
                gc.fillRect( 0, 0, canvas.getWidth(), canvas.getHeight());
                drawTreeFromGraph(dt, gc, canvas);
            }
        });

        zoomOut.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                gc.scale(0.5, 0.5);
            }
        });

        zoomIn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                gc.scale(2, 2);
            }
        });
    }

    private void drawTreeFromGraph(DrawTree tree, GraphicsContext gc, Canvas canvas) {
        for (DrawTree child : tree.getChildDrawTrees()) {

            gc.setFill(Color.WHITE);
            gc.setLineWidth(1);
            gc.strokeLine(tree.getX()+3, tree.getY()+3, child.getX()+3, child.getY()+3);

            gc.setLineWidth(2);
            if(child.isDeadNode()) {gc.setFill(Color.RED);}
            gc.fillOval(child.getX(), child.getY(), 6, 6);

            drawTreeFromGraph(child, gc, canvas);
        }
    }
}
