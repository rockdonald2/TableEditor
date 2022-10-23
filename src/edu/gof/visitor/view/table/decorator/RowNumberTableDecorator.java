package edu.gof.visitor.view.table.decorator;

import edu.gof.visitor.view.table.Table;
import edu.gof.visitor.view.table.model.CustomTableModel;
import edu.gof.visitor.view.table.model.decorator.RowNumberedTableModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class RowNumberTableDecorator extends TableDecorator {

    private String[][] data;
    private String[] headers;

    public RowNumberTableDecorator(Table table) {
        super(table);
    }

    @Override
    public void displayData(CustomTableModel tableModel) {
        getData(tableModel);
        addRowNumbers();
        super.displayData(this.constructModel(data, headers));
    }

    private void getData(CustomTableModel tableModel) {
        this.headers = new String[tableModel.getColumnCount()];
        this.data = new String[tableModel.getRowCount()][this.headers.length];

        IntStream.range(0, this.headers.length)
                .forEach(idx -> headers[idx] = tableModel.getColumnName(idx));
        IntStream.range(0, this.data.length)
                .forEach(rowIdx -> {
                    IntStream.range(0, this.headers.length)
                            .forEach(colIdx -> {
                                this.data[rowIdx][colIdx] = tableModel.getValueAt(rowIdx, colIdx).toString();
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
    public CustomTableModel constructModel(String[][] data, String[] headers) {
        final CustomTableModel tableModel = new RowNumberedTableModel(super.constructModel(data, headers));
        tableModel.setDataVector(data, headers);
        return tableModel;
    }

}
