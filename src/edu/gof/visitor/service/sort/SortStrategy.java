package edu.gof.visitor.service.sort;

import edu.gof.visitor.model.Data;

public interface SortStrategy {

    void sort(Data data, ComparatorStrategy comparatorStrategy);

}
