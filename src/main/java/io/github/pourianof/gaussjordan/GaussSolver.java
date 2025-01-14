package io.github.pourianof.gaussjordan;

public class GaussSolver {

    public static MatrixHint determinantCalc(double[][] matrix, double[] determinant_coe) {
        return gaussCalculator(matrix, false, true, null, determinant_coe);
    }

    static private MatrixHint gaussCalculator(double[][] matrix, boolean isInverse, boolean isDeterminant, double[][] inversePart, double[] determinant_coe) {

        int matrix_cols = matrix[0].length;
        int matrix_rows = matrix.length;
        double division_coe = 1.0;
        double[] to_zero = new double[matrix_rows];
        int start_col = 0;
        int det = 0;
        MatrixHint hint = null;

        int[] sameDegRows = null;
        if (isDeterminant) {
            boolean[] deletedRowsIndex = new boolean[matrix_rows];
            for (int i = 0; i < matrix_rows; i++) {
                sameDegRows = new int[matrix_rows - i];
                sameDegRows[0] = -1;
                boolean isAllZero = true;
                int sameCounter = 0;
                for (int j = 0, h = 0; j < matrix_rows; j++) {
                    if (deletedRowsIndex[j]) {
                        continue;
                    }
                    if (matrix[j][i] != 0) {
                        sameDegRows[h] = j;
                        h++;
                        sameCounter++;
                        isAllZero = false;
                    }
                }
                if (isAllZero) {
                    determinant_coe[0] = 0;
                    break;
                }

                int baseRow = sameDegRows[0];
                deletedRowsIndex[baseRow] = true;

                if (sameCounter == 1) {
                    determinant_coe[i] = matrix[baseRow][i];
                    continue;
                }

                determinant_coe[i] = division_coe = matrix[baseRow][i];
                for (int j = i; j < matrix_cols; j++) {
                    matrix[baseRow][j] /= division_coe;
                    for (int k = 1; k < sameCounter; k++) {
                        int r = sameDegRows[k];
                        if (j == i) {
                            to_zero[r] = matrix[r][j];
                        }
                        matrix[r][j] -= (to_zero[r] * matrix[i][j]);
                    }
                }

            }

        } else {
            if (isInverse) {
                hint = new InverseHint();
            } else {
                hint = new SolveHint();
            }
            int colNum = isInverse ? matrix_cols * 2 : matrix_cols;
            int[] coveredCols = null;
            if (isInverse) {
                coveredCols = new int[matrix_cols];
            }
            int numberOfCoveredCols = 0;
            for (int i = 0; i < matrix_rows; i++) {
                start_col = 0;
                division_coe = matrix[i][start_col];
                for (int j = 0; j < colNum; j++) {
                    if (division_coe == 0) {
                        start_col = j + 1;
                        if (start_col >= matrix_cols) {
                            hint.addZeroRow(i);
                            if (!isInverse) {
                                ((SolveHint) hint).addConst(matrix[i][matrix_cols - 1]);
                            }
                            break;
                        }
                        division_coe = matrix[i][start_col];
                        continue;
                    }
//                    coveredColsBool[start_col] = true;
                    numberOfCoveredCols++;
                    if (start_col == colNum - 1) {
                        break;
                    }
                    double[][] mat = (isInverse && j >= matrix_cols ? inversePart : matrix);
                    int currentCol = j % matrix_cols;
                    mat[i][currentCol] /= division_coe;

                    int row = ((matrix_rows - 1) == i) ? 0 : i + 1;
                    for (int k = 0, l = row; k < matrix_rows - 1; k++, l++) {
                        if (j == start_col) {
                            to_zero[l] = matrix[l][start_col];
                        }
                        if (to_zero[l] == 0) {
                            l = (l == matrix_rows - 1) ? -1 : l;
                            continue;
                        }
                        mat[l][currentCol] -= (to_zero[l] * mat[i][currentCol]);

                        if (l == matrix_rows - 1) {
                            l = -1;
                        }
                    }

                }
            }
        }
        return hint;

    }

    public static MatrixHint solve(double[][] matrix) {
        return GaussSolver.gaussCalculator(matrix, false, false, null, null);
    }

    public static MatrixHint inverseMatrix(double[][] matrix, double[][] inversePart) {
        return GaussSolver.gaussCalculator(matrix, true, false, inversePart, null);
    }

    public static MatrixHint determinantCalculate(double[][] matrix, double[] determinantCoes) {
        return GaussSolver.gaussCalculator(matrix, false, true, null, determinantCoes);
    }

}
