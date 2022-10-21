package edu.gof.visitor.service.iterator;

import java.io.IOException;

public interface Iterator<T> {

    boolean hasNext() throws IOException;

    T next() throws IOException;

}
