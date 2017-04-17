package pl.godziatkowski.viewer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.ParserConfigurationException;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        String currentPath = System.getProperty("user.dir");
        App app = new App();
        app.setVisible(true);
        String personId = showInputDialog(app, "Podaj pesel:");
        if (Objects.isNull(personId)) {
            LOGGER.log(Level.SEVERE, "Anulowano");
            showMessageDialog(app, "Nie podałeś peselu - program się zakończy");
            System.exit(0);
        }
        try {
            FileReader fileReader = new FileReader(app);
            ZipReader zipReader = new ZipReader(app);
            CsvGenerator csvGenerator = new CsvGenerator(app);
            List<Path> streamOfFilePaths = Files.walk(Paths.get(currentPath)).collect(Collectors.toList());
            Stream<Path> xmlStream = streamOfFilePaths.stream().filter(filterXml());
            Stream<Path> zipStream = streamOfFilePaths.stream().filter(filterZip());
            
            List<SimpleRecord> parsedFiles = fileReader.readStream(xmlStream, personId);
            app.checkReadFromDirectoriesCheckbox();
            parsedFiles.addAll(zipReader.readStream(zipStream, personId));
            app.checkReadFromZipCheckbox();
            if (!parsedFiles.isEmpty()) {
                parsedFiles = sortRecords(parsedFiles);
                csvGenerator.generateCsvFromRecords(parsedFiles);
                app.checkSaveToFileCheckbox();
            }
            app.enableOk();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Błąd w czasie przeszukiwania plików w aktualnym katalogu", ex);
            app.enableOk();
        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, "Nie udało się utworzyć parsera dla plików xml", ex);
            app.enableOk();
        }
    }

    private static Predicate<Path> filterZip() {
        return (Path file) -> file.getFileName().toString().toLowerCase().endsWith(".zip");
    }

    private static Predicate<Path> filterXml() {
        return (Path file) -> file.getFileName().toString().toLowerCase().endsWith(StaticConstants.XML);
    }

    private static List<SimpleRecord> sortRecords(List<SimpleRecord> parsedFiles) {
        return parsedFiles.stream()
                .sorted((r1, r2) -> r1.getCheckDate().compareTo(r2.getCheckDate()))
                .collect(Collectors.toList());
    }

}
