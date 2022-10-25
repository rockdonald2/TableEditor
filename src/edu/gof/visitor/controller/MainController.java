package edu.gof.visitor.controller;

import edu.gof.visitor.command.Command;
import edu.gof.visitor.command.PositionBasedCommand;
import edu.gof.visitor.model.CsvData;
import edu.gof.visitor.model.Data;
import edu.gof.visitor.model.Position;
import edu.gof.visitor.service.exception.ServiceException;
import edu.gof.visitor.service.export.Exporter;
import edu.gof.visitor.service.export.json.JsonExporter;
import edu.gof.visitor.service.loader.Importer;
import edu.gof.visitor.service.loader.csv.CsvImporter;
import edu.gof.visitor.utils.Util;
import edu.gof.visitor.view.MainPanel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public final class MainController {

    private static final Logger log = Logger.getLogger(MainController.class.getName());

    private static MainController instance;

    private final Deque<Command<?, ?>> commands = new ArrayDeque<>();
    private final Deque<Command<?, ?>> undoCommands = new ArrayDeque<>();

    private MainPanel mainPanel;
    private Data data;
    private boolean initialized;

    private MainController() {
    }

    public static synchronized MainController instance() {
        if (instance == null) {
            instance = new MainController();
        }

        return instance;
    }

    public void init() {
        if (initialized) {
            throw new IllegalStateException(String.format("%s already initialized", MainPanel.class.getName()));
        }

        log.info("Initialized MainController");

        this.data = new CsvData();
        this.mainPanel = MainPanel.instance();
        this.mainPanel.init();
        initialized = true;
    }

    public int getColumns() {
        return data.getHeaders().size();
    }

    public int getRows() {
        return data.getData().size();
    }

    public void doImportData(String filePath) {
        try {
            this.data = importData(filePath);
            doDisplayData();
            mainPanel.showInfo("Successfully imported data");
        } catch (ServiceException e) {
            log.severe(String.format("Exception occurred at importing data: %s", e.getMessage()));
            mainPanel.showError(e.getMessage());
        }
    }

    private Data importData(String filePath) throws ServiceException {
        final Optional<String> extension = Util.getExtensionByStringHandling(filePath);

        if (extension.isEmpty()) {
            throw new ServiceException("Failed to deduce extension from file name while importing data");
        }

        Importer importer = null;
        if ("csv".equals(extension.get())) {
            importer = new CsvImporter();
        }

        if (importer == null) {
            throw new ServiceException("Failed to obtain importer");
        }

        return importer.importData(filePath);
    }

    public void doDisplayData() {
        mainPanel.displayData(data);
    }

    public void doExportData() {
        try {
            Optional<File> optionalSaveFile = mainPanel.saveData();

            if (optionalSaveFile.isEmpty()) return;

            final File saveFile = optionalSaveFile.get();

            final Optional<String> extension = Util.getExtensionByStringHandling(saveFile.getName());

            if (extension.isEmpty()) {
                mainPanel.showError(String.format("Failed to find extension of file %s", saveFile.getName()));
                return;
            }

            final String dataStr = exportData(extension.get());

            try { // NOSONAR; intentionally
                Files.writeString(saveFile.toPath(), dataStr);
                mainPanel.showInfo("Successfully exported data");
            } catch (IOException e) {
                throw new ServiceException("Failed to save data", e);
            }
        } catch (ServiceException e) {
            log.severe(String.format("Exception occurred while exporting data: %s", e.getMessage()));
            mainPanel.showError(e.getMessage());
        }
    }

    private String exportData(String extension) throws ServiceException {
        Exporter exporter = null;

        if ("json".equals(extension)) {
            exporter = new JsonExporter();
        }

        if (exporter == null) {
            throw new ServiceException("Failed to export data because no suitable exporter has been found");
        }

        return exporter.exportData(data);
    }

    public void doModifyValueAt(int row, int column, String value) {
        List<List<String>> rowData = data.getData();
        rowData.get(row).set(column, value);
    }

    public void doAddNewRow() {
        final List<String> newRow = new ArrayList<>(data.getHeaders().size());
        IntStream.range(0, data.getHeaders().size()).forEach(idx -> newRow.add(""));

        data.getData().add(newRow);
        doDisplayData();
    }

    public void doAddNewColumn(String columnName) {
        List<List<String>> rowData = data.getData();
        List<String> headers = data.getHeaders();

        rowData.forEach(row -> row.add(""));
        headers.add(columnName);

        doDisplayData();
    }

    public List<String> doDeleteRowAt(int rowIdx) {
        List<String> lostData = data.getData().remove(rowIdx);
        doDisplayData();

        return lostData;
    }

    public List<String> doDeleteColumnAt(int columnIdx) {
        List<String> headers = data.getHeaders();
        List<List<String>> rowData = data.getData();

        List<String> lostData = new ArrayList<>();
        rowData.forEach(row -> lostData.add(row.remove(columnIdx)));
        headers.remove(columnIdx);

        doDisplayData();

        return lostData;
    }

    public void doAddRowAt(int rowIdx, List<String> rowData) {
        this.data.getData().add(rowIdx, rowData);
        doDisplayData();
    }

    public void doAddColumnAt(int colIdx, List<String> colData, String colName) {
        this.data.getHeaders().add(colIdx, colName);

        AtomicInteger idx = new AtomicInteger();
        this.data.getData().forEach(row -> row.add(colIdx, colData.get(idx.getAndIncrement())));
        doDisplayData();
    }

    public String getValueAt(Position position) {
        return data.getData().get(position.getRow()).get(position.getColumn());
    }

    public String getColumnNameAt(int col) {
        return data.getHeaders().get(col);
    }

    public void setHeaders(String[] headers) {
        data.setHeaders(new ArrayList<>(List.of(headers)));
    }

    public void addValueAt(int rowIdx, int colIdx, String value) {
        data.getData().get(rowIdx).add(colIdx, value);
    }

    public void setValueAt(int rowIdx, int colIdx, String value) {
        data.getData().get(rowIdx).set(colIdx, value);
    }

    public void executeCommand(Command<?, ?> command) {
        commands.push(command);
        command.execute();

        while (!undoCommands.isEmpty()) undoCommands.pop();
    }

    public void undoCommand() {
        try {
            Command<?, ?> undo = commands.pop();
            undoCommands.push(undo);
            undo.unexecute();
        } catch (RuntimeException ignored) {
            // ignored
        }
    }

    public void redoCommand() {
        try {
            Command<?, ?> redo = undoCommands.pop();
            commands.push(redo);
            redo.execute();
        } catch (RuntimeException ignored) {
            // ignored
        }
    }

    public void updateCommands(PositionBasedCommand.Orientation orientation, PositionBasedCommand.Which which, int offset) {
        commands.iterator().forEachRemaining(cmd -> {
            if (!(cmd instanceof final PositionBasedCommand<?, ?> positionBasedCmd)) return;

            switch (orientation) {
                case INCREASE -> {
                    switch (which) {
                        case ROW -> positionBasedCmd.getEditPosition().setRow(positionBasedCmd.getEditPosition().getRow() + offset);
                        case COLUMN -> positionBasedCmd.getEditPosition().setColumn(positionBasedCmd.getEditPosition().getColumn() + offset);
                    }
                }
                case DECREASE -> {
                    switch (which) {
                        case ROW -> positionBasedCmd.getEditPosition().setRow(positionBasedCmd.getEditPosition().getRow() - offset);
                        case COLUMN -> positionBasedCmd.getEditPosition().setColumn(positionBasedCmd.getEditPosition().getColumn() - offset);
                    }
                }
            }
        });
    }

}
