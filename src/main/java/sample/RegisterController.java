package main.java.sample;

import database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    List<User> users = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            users = Database.fetchAllUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void registerAccount() throws SQLException, IOException {
        String usernameText = usernameTextField.getText();
        String passwordText = passwordField.getText();
        Boolean userExists = false;

        for(User user: users){
            if(user.getUsername().equals(usernameText)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User already registered");
                alert.setHeaderText("Username taken");
                alert.setContentText("User with that username is already registered. Please pick another username");
                alert.showAndWait();
                userExists = true;
                break;
            }
        }

        if(!userExists){
            User user = new User(usernameText, passwordText);
            Database.registerUser(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User created");
            alert.setHeaderText("User successfully created");
            alert.setContentText("User with username "+user.getUsername()+" successfully created, you may now login");
            alert.showAndWait();

            Parent loginFrame =
                    FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            Scene loginScene = new Scene(loginFrame, 600, 400);
            Main.getMainStage().setScene(loginScene);
        }
    }
}
