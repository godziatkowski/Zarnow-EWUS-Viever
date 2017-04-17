package pl.godziatkowski.viewer;

import java.time.format.DateTimeFormatter;


public class StaticConstants {
    public static final String CHECK_DATE = "ns2:data_waznosci_potwierdzenia";
    public static final String HEADER_ROW = "Data,Status,DN";
    public static final String INSURANCE_STATUS = "ns2:status_ubezp";
    public static final String INSURANCE_STATUS_SIGNATURE = "ozn_rec";
    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_DATE;
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NAME = "ns2:imie";
    public static final String PESEL = "ns2:numer_pesel";
    public static final String SURNAME = "ns2:nazwisko";
    public static final String TXT = ".txt";
    public static final String XML = ".xml";
    public static final String ERROR = "[ERROR] ";
    public static final String INFO = "[INFO] ";
}
