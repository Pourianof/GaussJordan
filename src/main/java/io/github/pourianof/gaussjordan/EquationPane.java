package io.github.pourianof.gaussjordan;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.setColumnIndex;
import static javafx.scene.layout.GridPane.setColumnSpan;
import static javafx.scene.layout.GridPane.setRowIndex;
import static javafx.scene.layout.GridPane.setRowSpan;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

class EquationPane extends BorderPane implements MatrixProvider {

    private final ArrayList<EquationRow> rows = new ArrayList<>();
    private Button colAddBtn = null;
    private Button rowAddBtn = null;
    private int eqNumber = 0;
    private int varCount = 0;
    private Matrix<Double> matrix = null;

    GridPane centerGrid = new GridPane();

    public EquationPane() {
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.setStyle();
        this.setHeader();
        centerGrid.setVgap(10);
        this.setAddBtn();
        ScrollPane sp = new ScrollPane(this.centerGrid);
        sp.setStyle("-fx-background: rgb(255,255,255);-fx-background-color: #FFFFFF;");
        sp.setFitToHeight(true);
        sp.setPannable(true);
        this.setCenter(sp);

    }

    private void setHeader() {
        BorderPane topBP = new BorderPane();
        Label title = new Label(LocaleManager.getManager().get("interactiveMatrix"));
        topBP.setPadding(new Insets(10));
        topBP.setCenter(title);
        this.gotoIndexSec(topBP);
        this.matrixWithMultiplication(topBP);
        this.setTop(topBP);

    }

    private void indexWorker(BorderPane topBP, String btnTxt, int index) {
        HBox hb = new HBox();
        Label lbl1 = new Label(LocaleManager.getManager().get("row"));
        TextField rowTF = new TextField();
        VBox vb1 = new VBox();
        vb1.setSpacing(5);
        vb1.setAlignment(Pos.CENTER);
        rowTF.textProperty().addListener(Helper.isNumHandler);
        vb1.getChildren().addAll(lbl1, rowTF);
        rowTF.setPrefColumnCount(2);

        Label lbl2 = new Label(LocaleManager.getManager().get("column"));
        TextField colTF = new TextField();
        VBox vb2 = new VBox();
        vb2.setSpacing(5);
        vb2.setAlignment(Pos.CENTER);
        colTF.textProperty().addListener(Helper.isNumHandler);
        vb2.getChildren().addAll(lbl2, colTF);
        colTF.setPrefColumnCount(2);
        hb.setSpacing(10);
        Button gotoBtn = new Button(btnTxt);
        hb.setAlignment(Pos.BOTTOM_CENTER);
        EventHandler[] ehs = {e -> {
            try {
                this.requestFocusItem(Integer.parseInt(rowTF.getText()), Integer.parseInt(colTF.getText()));
            } catch (Error err) {
            }
        }, e -> {
            this.convertTo(Integer.parseInt(rowTF.getText()), Integer.parseInt(colTF.getText()));

        }
        };

        gotoBtn.setOnAction(ehs[index]);
        hb.getChildren().addAll(vb1, new StackPane(new Text("×")), vb2, gotoBtn);
        if (index == 0) {
            topBP.setLeft(hb);
        } else {
            topBP.setRight(hb);
        }
    }

    private void gotoIndexSec(BorderPane topBP) {
        this.indexWorker(topBP, LocaleManager.getManager().get("goToIdx"), 0);
    }

    private void matrixWithMultiplication(BorderPane topBP) {
        this.indexWorker(topBP,LocaleManager.getManager().get("generate"), 1);
    }

    public void convertTo(int row, int col) {

        if (row < this.eqNumber) {
            for (int i = this.eqNumber; i > row; i--) {
                var er = this.rows.get(i - 1);
                er.deleteRow();
                this.rows.remove(er);
                centerGrid.getChildren().remove(er);
                centerGrid.getChildren().remove(er.label);
            }
            eqNumber = row;
        } else if (row > this.eqNumber) {
            for (int i = this.eqNumber; i < row; i++) {
                this.addNewRow();
            }
        }
        if (col < this.varCount) {
            for (int i = 0; i < this.eqNumber; i++) {
                this.rows.get(i).reduceTo(col);
            }
            this.varCount = col;
        } else if (col > this.varCount) {
            for (int i = this.varCount; i < col; i++) {
                this.addItemInRows();
            }
        }
    }

    private void setStyle() {
        super.setStyle("-fx-padding: 10px; -fx-background-color : white;");
    }


    private void addDefaultCol(EquationRow row, int rowNum) {
        var lbl = new Label("Equation number " + (rowNum + 1) + " :");
        row.setLabel(lbl);
        centerGrid.add(lbl, 0, rowNum);
    }

    public void requestFocusItem(int row, int col) throws Error {
        if (row <= this.eqNumber && col <= this.varCount) {
            this.rows.get(row - 1).requestItemFocus(col - 1);
        } else {
            throw new Error(LocaleManager.getManager().get("outOfBound"));
        }
    }

    private void addItemInRows() {
        ++this.varCount;
        for (int i = 0; i < this.eqNumber; i++) {
            this.rows.get(i).addItem(this.varCount);

        }
        if (this.colAddBtn == null) {
            this.setAddBtn();
        }
        setColumnIndex(this.colAddBtn, this.varCount + 1);
        setColumnSpan(this.rowAddBtn, this.varCount);
    }

    private void addNewRow() {
        EquationRow er = new EquationRow(this.eqNumber, this.varCount);
        this.rows.add(er);
        this.addDefaultCol(er, this.eqNumber);
        this.centerGrid.add(er, 1, this.eqNumber++);
        setRowIndex(this.rowAddBtn, this.eqNumber);
        if (this.colAddBtn != null) {
            //            Pane vb = new Pane();

            setRowSpan(this.colAddBtn, this.eqNumber);
        }
    }

    private void matrixCollector() {
        this.matrix = new Matrix<>();
        for (int i = 0; i < this.eqNumber; i++) {
            matrix.add(this.rows.get(i).getMatrixRow());

        }
    }

    @Override
    public void inverseMatrix() throws NotSquareException {
        if (matrix == null) {
            this.matrixCollector();
        }
        this.matrix.inverseMatrix();

    }

    @Override
    public void solveMatrix() {
        if (matrix == null) {
            this.matrixCollector();
        }
        matrix.solveMatrix();

    }

    @Override
    public void showMatrix(Pane pane
    ) {
    }

    @Override
    public void calculateMatrixDeterminant() throws NotSquareException {
        if (matrix == null) {
            this.matrixCollector();
        }
        this.matrix.calculateMatrixDeterminant();

    }

    final void setAddBtn() {
        if (this.rowAddBtn == null) {
            this.rowAddBtn = new Button("+ Add new ROW");
            this.rowAddBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            this.centerGrid.add(this.rowAddBtn, 1, this.eqNumber + 1);
            setColumnSpan(this.rowAddBtn, (this.varCount > 0 ? this.varCount : 1));
            this.rowAddBtn.setOnAction((e) -> {
                this.addNewRow();
                if (this.colAddBtn == null) {
                    this.setAddBtn();
                }
            });
        }
        if (this.colAddBtn == null && this.eqNumber != 0) {
            this.colAddBtn = new Button("+");
            this.centerGrid.add(this.colAddBtn, this.varCount + 1, 0);
            this.colAddBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            setRowSpan(this.colAddBtn, this.eqNumber > 0 ? this.eqNumber : 1);
            this.colAddBtn.setOnAction((e) -> {
                this.addItemInRows();

            });
        }

    }

}

class EquationRow extends GridPane {

    private ArrayList<TextField> textFields = new ArrayList<>();
    private int rowNum = 0;
    Label label;

    public void setLabel(Label label){
        this.label = label;
    }

    public void deleteRow(int num) {
        int size = this.textFields.size();
        for (int i = size - 1; i >= size - num; i--) {
            this.textFields.remove(i);
            getChildren().remove(i);
        }
    }

    public void deleteRow() {
        this.deleteRow(this.textFields.size());
    }

    private void colsConstruct(int colsNum) {
        for (int i = 1; i <= colsNum; i++) {
            this.addItem(i);
        }
    }

    public EquationRow(int rowNum, int colNumToConstruct) {
        this.rowNum = rowNum;
        this.setHgap(10);
        this.colsConstruct(colNumToConstruct);
    }

    public void reduceTo(int col) {
        int size = this.textFields.size();
        this.deleteRow(size - col);
    }

    public void requestItemFocus(int col) {
        final TextField tf = this.textFields.get(col);
        tf.requestFocus();
        tf.selectAll();

    }

    public MatrixRow<Double> getMatrixRow() {
        MatrixRow<Double> mr = new MatrixRow<>();
        for (int i = 0; i < this.textFields.size(); i++) {
            mr.add(Double.valueOf((this.textFields.get(i).getText())));

        }
        return mr;
    }

    final void addItem(int col) {
        final HBox hb = new HBox();
        final VBox vb = new VBox();
        final TextField tf = new TextField();
        this.textFields.add(tf);
        final Label lbl = new Label("X" + col);
        tf.setPrefColumnCount(3);
        lbl.setPadding(new Insets(5));
        vb.getChildren().add(lbl);
        tf.textProperty().addListener(Helper.isNumHandler);

        hb.getChildren().addAll(tf, lbl);
        this.add(hb, col, 0);

    }

}
