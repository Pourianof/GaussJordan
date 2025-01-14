package io.github.pourianof.gaussjordan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

final class Matrix<T extends Number> extends ArrayList<MatrixRow<T>> implements MatrixProvider {

    private double[] determinantCoes;
    private double[][] inverseMainMatrix;
    private double[][] inverseIMatrix;
    private double[][] solvedMatrix;
    private int counter = 0;
    private InverseHint inverseHint = null;
    private SolveHint solveHint = null;

    public Matrix() {
    }

    private double detMultiplier() {
        double mul = 1;
        for (int i = 0; i < this.determinantCoes.length; i++) {
            mul *= this.determinantCoes[i];
            if (mul == 0) {
                return 0;
            }
        }
        return mul;
    }

    private static EventHandler<ActionEvent> takeOutputHandlerProvider(double[][] matrix, int rows, int cols) {
        return e -> {
            Stage stage = new Stage();
            stage.initOwner(StageProvider.getRootStage());
            FileChooser fc = new FileChooser();
            File file = fc.showSaveDialog(StageProvider.getRootStage());
            fc.setTitle("Save In File");
            fc.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
            if (file != null) {
                file = new File(file.getAbsolutePath() + ".txt");
                try ( PrintWriter dos = new PrintWriter(file);) {
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            dos.write(matrix[i][j] + " ");
                        }
                        if (i != cols - 1) {
                            dos.write("\n");
                        }
                    }
                } catch (IOException err) {

                }
            }
        };
    }

    private static Pane newStageDesigner(String topTitle, Node bottomDesc, double[][] matrix, boolean displayBtn, int rows, int cols) {
        VBox vb = new VBox();
        vb.setSpacing(10);
        vb.setPadding(new Insets(10));
        vb.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        vb.setAlignment(Pos.CENTER);
        ScrollPane sp = new ScrollPane(vb);
        sp.setStyle("-fx-background:white; -fx-backgroundColor:white;");

        Pane paintPane = new Pane();
        paintPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        vb.getChildren().addAll(new Label(topTitle), paintPane, bottomDesc);
        if (displayBtn) {
            Button takeOutPutBtn = new Button("ذخيره در فايل");
            takeOutPutBtn.setOnAction(takeOutputHandlerProvider(matrix, rows, cols));
            vb.getChildren().add(takeOutPutBtn);
        }
        StageProvider.getEmptyPane().getChildren().add(sp);

        return paintPane;
    }

    private static Pane newStageDesigner(String topTitle, String bottomDesc, double[][] matrix, boolean displayBtn, int rows, int cols) {
        return newStageDesigner(topTitle, new Label(bottomDesc), matrix, displayBtn, rows, cols);
    }

    @Override
    public void calculateMatrixDeterminant() throws NotSquareException {
        int deg = this.getRowsNumber();

        if (this.determinantCoes == null) {
            if (!this.isSquare()) {
                throw new NotSquareException("Matrix is not square", deg, this.getColsNumber());
            }
            this.determinantCoes = new double[deg];
            double[][] cloned = this.cloneMatrix();
            GaussSolver.determinantCalc(cloned, this.determinantCoes);

        }
        double det = this.detMultiplier();
        double[][] detCompat = new double[1][this.getColsNumber()];
        detCompat[0] = this.determinantCoes;
        Helper.drawMatrix(newStageDesigner(LocaleManager.getManager().get("upTriangle") + ":", "Det(A) = " + det, null, false, 1, deg), detCompat, 1, deg, true, false, null);
        StageProvider.displayStage();
    }

    public boolean isSquare() {
        return this.getRowsNumber() == this.getColsNumber();
    }

    public int getRowsNumber() {
        return this.size();
    }

    public int getColsNumber() {
        return this.get(0).size();
    }

    public void fileOutput(String address) {
        try ( PrintWriter pw = new PrintWriter(new FileOutputStream(address));) {
            for (int i = 0; i < this.getRowsNumber(); i++) {
                for (int j = 0; j < this.getColsNumber(); j++) {
                    pw.print(100.0);
                }
                pw.println("");
            }
        } catch (FileNotFoundException err) {
        }

    }

    @Override
    public MatrixRow get(int index) {
        return super.get(index);
    }

    @Override
    public void showMatrix(Pane pane) {
        Helper.drawMatrix(pane, this.cloneMatrix(), this.getRowsNumber(), this.getColsNumber(), false, false, null);
    }

    private double[][] cloneMatrix() {
        this.counter = 0;
        final double[][] x = new double[this.getRowsNumber()][this.getColsNumber()];

        this.stream().forEach(e -> {
            x[this.counter++] = e.toArr();
        });
        return x;
    }

    @Override
    public void inverseMatrix() throws NotSquareException {
        int deg = this.getRowsNumber();

        if (this.inverseIMatrix == null) {
            if (!this.isSquare()) {
                throw new NotSquareException("Matrix is not square", deg, this.getColsNumber());
            }
            this.inverseIMatrix = new double[deg][deg];
            for (int i = 0; i < deg; i++) {
                this.inverseIMatrix[i][i] = 1;
            }
            inverseMainMatrix = this.cloneMatrix();
            this.inverseHint = (InverseHint) GaussSolver.inverseMatrix(inverseMainMatrix, this.inverseIMatrix);
        }
        String msg = this.inverseHint.getInvertible() ?
                "** " + LocaleManager.getManager().get("unInvertibleMsg") +
                        "\n" + LocaleManager.getManager().get("noteToRow") +
                        (this.inverseHint.getZeroRows().get(0) + 1) : LocaleManager.getManager().get("invertibleMsg");
        Helper.drawMatrix(newStageDesigner("معکوس ماتريس : ", msg, this.inverseIMatrix, true, deg, deg), inverseMainMatrix, deg, deg, false, true, this.inverseIMatrix);
        StageProvider.displayStage();

    }

    public VBox eqProvider(double[][] matrix) {
        VBox vb = new VBox();
        vb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
//        StringBuilder equationsStringbuilder = new StringBuilder();

        StringBuilder hintsStringbuilder = new StringBuilder();
        boolean all_zero = true;
        int colInRow;
        for (int i = 0; i < this.getRowsNumber(); i++) {
            HBox hb = new HBox();
            hb.setAlignment(Pos.CENTER);
            hb.setSpacing(5);
            all_zero = true;
            String eq = "";
            colInRow = 0;
            for (int j = 0; j < this.getColsNumber() - 1; j++) {
                if (matrix[i][j] != 0) {
                    eq = ((matrix[i][j] >= 0 ? " +" : " ") + matrix[i][j] + "X" + (j + 1));
                    hb.getChildren().add(new Text(eq));
                    all_zero = false;
                    colInRow++;
                }
            }
            if (all_zero || colInRow > 1) {
                eq = colInRow > 1 ? eq : "0";
                if (all_zero) {
                    hb.getChildren().add(new Text(eq));
                }
                hintsStringbuilder.append("** " + LocaleManager.getManager().get("row")).append(i + 1).append(
                        (matrix[i][this.getColsNumber() - 1] == 0 || colInRow > 1 ?
                        LocaleManager.getManager().get("infiniteSolutions") :
                        LocaleManager.getManager().get("noSolution"))
                );
            }
//            eq += (" = " + matrix[i][this.getColsNumber() - 1]);
            hb.getChildren().addAll(new Text("="), new Text(matrix[i][this.getColsNumber() - 1] + ""));
//            equationsStringbuilder.append(eq).append("\n");
            vb.getChildren().add(hb);

        }
        Label hint = new Label(hintsStringbuilder.toString());
        hint.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        vb.getChildren().add(hint);
        return vb;
    }

    @Override
    public void solveMatrix() {
        if (this.solvedMatrix == null) {
            this.solvedMatrix = this.cloneMatrix();
            this.solveHint = (SolveHint) GaussSolver.solve(this.solvedMatrix);
        }
        String msg = this.solveHint.getStateMessage();

        for (int i = 0; i < this.getRowsNumber(); i++) {
            for (int j = 0; j < this.getColsNumber(); j++) {

            }
        }

        Helper.drawMatrix(newStageDesigner(LocaleManager.getManager().get("matrixInverse") + ": ", this.eqProvider(solvedMatrix), this.solvedMatrix, true, this.getRowsNumber(), this.getColsNumber()), this.solvedMatrix, this.getRowsNumber(), this.getColsNumber(), false, false, null);
        StageProvider.displayStage();
    }

}

class MatrixRow<T extends Number> extends ArrayList<T> {

    private int colNum = 0;

    public double[] toArr() {
        double[] x = new double[this.size()];
        for (int i = 0; i < this.size(); i++) {
            x[i] = this.get(i).doubleValue();
        }
        return x;
    }

    @Override
    public T set(int index, T obj) {
        this.colNum++;
        return super.set(index, obj);
    }


}
