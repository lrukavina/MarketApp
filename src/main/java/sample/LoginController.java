package main.java.sample;

import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Article;
import model.User;
import security.PasswordEncoder;


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

    private List<User> users = new ArrayList<>();

    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            users = Database.fetchAllUsers();
            System.out.println(users);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void registerAccount() throws IOException{
        Parent registerFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
        Scene registerScene = new Scene(registerFrame, 600, 400);
        Main.getMainStage().setScene(registerScene);
    }

    @FXML
    public void login(ActionEvent event) throws IOException, SQLException {
        String usernameText = usernameTextField.getText();
        String passwordText = passwordTextField.getText();
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        Boolean userFound = false;

        if(usernameText.isEmpty() || passwordText.isEmpty()){
            if(usernameText.isEmpty()){
                usernameTextField.setPromptText("Please fill up this field");
                usernameTextField.setStyle("-fx-text-box-border: red;");
            }
            else{
                usernameTextField.setStyle("-fx-text-box-border: black;");
            }

            if(passwordText.isEmpty()){
                passwordTextField.setPromptText("Please fill up this field");
                passwordTextField.setStyle("-fx-text-box-border: red;");
            }
            else{
                passwordTextField.setStyle("-fx-text-box-border: black;");
            }
        }
        else{
            for(User user: users){
                if(user.getUsername().equals(usernameText) && passwordEncoder.decodePassword(user.getPassword()).equals(passwordText)){

                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainMenu.fxml"));
                    root = loader.load();

                    MainMenuController mainMenuController = loader.getController();
                    mainMenuController.initUser(user);

                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    userFound = true;
                }
            }

            if(!userFound){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User not found");
                alert.setHeaderText("User not found");
                alert.setContentText("User with that username or password does not exist");
                alert.showAndWait();

                usernameTextField.setText("");
                passwordTextField.setText("");
            }
        }
    }

}
