package enumeration;

public enum ItemType {

    FOOD("Food"),
    ELECTRONICS("Electronics");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }
}
