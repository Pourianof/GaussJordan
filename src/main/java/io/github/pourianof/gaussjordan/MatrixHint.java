/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.pourianof.gaussjordan;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 *
 * @author Pooriana
 */
public interface MatrixHint {

    public void addZeroRow(int index);

    public ArrayList<Integer> getZeroRows();
}

class InverseHint implements MatrixHint {

    final ArrayList<Integer> zeroRows = new ArrayList<>();
//    final ArrayList<Integer> errorCol = new ArrayList<>();

    public boolean getInvertible() {
        return !zeroRows.isEmpty();
    }

    @Override
    public void addZeroRow(int index) {
        this.zeroRows.add(index);
    }

    @Override
    public ArrayList<Integer> getZeroRows() {
        return this.zeroRows;

    }
}

enum SolveState {
    ONE, MANY, NO
}

class SolveHint implements MatrixHint {

    private final ArrayList<Integer> zeroRows = new ArrayList<>();
    private final ArrayList<Double> constVal = new ArrayList<>();

    @Override
    public void addZeroRow(int index) {
        this.zeroRows.add(index);
    }

    @Override
    public ArrayList<Integer> getZeroRows() {
        return this.zeroRows;

    }

    public String getStateMessage() {
        if (zeroRows.isEmpty()) {
            return LocaleManager.getManager().get("solve1Hint");
        } else if(constVal.get(0) == 0){
            return LocaleManager.getManager().get("solve2Hint") + zeroRows.get(0);
        }else{
            return MessageFormat.format(LocaleManager.getManager().get("solve3Hint"), constVal.get(0));
        }
    }

    public void addConst(double val) {
        this.constVal.add(val);
    }
}

class DeterminantHints implements MatrixHint {

    private final ArrayList<Integer> zeroRows = new ArrayList<>();
    private final ArrayList<Integer> zeroCols = new ArrayList<>();

    @Override
    public void addZeroRow(int index) {
        this.zeroRows.add(index);
    }

    @Override
    public ArrayList<Integer> getZeroRows() {
        return this.zeroRows;

    }
}
