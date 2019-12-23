package src.enums;

public enum ImageType {
    CAT("cat");

    private String value;

    ImageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
