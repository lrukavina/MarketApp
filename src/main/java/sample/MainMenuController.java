package main.java.sample;

import enumeration.UserType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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

    @FXML
    private Text welcomeText = new Text("Welcome");

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem registerUserMenuItem = new MenuItem();

    @FXML
    private MenuItem addItemMenuItem = new MenuItem();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(currentUser != null){
            welcomeText.setText(currentUser.getUsername().toUpperCase()+", welcome to MarketApp");

            if(currentUser.getUserType().equals(UserType.USER)){
               registerUserMenuItem.setDisable(true);
               addItemMenuItem.setDisable(true);

            }
        }
    }

    public void initUser(User user) throws SQLException {
        currentUser = user;
    }

    @FXML
    public void showCart() throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("cart.fxml"));
        loader.load();

        CartController cartController = loader.getController();
        cartController.initUser(currentUser);

        Parent cartFrame =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(
                        "cart.fxml")));
        Scene cartScene = new Scene(cartFrame, 800, 600);
        Main.getMainStage().setTitle("MarketApp | Cart");
        Main.getMainStage().setScene(cartScene);
    }

    @FXML
    public void showMain() throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainMenu.fxml"));
        loader.load();

        MainMenuController mainMenuController = loader.getController();
        mainMenuController.initUser(currentUser);

        Parent mainMenuFrame =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(
                        "mainMenu.fxml")));
        Scene mainMenuScene = new Scene(mainMenuFrame, 800, 600);
        Main.getMainStage().setTitle("MarketApp | Main menu");
        Main.getMainStage().setScene(mainMenuScene);
    }

    @FXML
    public void changeUser() throws IOException {
        Parent loginFrame =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(
                        "login.fxml")));
        Scene loginScene = new Scene(loginFrame, 650, 400);
        Main.getMainStage().setTitle("MarketApp | Login");
        Main.getMainStage().setScene(loginScene);
    }

    @FXML
    public void exitApplication(){
        Platform.exit();
    }

    @FXML
    public void showRegisterUser() throws IOException{
        Parent registerFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("registration.fxml"));
        Scene registerScene = new Scene(registerFrame, 650, 400);
        Main.getMainStage().setScene(registerScene);
    }

    @FXML
    public void showManageUsers() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("manageUsers.fxml"));
        loader.load();

        manageUsersController manageUsersController = loader.getController();
        manageUsersController.initUser(currentUser);

        Parent manageUsersFrame =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(
                        "manageUsers.fxml")));
        Scene manageUsersScene = new Scene(manageUsersFrame, 800, 600);
        Main.getMainStage().setTitle("MarketApp | Manage users");
        Main.getMainStage().setScene(manageUsersScene);
    }

    @FXML
    public void showAbout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("about.fxml"));

        Scene scene = new Scene(loader.load(), 400, 500);
        Stage stage = new Stage();
        stage.setTitle("MarketApp | About");
        stage.setScene(scene);
        stage.show();
    }
}
