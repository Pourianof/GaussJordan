
package io.github.pourianof.gaussjordan;

import javafx.scene.layout.Pane;


public interface MatrixProvider {

    void showMatrix(Pane pane);

    void solveMatrix();

    void inverseMatrix() throws NotSquareException;
//

    void calculateMatrixDeterminant() throws NotSquareException;
}
