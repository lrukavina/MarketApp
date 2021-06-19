package main.java.sample;

import enumeration.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {


    private static User currentUser;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Text welcomeText = new Text("Welcome");

    @FXML
    private MenuBar menuBar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(currentUser != null){
            welcomeText.setText(currentUser.getUsername().toUpperCase()+", welcome to MarketApp");
        }
    }

    public void initUser(User user) throws SQLException {
        currentUser = user;
    }

    @FXML
    public void showCart(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("cart.fxml"));
        root = loader.load();

        CartController cartController = loader.getController();
        cartController.initUser(currentUser);

        Parent cartFrame =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(
                        "cart.fxml")));
        Scene cartScene = new Scene(cartFrame, 650, 400);
        Main.getMainStage().setScene(cartScene);
    }

    @FXML
    public void showMain(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainMenu.fxml"));
        root = loader.load();

        MainMenuController mainMenuController = loader.getController();
        mainMenuController.initUser(currentUser);

        Parent mainMenuFrame =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(
                        "mainMenu.fxml")));
        Scene mainMenuScene = new Scene(mainMenuFrame, 650, 400);
        Main.getMainStage().setScene(mainMenuScene);
    }
}
