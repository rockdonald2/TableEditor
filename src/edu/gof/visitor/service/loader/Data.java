package edu.gof.visitor.service.loader;

import java.util.List;

public interface Data {

    List<String> getHeaders();

    List<List<String>> getData();

    void setHeaders(List<String> headers);

    void setData(List<List<String>> data);

}
