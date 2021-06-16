package enumeration;

public enum ItemType {

    FOOD("Food");

    private String description;

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
