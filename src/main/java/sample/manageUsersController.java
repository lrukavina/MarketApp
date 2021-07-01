package main.java.sample;

import database.Database;
import enumeration.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class manageUsersController implements Initializable {

    private static User currentUser = new User();
    private static ObservableList<User> usersObservableList;
    private List<User> users = new ArrayList<>();

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, String> userNameColumn;

    @FXML
    private TableColumn<User, String> userSurnameColumn;

    @FXML
    private TableColumn<User, UserType> userTypeColumn;

    @FXML
    private TableColumn<User, String> userUsernameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usersObservableList = FXCollections.observableArrayList();

        try {
            users = Database.fetchAllUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        usersObservableList.addAll(users);
        userTableView.setItems(usersObservableList);

        userNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        userSurnameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("surname"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<User, UserType>("userType"));
        userUsernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

    }

    public void initUser(User user){
        currentUser = user;
    }
}
