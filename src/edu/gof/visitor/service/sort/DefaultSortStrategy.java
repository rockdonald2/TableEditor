package edu.gof.visitor.service.sort;

import edu.gof.visitor.model.Data;

public class DefaultSortStrategy implements SortStrategy {

    @Override
    public void sort(Data data, ComparatorStrategy comparatorStrategy) {
        data.getData().sort(comparatorStrategy);
    }

}
