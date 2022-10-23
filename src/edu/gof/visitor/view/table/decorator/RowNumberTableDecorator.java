package edu.gof.visitor.view.table.decorator;

import edu.gof.visitor.command.EditCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;
import edu.gof.visitor.view.table.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class RowNumberTableDecorator extends TableDecorator {

    private String[][] data;
    private String[] headers;
    private TableModel tableModel;

    public RowNumberTableDecorator(Table table) {
        super(table);
    }

    @Override
    public void displayData(TableModel tableModel) {
        this.tableModel = tableModel;

        getData();
        addRowNumbers();
        super.displayData(constructModel(this.data, this.headers));
    }

    private void getData() {
        this.headers = new String[this.tableModel.getColumnCount()];
        this.data = new String[this.tableModel.getRowCount()][this.headers.length];

        IntStream.range(0, this.headers.length)
                .forEach(idx -> headers[idx] = this.tableModel.getColumnName(idx));
        IntStream.range(0, this.data.length)
                .forEach(rowIdx -> {
                    IntStream.range(0, this.headers.length)
                            .forEach(colIdx -> {
                                this.data[rowIdx][colIdx] = this.tableModel.getValueAt(rowIdx, colIdx).toString();
                            });
                });
    }

    private void addRowNumbers() {
        List<String> tmpHeaders = new ArrayList<>(Arrays.stream(headers).toList());
        List<List<String>> tmpData = new ArrayList<>(Arrays.stream(data)
                .toList()
                .stream()
                .map(elem -> new ArrayList<>(Arrays.stream(elem).toList()))
                .toList());

        tmpHeaders.add(0, "#");
        IntStream.range(0, tmpData.size())
                .forEach(idx -> tmpData.get(idx).add(0, String.valueOf(idx + 1)));

        this.headers = tmpHeaders.toArray(new String[]{});
        this.data = new String[tmpData.size()][tmpHeaders.size()];
        IntStream.range(0, tmpData.size())
                .forEach(idx -> {
                    RowNumberTableDecorator.this.data[idx] = tmpData.get(idx).toArray(new String[]{});
                });
    }

    @Override
    public TableModel constructModel(String[][] data, String[] headers) {
        return new DefaultTableModel(data, headers) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                MainController.instance().executeCommand(new EditCommand(MainController.instance(), (JTable) RowNumberTableDecorator.this.getComponent(), aValue.toString(), new Position(row, column - 1)));
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
    }

}
