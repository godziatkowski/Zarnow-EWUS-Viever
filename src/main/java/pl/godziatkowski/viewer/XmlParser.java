package pl.godziatkowski.viewer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlParser {

    private static final Logger LOGGER = Logger.getLogger(XmlParser.class.getName());
    private final App app;

    public XmlParser(App app) {
        this.app = app;
    }

    public Document parseInputStream(InputStream inputStream) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            return documentBuilder.parse(inputStream);
        } catch (SAXException | IOException ex) {
            String errorMessage = "Błąd parsowania pliku na obiekt xml";
            LOGGER.log(Level.SEVERE, errorMessage, ex);
            app.logError(errorMessage);
        } catch (ParserConfigurationException ex) {
            String errorMessage = "Błąd tworzenia parsera";
            LOGGER.log(Level.SEVERE, errorMessage, ex);
            app.logError(errorMessage);
        }
        return null;
    }

    public Document parseFile(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            return documentBuilder.parse(file);
        } catch (SAXException | IOException ex) {
            String errorMessage = "Błąd parsowania pliku " + file.getName();
            LOGGER.log(Level.SEVERE, errorMessage, ex);
            app.logError(errorMessage);
        } catch (ParserConfigurationException ex) {
            String errorMessage = "Błąd tworzenia parsera";
            LOGGER.log(Level.SEVERE, errorMessage, ex);
            app.logError(errorMessage);
        }
        return null;
    }

    public SimpleRecord convertXmlDocumentIntoSimpleRecord(Document xmlDocument) {
        try {
            return new SimpleRecord(
                    getCheckDate(xmlDocument),
                    getInsuranceStatus(xmlDocument),
                    getStringField(xmlDocument, StaticConstants.NAME),
                    getStringField(xmlDocument, StaticConstants.SURNAME),
                    getStringField(xmlDocument, StaticConstants.PESEL),
                    isInsuranceStatusSignaturePresent(xmlDocument));
        } catch (DOMException e) {
            String errorMessage = "Error reading xml file from xml Document";
            LOGGER.log(Level.SEVERE, errorMessage, e);
            app.logError(errorMessage);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error parsing xml attribute";
            LOGGER.log(Level.SEVERE, errorMessage, e);
            app.logError(errorMessage);
        }
        return null;
    }

    private static boolean isInsuranceStatusSignaturePresent(Document xmlDocument) {
        return xmlDocument.getElementsByTagName(StaticConstants.INSURANCE_STATUS).item(0).getAttributes().getNamedItem(StaticConstants.INSURANCE_STATUS_SIGNATURE) != null;
    }

    private LocalDate getCheckDate(Document xmlDocument) throws DOMException {
        return LocalDate.parse(xmlDocument.getElementsByTagName(StaticConstants.CHECK_DATE).item(0).getTextContent(), StaticConstants.ISO_DATE);
    }

    private InsuranceStatus getInsuranceStatus(Document xmlDocument) throws DOMException {
        int intFlag = Integer.parseInt(xmlDocument.getElementsByTagName(StaticConstants.INSURANCE_STATUS).item(0).getTextContent());
        return InsuranceStatus.parse(intFlag);
    }

    private String getStringField(Document xmlDocument, String fieldName) {
        return xmlDocument.getElementsByTagName(fieldName).item(0).getTextContent();
    }

}
