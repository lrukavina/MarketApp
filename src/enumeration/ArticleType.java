package enumeration;

public enum ArticleType {

    FOOD("Food");

    private String description;

    ArticleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
