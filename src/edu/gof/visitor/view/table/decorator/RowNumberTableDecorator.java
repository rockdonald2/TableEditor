package edu.gof.visitor.view.table.decorator;

import edu.gof.visitor.command.PositionBasedCommand;
import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;
import edu.gof.visitor.view.table.Table;
import edu.gof.visitor.view.table.model.CustomTableModel;
import edu.gof.visitor.view.table.model.decorator.RowNumberedTableModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class RowNumberTableDecorator extends ProtectedFieldTableDecorator {

    private String[][] data;
    private String[] headers;
    private boolean rowNumbersAdded;
    private boolean resetCalled;

    public RowNumberTableDecorator(Table table) {
        super(table);
    }

    @Override
    public Position getProtectedPosition() {
        return new Position(-1, 0);
    }

    @Override
    public void displayData(CustomTableModel tableModel) {
        getData(tableModel);

        if (!rowNumbersAdded) {
            addRowNumbers();
            rowNumbersAdded = true;
        } else {
            updateRowNumbers();
        }

        super.displayData(this.constructModel(data, headers));
    }

    private void getData(CustomTableModel tableModel) {
        this.headers = new String[tableModel.getColumnCount()];
        this.data = new String[tableModel.getRowCount()][this.headers.length];

        IntStream.range(0, this.headers.length)
                .forEach(idx -> headers[idx] = tableModel.getColumnName(idx));
        IntStream.range(0, this.data.length)
                .forEach(rowIdx -> IntStream.range(0, this.headers.length)
                        .forEach(colIdx -> this.data[rowIdx][colIdx] = tableModel.getValueAt(rowIdx, colIdx).toString()));
    }

    private void addRowNumbers() {
        if ("#".equals(MainController.instance().getColumnNameAt(0))) {
            throw new IllegalStateException("Illegal to re-add row numbers");
        }

        List<String> tmpHeaders = new ArrayList<>(Arrays.stream(headers).toList());
        List<List<String>> tmpData = new ArrayList<>(Arrays.stream(data)
                .toList()
                .stream()
                .map(elem -> new ArrayList<>(Arrays.stream(elem).toList()))
                .toList());

        tmpHeaders.add(0, "#");
        IntStream.range(0, tmpData.size())
                .forEach(idx -> tmpData.get(idx).add(0, String.valueOf(idx + 1)));

        final MainController controller = MainController.instance();

        this.headers = tmpHeaders.toArray(new String[]{});
        controller.setHeaders(this.headers);

        this.data = new String[tmpData.size()][tmpHeaders.size()];
        IntStream.range(0, tmpData.size())
                .forEach(idx -> {
                    RowNumberTableDecorator.this.data[idx] = tmpData.get(idx).toArray(new String[]{});
                    controller.addValueAt(idx, 0, tmpData.get(idx).get(0));
                });

        controller.updateCommands(PositionBasedCommand.Orientation.INCREASE, PositionBasedCommand.Which.COLUMN, 1);
    }

    private void updateRowNumbers() {
        if (resetCalled) {
            return;
        } else if (!"#".equals(MainController.instance().getColumnNameAt(0))) {
            throw new IllegalStateException("Illegal to update non-existing row numbers");
        }

        List<List<String>> tmpData = new ArrayList<>(Arrays.stream(data)
                .toList()
                .stream()
                .map(elem -> new ArrayList<>(Arrays.stream(elem).toList()))
                .toList());

        IntStream.range(0, tmpData.size())
                .forEach(idx -> tmpData.get(idx).set(0, String.valueOf(idx + 1)));

        final MainController controller = MainController.instance();

        this.data = new String[tmpData.size()][headers.length];
        IntStream.range(0, tmpData.size())
                .forEach(idx -> {
                    RowNumberTableDecorator.this.data[idx] = tmpData.get(idx).toArray(new String[]{});
                    controller.setValueAt(idx, 0, tmpData.get(idx).get(0));
                });
    }

    @Override
    public CustomTableModel constructModel(String[][] data, String[] headers) {
        final CustomTableModel tableModel = new RowNumberedTableModel(super.constructModel(data, headers));
        tableModel.setDataVector(data, headers);
        return tableModel;
    }

    @Override
    public void resetModel() {
        final MainController controller = MainController.instance();

        if ("#".equals(controller.getColumnNameAt(0))) {
            resetCalled = true;

            controller.doDeleteColumnAt(0);
            controller.updateCommands(PositionBasedCommand.Orientation.DECREASE, PositionBasedCommand.Which.COLUMN, 1);

            rowNumbersAdded = false;
            resetCalled = false;
        }
    }

}
