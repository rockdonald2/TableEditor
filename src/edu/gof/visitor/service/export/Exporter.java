package edu.gof.visitor.service.export;

import edu.gof.visitor.model.Data;
import edu.gof.visitor.model.Field;
import edu.gof.visitor.service.exception.ServiceException;

import java.util.List;

public abstract class Exporter {

    public final String exportData(Data data) {
        StringBuilder exportedData = new StringBuilder();
        exportLogic(exportedData, data.getHeaders(), data.getData(), exportVisitor());
        return exportedData.toString();
    }

    public abstract ExportVisitor exportVisitor();

    public abstract Field parseField(String key, String value) throws ServiceException;

    public abstract void exportLogic(StringBuilder exportedData, List<String> headers, List<List<String>> rowData, ExportVisitor exporterVisitor);

}
