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
import model.Article;
import model.User;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

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
    public void login() throws IOException{
        String usernameText = usernameTextField.getText();
        String passwordText = passwordTextField.getText();
        Boolean userFound = false;

        for(User user: users){
            if(user.getUsername().equals(usernameText) && user.getPassword().equals(passwordText)){
                Parent mainMenuFrame =
                        FXMLLoader.load(getClass().getClassLoader().getResource("mainMenu.fxml"));
                Scene mainMenuScene = new Scene(mainMenuFrame, 600, 400);
                Main.getMainStage().setScene(mainMenuScene);
                userFound = true;
            }
        }

        if(!userFound){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User not found");
            alert.setHeaderText("User not found");
            alert.setContentText("User with that username or password does not exist");
            alert.showAndWait();
        }
    }

}
