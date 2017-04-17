package pl.godziatkowski.viewer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static pl.godziatkowski.viewer.StaticConstants.HEADER_ROW;
import static pl.godziatkowski.viewer.StaticConstants.LINE_SEPARATOR;

public class CsvGenerator {

    private static final Logger LOGGER = Logger.getLogger(CsvGenerator.class.getName());
    private final App app;

    public CsvGenerator(App app) {
        this.app = app;
    }

    void generateCsvFromRecords(List<SimpleRecord> simpleRecords) {
        String fileName = generateFileName(simpleRecords);
        String recordsAsCsv = simpleRecords.stream()
                .map(SimpleRecord::toCsvRow)
                .collect(Collectors.joining(LINE_SEPARATOR));
        String csvContent = String.join(LINE_SEPARATOR, HEADER_ROW, recordsAsCsv);
        Path filePath = Paths.get(fileName);
        try {
            Files.write(filePath, Collections.singletonList(csvContent));
        } catch (IOException ex) {
            String error = "Błąd w czasie zapisu danych do pliku csv " + fileName;
            LOGGER.log(Level.SEVERE, error, ex);
            app.logError(error);
        }
    }

    private String generateFileName(List<SimpleRecord> simpleRecords) {
        String fileName = simpleRecords.get(0).generateFileName();
        return fileName;
    }

}
