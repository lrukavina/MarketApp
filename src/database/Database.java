package database;

import enumeration.ArticleType;
import model.Article;
import model.User;
import security.PasswordEncoder;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {

    private static final String CONFIG_FILE = "src/database/config.properties";

    private static Connection openConnection() {
        Properties properties = new Properties();
        Connection connection = null;

        try {
            properties.load(new FileReader(CONFIG_FILE));

            String databaseUrl = properties.getProperty("databaseUrl");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

            connection = DriverManager.getConnection(databaseUrl, username, password);

        } catch (IOException | SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    private static void closeConnection(Connection connection){
        try{
            connection.close();
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public static List<Article> fetchAllArticles() throws SQLException{
        List<Article> articles = new ArrayList<>();

        Connection connection = openConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ARTICLE");

        while (rs.next()){
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String articleTypeString = rs.getString("type");

            ArticleType articleType = null;
            switch (articleTypeString){
                case "Food":
                    articleType = ArticleType.FOOD;
            }

            Integer quantity = rs.getInt("quantity");
            BigDecimal price = rs.getBigDecimal("price");

            Article article = new Article(id, name, articleType, quantity, price);
            articles.add(article);
        }
        closeConnection(connection);
        return articles;
    }

    public static List<User> fetchAllUsers() throws SQLException{
        List<User> users = new ArrayList<>();

        Connection connection = openConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM USER");

        while (rs.next()){
            long id = rs.getLong("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            List<Article> articles = new ArrayList<>();

            articles = fetchUserArticles(id);

            User user = new User(id, username, password, articles);
            users.add(user);
        }
        closeConnection(connection);
        return users;
    }

    public static void registerUser(User user) throws SQLException{
        Connection connection = openConnection();
        PasswordEncoder passwordEncoder = new PasswordEncoder();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO USER (USERNAME, PASSWORD) VALUES (?,?)");
        stmt.setString(1, user.getUsername());
        stmt.setString(2, passwordEncoder.encodePassword(user.getPassword()));

        stmt.executeUpdate();
        closeConnection(connection);
    }

    public static List<Article> fetchUserArticles(Long userId)throws SQLException{
        List<Article> articles = new ArrayList<>();

        Connection connection = openConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT ARTICLE.* FROM ARTICLE INNER JOIN\n" +
                "USER_ARTICLE ON ARTICLE.ID = ARTICLE_ID INNER JOIN\n" +
                "USER ON USER_ID = USER.ID\n" +
                "WHERE USER.ID = ?");
        stmt.setLong(1, userId);
        ResultSet rs = stmt.executeQuery();


        while (rs.next()){
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String articleTypeString = rs.getString("type");

            ArticleType articleType = null;
            switch (articleTypeString){
                case "Food":
                    articleType = ArticleType.FOOD;
            }

            Integer quantity = rs.getInt("quantity");
            BigDecimal price = rs.getBigDecimal("price");

            Article article = new Article(id, name, articleType, quantity, price);
            articles.add(article);
        }
        closeConnection(connection);
        return articles;
    }
}
