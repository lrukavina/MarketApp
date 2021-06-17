package enumeration;

public enum UserType {

    ADMIN("Admin"),
    USER("User");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
