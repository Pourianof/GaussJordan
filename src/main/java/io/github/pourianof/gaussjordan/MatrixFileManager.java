
package io.github.pourianof.gaussjordan;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.layout.Pane;

public class MatrixFileManager implements MatrixProvider {

    private File matrixFile;
    Matrix matrix = new Matrix<Double>();

    public MatrixFileManager(File file) {
        this.matrixFile = file;
    }

    @Override
    public void showMatrix(Pane pane) {
        this.matrix.showMatrix(pane);
    }

    @Override
    public void solveMatrix() {
        this.matrix.solveMatrix();
    }
//    

    @Override
    public void inverseMatrix() throws NotSquareException {
        this.matrix.inverseMatrix();
    }

    @Override
    public void calculateMatrixDeterminant() throws NotSquareException {
        this.matrix.calculateMatrixDeterminant();
    }

    public int getRowsNumber() {
        return this.matrix.getRowsNumber();
    }

    public int getColsNumber() {
        return this.matrix.getColsNumber();
    }

    public void drawMatrix(Pane pane) {
        this.matrix.showMatrix(pane);
    }

    public void extractMatrix() {

        try ( Scanner fileScanner = new Scanner(this.matrixFile);) {
            int rowNum = 0;
            int colNum;
            int colKeeper = 0;
            while (fileScanner.hasNextLine()) {
                Scanner strScanner = new Scanner(fileScanner.nextLine());
                this.matrix.add(new MatrixRow<>());
                colNum = 0;
                while (strScanner.hasNextDouble()) {
                    this.matrix.get(rowNum).add(strScanner.nextDouble());
                    colNum++;
                }
                if (colKeeper == 0) {
                    colKeeper = colNum;
                } else if (colNum != colKeeper) {
                }
                rowNum++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not Founded !!");
        }

    }
}
