package pl.godziatkowski.viewer;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.ParserConfigurationException;

public class FileReader {

    private final XmlParser xmlParser;
    private final App app;

    public FileReader(App app) throws ParserConfigurationException {
        this.app = app;
        xmlParser = new XmlParser(app);
    }

    /**
     * @param pathStream - stream with xml file names
     * @param pesel - Pesel of person which function should read
     * @return
     */
    public List<SimpleRecord> readStream(Stream<Path> pathStream, String pesel) {
        return pathStream
                .filter(peselMatch(pesel))
                .parallel()
                .map(path -> logFile(path))
                .map(Path::toFile)
                .map(file -> xmlParser.parseFile(file))
                .filter(Objects::nonNull)
                .map(xmlParser::convertXmlDocumentIntoSimpleRecord)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Predicate<Path> peselMatch(String pesel) {
        return (Path path) -> path.getFileName().toString().contains(pesel);
    }

    private Path logFile(Path path) {
        app.logInfo(path.getFileName().toString());
        return path;
    }

}
