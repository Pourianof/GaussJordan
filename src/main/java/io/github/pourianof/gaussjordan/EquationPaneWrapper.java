package io.github.pourianof.gaussjordan;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.MessageFormat;

class ButtonData {

    final private int index;

    public ButtonData(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}

public class EquationPaneWrapper extends BorderPane {

    private BorderPane[] tabs = {new EquationPane(), new ImportFilePane()};
    private int screenState = 0;

    private void setTab(int index) {
        if (index != this.screenState) {
            this.setCenter(this.tabs[index]);
        }
        this.screenState = index;
    }

    private void setSolverButtons() {
        BorderPane bottomPane = new BorderPane();
        Button determinantCalculator = new Button(LocaleManager.getManager().get("calcDeter"));
        determinantCalculator.setUserData(new ButtonData(0));
        Button inverseSystem = new Button(LocaleManager.getManager().get("calcInverse"));
        inverseSystem.setUserData(new ButtonData(1));
        Button solveSystem = new Button(LocaleManager.getManager().get("solveMatrix"));
        solveSystem.setUserData(new ButtonData(2));
        HBox hb = new HBox(solveSystem, inverseSystem, determinantCalculator);
        hb.setSpacing(10);
        hb.setPadding(new Insets(5));
        hb.setAlignment(Pos.CENTER);
        VBox vb = new VBox(hb);
        vb.setSpacing(10);
        vb.setPadding(new Insets(5));
        vb.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        bottomPane.setTop(vb);
        this.setBottom(bottomPane);

        EventHandler<ActionEvent> eh = e -> {
            try {
                Button btn = (Button) e.getTarget();
                int index = ((ButtonData) btn.getUserData()).getIndex();
                MatrixProvider tab = ((MatrixProvider) this.tabs[this.screenState]);
                Label errorMsgLbl = new Label();

                switch (index) {
                    case 0: {
                        try {
                            tab.calculateMatrixDeterminant();

                        } catch (NotSquareException err) {
                            errorMsgLbl.setText("**** " + "* " + MessageFormat.format(LocaleManager.getManager().get("squareErr")," * " + err.getRow() + " * ", err.getCol() + " * " ) );
                            vb.getChildren().add(errorMsgLbl);
                        }

                    }
                    break;
                    case 2: {
                        tab.solveMatrix();
//                        tab.showMatrix(vb);
                    }
                    break;
                    case 1: {
                        try {
                            tab.inverseMatrix();
                        } catch (NotSquareException err) {
                            errorMsgLbl.setText("**** " + "* " + MessageFormat.format(LocaleManager.getManager().get("squareErr")," * " + err.getRow() + " * ", err.getCol() + " * " ) );
                            vb.getChildren().add(errorMsgLbl);

                        }
                    }
                }

            } catch (ClassCastException er) {
            }
        };
        solveSystem.setOnAction(eh);
        inverseSystem.setOnAction(eh);
        determinantCalculator.setOnAction(eh);
    }

    private void setHeader() {

        HBox hb = new HBox();
        hb.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        EventHandler<ActionEvent> eh = e -> {
            Button btn = (Button) e.getSource();
            btn.setStyle("-fx-stroke-width:2 ; -fx-stroke-color:black");
            int index = ((ButtonData) btn.getUserData()).getIndex();
            this.setTab(index);
        };
        Button fileImportSectionBtn = new Button(LocaleManager.getManager().get("matrixFromFile"));
        Button interactiveMatrixBtn = new Button(LocaleManager.getManager().get("matrixMan"));
        fileImportSectionBtn.setUserData(new ButtonData(1));
        interactiveMatrixBtn.setUserData(new ButtonData(0));
        fileImportSectionBtn.minWidthProperty().bind(hb.widthProperty().divide(2));
        interactiveMatrixBtn.minWidthProperty().bind(hb.widthProperty().divide(2));
        hb.getChildren().addAll(interactiveMatrixBtn, fileImportSectionBtn);
        fileImportSectionBtn.setOnAction(eh);
        interactiveMatrixBtn.setOnAction(eh);
        this.setTop(hb);
    }

    public EquationPaneWrapper() {

        this.setStyle("-fx-background-color:#FFF");
        this.setCenter(this.tabs[this.screenState]);
        this.setHeader();
        this.setSolverButtons();
    }

}
