package edu.gof.visitor.controller;

import edu.gof.visitor.panel.MainPanel;
import edu.gof.visitor.panel.tabel.Field;
import edu.gof.visitor.panel.tabel.TextField;
import edu.gof.visitor.service.exception.ServiceException;
import edu.gof.visitor.service.export.ExportVisitor;
import edu.gof.visitor.service.export.json.JsonExportVisitor;
import edu.gof.visitor.service.loader.Data;
import edu.gof.visitor.service.loader.Importer;
import edu.gof.visitor.service.loader.csv.CsvData;
import edu.gof.visitor.service.loader.csv.CsvImporter;
import edu.gof.visitor.utils.Converters;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class MainController {

    private static final Logger log = Logger.getLogger(MainController.class.getName());

    private final MainPanel mainPanel;
    private final Importer importer;
    private final ExportVisitor exporter;

    private Data data;

    public MainController() {
        log.info("Initialized MainController");

        this.data = new CsvData();
        this.mainPanel = new MainPanel(this);
        this.importer = new CsvImporter();
        this.exporter = new JsonExportVisitor();
    }

    public void doImportData(String filePath) {
        try {
            this.data = importData(filePath);
            doDisplayData();
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
            mainPanel.saveData();
        } catch (ServiceException e) {
            log.severe(String.format("Exception occurred while exporting data: %s", e.getMessage()));
            showError(e.getMessage());
        }
    }

    public String exportData(String extension) {
        List<String> headers = data.getHeaders();
        List<List<String>> rowData = data.getData();

        StringBuilder exportedData = new StringBuilder();

        AtomicInteger idx = new AtomicInteger();
        rowData.forEach(row -> {
            exportedData.append("{");
            idx.set(0);

            row.forEach(value -> {
                String key = headers.get(idx.get());

                Optional<Field> field = Converters.tryParseNumberField(key, value);

                if (field.isEmpty()) {
                    field = Optional.of(new TextField(key, value));
                }

                exportedData.append(field.get().accept(exporter));

                if (idx.get() < (headers.size() - 1)) {
                    exportedData.append(",");
                }

                idx.getAndIncrement();
            });

            exportedData.append("}");
        });

        return exportedData.toString();
    }

}
