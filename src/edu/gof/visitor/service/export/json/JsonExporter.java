package edu.gof.visitor.service.export.json;

import edu.gof.visitor.model.Field;
import edu.gof.visitor.model.TextField;
import edu.gof.visitor.service.export.ExportVisitor;
import edu.gof.visitor.service.export.Exporter;
import edu.gof.visitor.utils.Converters;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonExporter extends Exporter {

    @Override
    public ExportVisitor exportVisitor() {
        return new JsonExportVisitor();
    }

    @Override
    public void exportLogic(StringBuilder exportedData, List<String> headers, List<List<String>> rowData, ExportVisitor exporterVisitor) {
        AtomicInteger fieldIdx = new AtomicInteger(0);
        AtomicInteger elemIdx = new AtomicInteger(0);

        exportedData.append("[");
        rowData.forEach(row -> {
            exportedData.append("{");
            fieldIdx.set(0);

            row.forEach(value -> {
                String key = headers.get(fieldIdx.get());
                Optional<Field> field;

                field = Converters.tryParseNumberField(key, value);

                if (field.isEmpty()) {
                    field = Converters.tryParseDecimalField(key, value);
                }

                if (field.isEmpty()) {
                    field = Optional.of(new TextField(key, value));
                }

                exportedData.append(field.get().accept(exporterVisitor));

                if (fieldIdx.get() < (headers.size() - 1)) {
                    exportedData.append(",");
                }

                fieldIdx.getAndIncrement();
            });

            exportedData.append("}");

            if (elemIdx.get() < (rowData.size() - 1)) {
                exportedData.append(",");
            }

            elemIdx.incrementAndGet();
        });

        exportedData.append("]");
    }
}
