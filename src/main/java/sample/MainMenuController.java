package main.java.sample;

import database.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import model.Article;
import model.User;


import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {


    private User currentUser;

    @FXML
    private Text welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initUser(User user) throws SQLException {
        currentUser = user;
        welcomeText.setText(user.getUsername().toUpperCase()+", welcome to MarketApp");
        getUserArticles();
    }

    @FXML
    public void getUserArticles() throws SQLException {
        List<Article> articles;

        articles = Database.fetchUserArticles(currentUser.getId());
        System.out.println(articles);
    }
}
