package io.github.pourianof.gaussjordan;


import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Helper {

    static ChangeListener<String> isNumHandler = (obs, oldVal, newVal) -> {
        Helper.numberInputHandler(obs, oldVal, newVal);
    };

    private Helper() {
    }

    static boolean isNumber(String input) {
        if (input.isEmpty()) {
            return true;
        }
        char character = input.charAt(input.length() - 1);
        return Character.isDigit(character) || character == '-';
    }

    static void numberInputHandler(javafx.beans.Observable observable, String oldValue, String newValue) {
        if (!Helper.isNumber(newValue)) {
            try {
                ((StringProperty) observable).setValue(oldValue);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void drawMatrix(Pane pane, double[][] matrix, final int rowNum, final int colNum, boolean isDet, boolean isInverse, double[][] inversePart) {
        final double xWidth = 50;
        final double yHeight = 30;
        final double xOffset = 20;
        final double yOffset = 20;
        final int forNum = isDet ? colNum : rowNum;
        final int totCol = isInverse ? colNum * 2 : colNum;
        for (int i = 0; i < forNum; i++) {
            if (isDet) {
                Text txt = new Text(matrix[0][i] + "");
                txt.setX(i * xWidth + xOffset);
                txt.setY(yOffset);
                pane.getChildren().add(txt);
            } else {
                for (int j = 0; j < totCol; j++) {
                    double[][] mat = (isInverse && j >= colNum ? inversePart : matrix);
                    int currentCol = j % colNum;
                    Text txt = new Text(mat[i][currentCol] + "");
                    if (mat[i][currentCol] != 0) {
                        txt.setFill(Color.BLUE);
                    }
                    txt.setX(j * xWidth + xOffset);
                    txt.setY(i * yHeight + yOffset);
                    pane.getChildren().add(txt);

                }
//                pane.getChildren().add(new Line(colNum * xWidth + 5, ));

            }
        }

        final float braceWidth = 20;
        final double endY = (rowNum * yHeight);
        final double startRightX = (totCol * xWidth);

        final double[] startX = {10, startRightX};
        final double[] startY = {5, endY};

        if (isInverse) {
            double xStart = 5 + xWidth * colNum;
            Line inverseMiddleLine = new Line(xStart, 5, xStart, endY);
            inverseMiddleLine.setStroke(Color.RED);
            inverseMiddleLine.setStrokeWidth(2);
            pane.getChildren().add(inverseMiddleLine);
        }

        for (int i = 0; i < 2; i++) {
            float x = i == 0 ? braceWidth : -braceWidth;
            Line upLine = new Line(startX[i], startY[0], startX[i] + x, startY[0]);
            Line middleLine = new Line(startX[i], startY[0], startX[i], startY[1]);
            Line bottomLine = new Line(startX[i], startY[1], startX[i] + x, startY[1]);
            upLine.setStrokeWidth(3);
            upLine.setStroke(Color.BLACK);
            middleLine.setStrokeWidth(3);
            middleLine.setStroke(Color.BLACK);
            bottomLine.setStrokeWidth(3);
            bottomLine.setStroke(Color.BLACK);
            pane.getChildren().addAll(upLine, middleLine, bottomLine);
        }

    }

}
