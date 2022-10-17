package edu.gof.visitor.service.loader;

import java.util.List;

public interface Data {

    List<String> getHeaders();

    void setHeaders(List<String> headers);

    List<List<String>> getData();

    void setData(List<List<String>> data);

}
