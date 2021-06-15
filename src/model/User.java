package model;

import java.util.List;

public class User {
    private Long id;
    private String username;
    private String password;
    private List<Article> articles;

    public User(Long id, String username, String password, List<Article> articles) {
        this.username = username;
        this.password = password;
        this.articles = articles;
    }

    public User(String username, String password, List<Article> articles){
        this.username = username;
        this.password = password;
        this.articles = articles;
    }

    public User(String username, String password){
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

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", articles=" + articles +
                '}';
    }
}
