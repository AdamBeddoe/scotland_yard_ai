package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

import javafx.application.Platform;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;

import java.awt.Canvas;

/**
 * Controls user input from the GameMonitor.
 */
public class GameMonitorController {
    private javafx.scene.canvas.Canvas canvas;
    private GraphicsContext gc;
    private DrawTree dt;
    private GameMonitorView view;

    void viewAttributes(javafx.scene.canvas.Canvas canvas, GraphicsContext gc, DrawTree dt, GameMonitorView view) {
        this.canvas = canvas;
        this.gc = gc;
        this.dt = dt;
        this.view = view;
    }

    void leftBHandlerInit(javafx.scene.control.Button leftB) {
        TransformVisitor leftV = new TransformVisitor(800);

        leftB.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                view.clearCanvas(canvas, gc);
                dt.accept(leftV);
                view.drawTreeFromGraph(dt, gc);
            }
        });
    }

    void rightBHandlerInit(Button rightB) {
        TransformVisitor rightV = new TransformVisitor(-800);

        rightB.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                view.clearCanvas(canvas, gc);
                dt.accept(rightV);
                view.drawTreeFromGraph(dt, gc);
            }
        });
    }

    void zoomOutHandlerInit(Button zoomOut) {
        zoomOut.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                view.clearCanvas(canvas, gc);
                gc.scale(0.5, 0.5);
                view.drawTreeFromGraph(dt, gc);
            }
        });
    }

    void zoomInHandlerInit(Button zoomIn) {
        zoomIn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                view.clearCanvas(canvas, gc);
                gc.scale(2, 2);
                view.drawTreeFromGraph(dt, gc);
            }
        });
    }

    void mouseClickHandlerInit(Label clickedNode) {
        canvas.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.findNodeOnClick(dt, (int) event.getX(), (int) event.getY(), clickedNode);
            }
        });
    }

}
