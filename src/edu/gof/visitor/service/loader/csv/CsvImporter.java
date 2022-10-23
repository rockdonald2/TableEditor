package edu.gof.visitor.service.loader.csv;

import edu.gof.visitor.model.CsvData;
import edu.gof.visitor.model.Data;
import edu.gof.visitor.service.exception.ServiceException;
import edu.gof.visitor.service.iterator.CsvIterator;
import edu.gof.visitor.service.iterator.Iterator;
import edu.gof.visitor.service.loader.Importer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter implements Importer {

    @Override
    public Data importData(String fileName) throws ServiceException {
        final Data data = new CsvData();
        final Path file = Path.of(fileName);

        if (Files.notExists(file)) {
            throw new ServiceException("Given file for import does not exists");
        }

        Iterator<List<String>> iterator;
        try {
            iterator = new CsvIterator(file);
        } catch (FileNotFoundException e) {
            throw new ServiceException("Exception occurred while processing import file", e);
        }

        List<String> headers;
        List<List<String>> rowData = new ArrayList<>();

        try {
            headers = iterator.next();

            while (iterator.hasNext()) {
                rowData.add(iterator.next());
            }
        } catch (IOException e) {
            throw new ServiceException("Exception occurred while processing import file", e);
        }

        data.setData(rowData);
        data.setHeaders(headers);

        return data;
    }

}
