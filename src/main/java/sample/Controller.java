package main.java.sample;

import database.Database;
import javafx.fxml.Initializable;
import model.Article;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Article> articles = Database.fetchAllArticles();
            System.out.println(articles);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
