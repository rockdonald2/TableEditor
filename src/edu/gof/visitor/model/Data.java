package edu.gof.visitor.model;

import edu.gof.visitor.service.sort.ComparatorStrategy;
import edu.gof.visitor.service.sort.SortStrategy;

import java.util.List;

public interface Data {

    List<String> getHeaders();

    void setHeaders(List<String> headers);

    List<List<String>> getData();

    void setData(List<List<String>> data);

    default void sort(SortStrategy sortStrategy, ComparatorStrategy comparatorStrategy) {
        sortStrategy.sort(this, comparatorStrategy);
    }

}
