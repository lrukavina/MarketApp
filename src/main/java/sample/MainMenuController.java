package main.java.sample;

import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Article;
import model.User;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {


    private User currentUser;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Text welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initUser(User user) throws SQLException {
        currentUser = user;
        welcomeText.setText(user.getUsername().toUpperCase()+", welcome to MarketApp");
    }

    @FXML
    public void viewCart(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("cart.fxml"));
        root = loader.load();

        CartController cartController = loader.getController();
        cartController.initUser(currentUser);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Cart");
        stage.show();
    }
}
