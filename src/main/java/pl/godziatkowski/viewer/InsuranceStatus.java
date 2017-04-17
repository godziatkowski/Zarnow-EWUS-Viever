package pl.godziatkowski.viewer;

public enum InsuranceStatus {
    INSURED, UN_INSURED;

    public static InsuranceStatus parse(int intBooleanFlag) {
        switch (intBooleanFlag) {
            case 0:
                return UN_INSURED;
            case 1:
                return INSURED;
            default:
                throw new IllegalArgumentException("Unknown Insurance Status");
        }
    }

    public String convertStatusToColor() {
        switch (this) {
            case INSURED:
                return "ZIELONY";
            case UN_INSURED:
                return "CZERWONY";
            default:
                throw new IllegalArgumentException("Unknown Insurance Status");
        }
    }
}
