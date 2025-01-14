
package io.github.pourianof.gaussjordan;

import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImportFilePane extends BorderPane implements MatrixProvider {
    
    private MatrixFileManager fileManager = null;
    private Pane matrixPane = new Pane();
    private VBox rightVerticalBox = null;
    
    public ImportFilePane() {
        this.rightSetter();
        this.leftSetter();
        this.setMinHeight(200);
        
    }
    
    private void leftSetter() {
        
        Label titleLbl = new Label( LocaleManager.getManager().get("matrixFilePreview") + ":");
        
        this.matrixPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        VBox vb = new VBox(titleLbl, this.matrixPane);
        
        matrixPane.minHeightProperty().bind(vb.heightProperty().subtract(titleLbl.heightProperty()).subtract(30));
        
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(20);
        vb.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        vb.minWidthProperty().bind(this.widthProperty().divide(3).multiply(2));
        vb.maxWidthProperty().bind(this.widthProperty().divide(3).multiply(2));
        this.setLeft(vb);
        
    }
    
    @Override
    public void inverseMatrix() throws NotSquareException {
        this.fileManager.inverseMatrix();
    }
    
    @Override
    public void solveMatrix() {
        this.fileManager.solveMatrix();
    }
    
    @Override
    public void showMatrix(Pane pane) {
        this.fileManager.showMatrix(pane);
    }
    
    @Override
    public void calculateMatrixDeterminant() throws NotSquareException {
        this.fileManager.calculateMatrixDeterminant();
        
    }
    
    private void rightSetter() {
        Stage parentStage = StageProvider.getRootStage();
        Button importBtn = new Button(LocaleManager.getManager().get("insertFileBtn"));
        Text rowNumTxt = new Text("");
        Text colNumTxt = new Text("");
        
        Label fileNameLbl = new Label("");
        
        final FileChooser fc = new FileChooser();
        fc.setTitle("Import File");
        fc.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        
        GridPane gp = new GridPane();
        gp.add(new Label(LocaleManager.getManager().get("rowNumber") + ":"), 0, 0);
        gp.add(rowNumTxt, 1, 0);
        gp.add(new Label(LocaleManager.getManager().get("colNumber") + ":"), 0, 1);
        gp.add(colNumTxt, 1, 1);
        gp.setPadding(new Insets(20));
        gp.setVgap(10);
        gp.setHgap(10);
        
        this.rightVerticalBox = new VBox(importBtn, gp);
        this.rightVerticalBox.setSpacing(20);
        this.rightVerticalBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        this.rightVerticalBox.setAlignment(Pos.CENTER);
        this.rightVerticalBox.minWidthProperty().bind(this.widthProperty().divide(3));
        this.rightVerticalBox.maxWidthProperty().bind(this.widthProperty().divide(3));
        this.setRight(this.rightVerticalBox);
        this.rightVerticalBox.getChildren().add(1, fileNameLbl);
        fileNameLbl.setVisible(false);
        importBtn.setOnAction(e -> {
            Stage stage = new Stage();
            stage.initOwner(parentStage);
//            stage.setResizable(false);
            File file = fc.showOpenDialog(parentStage);
            if (file != null) {
//                fileNameLbl.setText(file.getName());
                this.fileManager = new MatrixFileManager(file);
                this.fileManager.extractMatrix();
//                this.fileManager.showMatrix();
                fileNameLbl.setText(file.getName());
                int rows = this.fileManager.getRowsNumber();
                int cols = this.fileManager.getColsNumber();
                rowNumTxt.setText("" + rows);
                colNumTxt.setText("" + cols);
                if (!fileNameLbl.isVisible()) {
                    fileNameLbl.setVisible(true);
                }
                this.matrixPane.getChildren().clear();
                this.fileManager.showMatrix(this.matrixPane);
            }
        });
        
    }

}
