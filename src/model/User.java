package model;

import java.util.Arrays;
import java.util.Base64;

public class User {
    private Long id;
    private String username;
    private String password;

    public User(Long id, String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String  username, String password){
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String decodePassword(String password){
        byte[] decodedBytes = Base64.getDecoder().decode(password);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }

    public String encodePassword(String password){
        String encodedPassword = Base64.getEncoder().encodeToString(this.getPassword().getBytes());
        return encodedPassword;
    }
}
