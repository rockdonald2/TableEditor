package edu.gof.visitor.service.loader.csv;

import edu.gof.visitor.service.exception.ServiceException;
import edu.gof.visitor.service.loader.Data;
import edu.gof.visitor.service.loader.Importer;

import java.io.BufferedReader;
import java.io.FileReader;
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

        List<String> headers = new ArrayList<>();
        List<List<String>> rowData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String tempLine;
            int idx = 0;

            while ((tempLine = reader.readLine()) != null) {
                List<String> values = new ArrayList<>(List.of(tempLine.split(",")));

                if (idx == 0) {
                    headers = values;
                } else {
                    rowData.add(values);
                }

                ++idx;
            }
        } catch (IOException e) {
            throw new ServiceException("Exception occurred while processing import file", e);
        }

        data.setData(rowData);
        data.setHeaders(headers);

        return data;
    }

}
