package edu.gof.visitor.model;

import java.util.Collections;
import java.util.List;

public class CsvData implements Data {

    private List<String> headers;
    private List<List<String>> data;

    public CsvData() {
        this.headers = Collections.emptyList();
        this.data = Collections.emptyList();
    }

    @Override
    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public List<List<String>> getData() {
        return data;
    }

    @Override
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    @Override
    public void setData(List<List<String>> data) {
        this.data = data;
    }

}
