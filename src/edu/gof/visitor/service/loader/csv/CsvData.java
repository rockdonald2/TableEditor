package edu.gof.visitor.service.loader.csv;

import edu.gof.visitor.service.loader.Data;

import java.util.Collections;
import java.util.List;

public class CsvData implements Data {

    private List<String> headers;
    private List<List<String>> rowData;

    public CsvData() {
        this.headers = Collections.emptyList();
        this.rowData = Collections.emptyList();
    }

    @Override
    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public List<List<String>> getData() {
        return rowData;
    }

    @Override
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    @Override
    public void setData(List<List<String>> data) {
        this.rowData = data;
    }

}
