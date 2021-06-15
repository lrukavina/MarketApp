package main.java.sample;

import database.Database;
import enumeration.ArticleType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Article;
import model.User;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    private static ObservableList<Article> articleObservableList;
    private User currentUser;

    @FXML
    List<Article> articles = new ArrayList<>();

    @FXML
    private TableView<Article> articleTableView;

    @FXML
    private TableColumn<Article, String> articleNameColumn;

    @FXML
    private TableColumn<Article, ArticleType> articleTypeColumn;

    @FXML
    private TableColumn<Article, Integer> articleQuantityColumn;

    @FXML
    private TableColumn<Article, BigDecimal> articlePriceColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        articleNameColumn.setCellValueFactory(new PropertyValueFactory<Article, String>("name"));
        articleTypeColumn.setCellValueFactory(new PropertyValueFactory<Article, ArticleType>("articleType"));
        articleQuantityColumn.setCellValueFactory(new PropertyValueFactory<Article, Integer>("quantity"));
        articlePriceColumn.setCellValueFactory(new PropertyValueFactory<Article, BigDecimal>("price"));

        if(articleObservableList == null){
            articleObservableList = FXCollections.observableArrayList();
        }
    }

    public void initUser(User user) throws SQLException {
        currentUser = user;
        articles = Database.fetchUserArticles(currentUser.getId());
        articleObservableList.addAll(articles);
        articleTableView.setItems(articleObservableList);
    }

}
