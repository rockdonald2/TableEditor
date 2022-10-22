package edu.gof.visitor.service.iterator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvIterator implements Iterator<List<String>> {

    private static final String SEPARATOR = ",";

    private final BufferedReader bufferedReader;

    private String nextLine;

    public CsvIterator(Path inputFile) throws FileNotFoundException {
        this.bufferedReader = new BufferedReader(new FileReader(inputFile.toFile()));
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public List<String> next() throws IOException {
        String currLine = bufferedReader.readLine();

        if (currLine == null && nextLine != null) {
            currLine = nextLine;
            nextLine = null;
            this.bufferedReader.close();
        } else {
            nextLine = bufferedReader.readLine();
        }

        if (currLine != null) {
            return new ArrayList<>(List.of(currLine.split(SEPARATOR)));
        }

        throw new IOException("Trying to read from an empty iterator");
    }

}
