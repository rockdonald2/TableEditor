package edu.gof.visitor.controller;

import edu.gof.visitor.panel.MainPanel;
import edu.gof.visitor.service.exception.ServiceException;
import edu.gof.visitor.service.export.Exporter;
import edu.gof.visitor.service.export.json.JsonExporter;
import edu.gof.visitor.service.loader.Data;
import edu.gof.visitor.service.loader.Importer;
import edu.gof.visitor.service.loader.csv.CsvData;
import edu.gof.visitor.service.loader.csv.CsvImporter;
import edu.gof.visitor.utils.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class MainController {

    private static final Logger log = Logger.getLogger(MainController.class.getName());

    private final MainPanel mainPanel;
    private final Importer importer;

    private Data data;

    public MainController() {
        log.info("Initialized MainController");

        this.data = new CsvData();
        this.mainPanel = new MainPanel(this);
        this.importer = new CsvImporter();
    }

    public void doImportData(String filePath) {
        try {
            this.data = importData(filePath);
            doDisplayData();
            mainPanel.showInfo("Successfully imported data");
        } catch (ServiceException e) {
            log.severe(String.format("Exception occurred at importing data: %s", e.getMessage()));
            showError(e.getMessage());
        }
    }

    public void doDisplayData() {
        mainPanel.displayData(data);
    }

    private void showError(String message) {
        mainPanel.showError(message);
    }

    private Data importData(String filePath) throws ServiceException {
        return importer.importData(filePath);
    }

    public void doExportData() {
        try {
            Optional<File> optionalSaveFile = mainPanel.saveData();

            if (optionalSaveFile.isEmpty()) return;

            final File saveFile = optionalSaveFile.get();

            final Optional<String> extension = Util.getExtensionByStringHandling(saveFile.getName());

            if (extension.isEmpty()) {
                showError(String.format("Failed to find extension of file %s", saveFile.getName()));
                return;
            }

            final String data = exportData(extension.get());

            try {
                Files.writeString(saveFile.toPath(), data);
                mainPanel.showInfo("Successfully exported data");
            } catch (IOException e) {
                throw new ServiceException("Failed to save data", e);
            }
        } catch (ServiceException e) {
            log.severe(String.format("Exception occurred while exporting data: %s", e.getMessage()));
            showError(e.getMessage());
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

    public void doDeleteRowAt(int rowIdx) {
        data.getData().remove(rowIdx);
        doDisplayData();
    }

    public void doDeleteColumnAt(int columnIdx) {
        List<String> headers = data.getHeaders();
        List<List<String>> rowData = data.getData();

        rowData.forEach(row -> row.remove(columnIdx));
        headers.remove(columnIdx);

        doDisplayData();
    }

}
