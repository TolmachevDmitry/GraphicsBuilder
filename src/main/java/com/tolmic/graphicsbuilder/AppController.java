package com.tolmic.graphicsbuilder;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;


public class AppController {

    private PaintGraphic paintGraphic;
    private SolveTree solveTree;

    private String[] changeGridModeLabels = new String[] {"Отключить сетку", "Включить сетку"};
    private int number = 0;

    @FXML
    private TilePane puzzleField;

    @FXML
    private TextField formula;

    @FXML
    private Button minus;

    @FXML
    private Button plus;
    
    @FXML
    private Canvas graphics;

    @FXML
    private Button changeGridMode;

    @FXML
    private void initialize() {
        paintGraphic = new PaintGraphic(graphics);
        solveTree = new SolveTree();

        graphics.setOnMousePressed(ev -> {
            paintGraphic.setStartPointForDrag(ev.getX(), ev.getY());
        });

        graphics.setOnMouseDragged(ev -> {
            paintGraphic.changeCenter(ev.getX(), ev.getY());
        });
    }

    @FXML
    protected void onChangeGridMode() {
        paintGraphic.changeGridMode();

        number += 1;
        if (number == 2) {
            number = 0;
        }

        changeGridMode.setText(changeGridModeLabels[number]);
    }

    @FXML
    protected void onIncreaseScale() {
        paintGraphic.increaseScale();
    }

    @FXML
    protected void onDecreaseScale() {
        paintGraphic.decreaseScale();
    }

    @FXML
    protected void writenFormula() {

    }

}
