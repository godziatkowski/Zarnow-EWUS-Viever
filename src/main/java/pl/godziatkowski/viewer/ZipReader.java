package pl.godziatkowski.viewer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;

import static pl.godziatkowski.viewer.StaticConstants.XML;

public class ZipReader {

    private static final Logger LOGGER = Logger.getLogger(ZipReader.class.getName());
    private final XmlParser xmlParser;
    private final App app;

    public ZipReader(App app) throws ParserConfigurationException {
        this.app = app;
        xmlParser = new XmlParser(app);
    }

    public List<SimpleRecord> readStream(Stream<Path> pathStream, String pesel) {
        return pathStream
                .map(zip -> readZipContent(zip, pesel))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(xmlParser::parseInputStream)
                .map(xmlParser::convertXmlDocumentIntoSimpleRecord)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<InputStream> readZipContent(Path zip, String pesel) {
        try {
            app.logInfo("Reading files from " + zip.getFileName().toString());
            ZipFile zipFile = new ZipFile(zip.toAbsolutePath().toString());
            return Collections.list(zipFile.entries())
                    .stream()
                    .parallel()
                    .filter(entry -> !entry.isDirectory())
                    .filter(isXml())
                    .filter(containsPesel(pesel))
                    .map(entry -> getInputStream(zipFile, entry))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException ex) {
            String errorMessage = "Błąd w czasie odczytu danych z plików z paczki " + zip.toAbsolutePath().toString();
            LOGGER.log(Level.SEVERE, errorMessage, ex);
            app.logError(errorMessage);
            return null;
        }
    }

    private InputStream getInputStream(ZipFile zipFile, ZipEntry entry) {
        try {
            app.logInfo(entry.getName());
            return zipFile.getInputStream(entry);
        } catch (IOException ex) {
            String errorMessage = "Błąd w czasie odczytu pliku " + entry.getName() + " z paczki " + zipFile.getName();
            LOGGER.log(Level.SEVERE, errorMessage, ex);
            app.logError(errorMessage);
            return null;
        }
    }

    private static Predicate<ZipEntry> containsPesel(String pesel) {
        return entry -> entry.getName().contains(pesel);
    }

    private static Predicate<ZipEntry> isXml() {
        return entry -> entry.getName().endsWith(XML);
    }

}
