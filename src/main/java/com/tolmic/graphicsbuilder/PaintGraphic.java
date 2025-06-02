package com.tolmic.graphicsbuilder;

import java.lang.reflect.Method;

import com.tolmic.graphicsbuilder.Exception.NonEqualsSize;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class PaintGraphic {

    @FunctionalInterface
    private interface DrawLine {
        public void drawLine(double x, double thickness);
    }

    @FunctionalInterface
    private interface DrawLabel {
        public void drawLabel(double number, double secondCoordinate);        
    }

    @FunctionalInterface
    private interface MaxBorder {
        public double getMaxBorder();
    }

    private GraphicsContext gc;
    private double width;
    private double height;

    private final double AXLE_THICKNESS = 3.0;
    private final double REFERENCE_LINES_THICKNESS = 2.0;
    private final double ORDINARY_LINES_THICKNESS = 1.0;
    private final int NUMBER_SHARES = 5;
    private final double SIZE_UNIT = 30.0;
    private final double D_SCALE = 1;

    private final double LABEL_HEIGHT = 20;
    
    private double scale = 5;

    private double labelWidth = 20;

    private double OX;
    private double OY;

    private double x1;
    private double y1;

    private double discreate = 5.0;

    private boolean isNeedGrid = true;

    private double[] x = new double[0];
    private double[] y = new double[0];

    public PaintGraphic(Canvas graphics) {
        this.gc = graphics.getGraphicsContext2D();

        this.width = graphics.getWidth();
        this.height = graphics.getHeight();

        this.OX = width / 2;
        this.OY = height / 2;

        paint();
    }

    private void paint() {
        clear();

        double Ox = OX - AXLE_THICKNESS / 2;
        double Oy = OY - AXLE_THICKNESS / 2;

        drawCoordinateAxes(Ox, Oy);

        drawVerticalLine(Ox, AXLE_THICKNESS);
        drawHorizontalLine(Oy, AXLE_THICKNESS);

        if (isNeedGrid) {
            drawGrid(Ox, Oy);
        }

        drawGraphic();
        drawLabels(Ox, Oy);
    }

    private void clear() {
        gc.clearRect(0, 0, width, height);
    }

    public void drawCoordinateAxes(double Ox, double Oy) {
        drawVerticalLine(Ox, AXLE_THICKNESS);
        drawHorizontalLine(Oy, AXLE_THICKNESS);
    }

    private void drawGrid(double Ox, double Oy) {
        drawGridAxisX(Ox, Oy);
        drawGridAxisY(Ox, Oy);
    }

    private void drawGridAxisX(double Ox, double Oy) {
        drawSymmetricalLines(Ox, (p, th) -> drawVerticalLine(p, th), this::getWidth);
    }

    private void drawGridAxisY(double Ox, double Oy) {
        drawSymmetricalLines(Oy, (p, th) -> drawHorizontalLine(p, th), this::getHeight);
    }

    private void drawSymmetricalLines(double p0, DrawLine lineMethod, MaxBorder maxBorder) {
        int n = countLineNumber(p0);
        double p = countStartCoordinate(p0, n);

        while (p < maxBorder.getMaxBorder()) {
            if (n != 0) {
                lineMethod.drawLine(p, defindThinkness(n));
            }
            
            p += SIZE_UNIT;
            n += 1;
        }
    }

    private double defindThinkness(int n) {
        return n % NUMBER_SHARES == 0 ? REFERENCE_LINES_THICKNESS : ORDINARY_LINES_THICKNESS;
    }

    private void drawLabels(double Ox, double Oy) {
        drawLabelsAxisX(Ox, Oy);
        drawLabelsAxisY(Ox, Oy);
    }

    private void drawLabelsAxisX(double Ox, double Oy) {
        drawSymmetricalLabels(Ox, (v, p) -> drawLabel(v, p, Oy), this::getWidth);
    }

    private void drawLabelsAxisY(double Ox, double Oy) {
        drawSymmetricalLabels(Oy, (v, p) -> drawLabel(v, Ox, p), this::getHeight);
    }

    private void drawSymmetricalLabels(double p0, DrawLabel labelMethod, MaxBorder maxBorder) {
        int n = countControlLineNumber(p0);
        double p = countStartControlCoordinate(p0, n);

        double coordinateIncrement = SIZE_UNIT * NUMBER_SHARES;

        while (p < maxBorder.getMaxBorder()) {
            if (n != 0) {
                labelMethod.drawLabel(n * NUMBER_SHARES * D_SCALE, p);
            }

            p += coordinateIncrement;
            n += 1;
        }
    }

    private int countControlLineNumber(double p0) {
        int n = countLineNumber(p0);
        return (int) (n / NUMBER_SHARES + (p0 < 0 && n % NUMBER_SHARES != 0 ? 1 : 0));
    }

    private double countStartControlCoordinate(double p0, int n) {
        return p0 + n * SIZE_UNIT * NUMBER_SHARES;
    }

    // number of first ordinary line from axis in screen
    private int countLineNumber(double p0) {
        double p0M = Math.abs(p0);
        return (int) (p0 >= 0 ? -(p0M / SIZE_UNIT) : (p0M / SIZE_UNIT) + 1);
    }

    private double countStartCoordinate(double p0, int n) {
        return p0 + n * SIZE_UNIT;
    }

    private void drawVerticalLine(double coordinate, double thickness) {
        gc.fillRect(coordinate, 0, thickness, height);
    }

    private void drawHorizontalLine(double coordinate, double thickness) {
        gc.fillRect(0, coordinate, width, thickness);
    }

    public void addFunctionGraphic(double[] x, double[] y) {

        if (x.length != y.length) {
            new NonEqualsSize("Sizes of x and y must be equals.");
        }

        this.x = x;
        this.y = y;

        paint();
    }

    private void defindLabelWidth(int n) {
        double w = 30;
        
        for (int i = n; i < 6 && scale / i < 10; i *= 10) {
            w += 5;
        }

        labelWidth = w;
    }

    private double specifyWidth(double value) {
        int digitsNumber = String.valueOf(value).length();
        int scaleDigits = String.valueOf(scale).length() - 2;

        if (scale > 0) {
            scaleDigits += 2;
        }

        if (digitsNumber <= scaleDigits) {
            return labelWidth;
        }

        double t = labelWidth;

        for (double i = scaleDigits; i < digitsNumber; i++) {
            t += 5;
        }

        return t;
    }

    public void increaseScale() {
        scale += D_SCALE;
        
        defindLabelWidth(1);
        paint();
    }

    public void decreaseScale() {
        scale -= D_SCALE;

        defindLabelWidth(1);
        paint();
    }

    // подумать
    private void drawLabel(double value, double x0, double y0) {
        double width = specifyWidth(value);

        double x = x0 - width / 2;

        double xChecked = getCorrectX(x0 - width / 2);
        double yChecked = getCorrectY(y0);

        gc.setFill(Color.WHITE);

        gc.fillRect(xChecked, yChecked + 5, width, LABEL_HEIGHT);

        gc.setFill((xChecked != x || yChecked != y0) ? Color.GREY : Color.BLACK);

        gc.fillText(String.valueOf(value), xChecked, yChecked + 20);

        gc.setFill(Color.BLACK);
    }

    private double getCorrectX(double x) {
        double t = x;

        if (x < 0) {
            t = 0;
        } else if (x + labelWidth > width) {
            t = width - labelWidth;
        }

        return t;
    }

    private double getCorrectY(double y) {
        double t = y;

        if (y < 0) {
            t = 0;
        } else if (y + LABEL_HEIGHT > height) {
            t = height - LABEL_HEIGHT;
        }

        return t;
    }

    private void drawGraphic() {
        gc.setFill(Color.BLUE);

        // ...

        gc.setFill(Color.BLACK);
    }

    public void changeGridMode() {
        isNeedGrid = !isNeedGrid;

        paint();
    }

    public void changeCenter(double x, double y) {
        OX += (x - x1);
        OY += (y - y1);

        x1 = x;
        y1 = y;

        paint();
    }

    public void setStartPointForDrag(double x, double y) {
        x1 = x;
        y1 = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

}
