package edu.gof.visitor.model;

import java.util.List;

public interface Data {

    List<String> getHeaders();

    void setHeaders(List<String> headers);

    List<List<String>> getData();

    void setData(List<List<String>> data);

}
