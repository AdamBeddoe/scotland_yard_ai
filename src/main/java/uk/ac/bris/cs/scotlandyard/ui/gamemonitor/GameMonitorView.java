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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uk.ac.bris.cs.scotlandyard.ai.Visualiser;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameTree;

import javax.swing.text.Element;
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

    public GameMonitorView(Visualiser visualiser) {
        this.visualiser = visualiser;

        visualiserInit();
        timeInitGraph();
    }

    private void visualiserInit() {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 800);
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

    //Tried to use a table to display, cannot do individual cells :/
    private void timeInit() {
        Group root = (Group) visualiser.surface().getChildren().get(0);
        BorderPane borderPane = (BorderPane) root.getChildren().get(0);
        TabPane tabPane = (TabPane) borderPane.getChildren().get(0);
        Tab timesTab = tabPane.getTabs().get(1);

        TableView table = new TableView();

        TableColumn moveNumber = new TableColumn("Move #");
        TableColumn nextRound = new TableColumn("Next Round");
        TableColumn score = new TableColumn("Score");
        TableColumn prune = new TableColumn("Prune");
        TableColumn treeFinish = new TableColumn("Tree finish");

        table.getColumns().addAll(moveNumber, nextRound, score, prune, treeFinish);

        ObservableList data =
                FXCollections.observableArrayList("hello");


        table.setItems(data);
        timesTab.setContent(table);


        //System.out.println(table.);
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
        Canvas canvas = new Canvas(800, 800);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(400, 20, 30 , 30);

        int spacing = 800/(tree.getChildTrees().size());
        int x = 0;

        for(GameTree child : tree.getChildTrees()) {
            gc.strokeOval(x, 100, spacing, spacing);
            x = x + spacing;
        }

        System.out.println(tree.getChildTrees().size());


        //tree.getScore()
        Platform.runLater(() -> tab.setContent(canvas));
    }
}
