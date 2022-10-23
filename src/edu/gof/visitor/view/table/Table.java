package edu.gof.visitor.view.table;

import javax.swing.*;
import javax.swing.table.TableModel;

public interface Table {

    JTable getComponent();

    void displayData(TableModel tableModel);

    TableModel constructModel(String[][] data, String[] headers);

}
