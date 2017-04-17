package pl.godziatkowski.viewer;

import java.time.LocalDate;

import static pl.godziatkowski.viewer.StaticConstants.ISO_DATE;
import static pl.godziatkowski.viewer.StaticConstants.TXT;

public class SimpleRecord {

    private final LocalDate checkDate;
    private final InsuranceStatus status;
    private final String name;
    private final String surname;
    private final String pesel;
    private boolean dn;

    public SimpleRecord(LocalDate checkDate, InsuranceStatus status, String name, String surname, String pesel, boolean dn) {
        this.checkDate = checkDate;
        this.status = status;
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.dn = dn;
    }

    public LocalDate getCheckDate() {
        return checkDate;
    }

    public InsuranceStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPesel() {
        return pesel;
    }

    public boolean isDn() {
        return dn;
    }

    public void setDn(boolean dn) {
        this.dn = dn;
    }

    public String toCsvRow() {
        String joinedRequiredValues = String.join(",", checkDate.format(ISO_DATE), status.convertStatusToColor());
        if (dn) {
            joinedRequiredValues = String.join(",", joinedRequiredValues, "DN");
        }
        return joinedRequiredValues;
    }

    public String generateFileName() {
        return String.join("_", pesel, surname, name) + TXT;
    }

}
