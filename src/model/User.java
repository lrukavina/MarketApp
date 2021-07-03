package model;


import enumeration.UserType;

public class User {
    private Long id;
    private String name;
    private String surname;
    private UserType userType;
    private String username;
    private String password;

    public User(){}

    public User(Long id, String name, String surname, UserType userType, String username, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.userType = userType;
        this.username = username;
        this.password = password;
    }

    public User(String name, String surname, UserType userType, String username, String password){
        this.name = name;
        this.surname = surname;
        this.userType = userType;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.getSurname();
    }
}
