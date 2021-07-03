package main.java.sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.User;
import settings.SettingsLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private static User currentUser = new User();
    private SettingsLoader settingsLoader = new SettingsLoader();
    private Properties settings = new Properties();

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private ChoiceBox<String> currencyChoiceBox = new ChoiceBox<String>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            settings = settingsLoader.loadSettings();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Settings error");
            alert.setHeaderText("Error loading settings");
            alert.setContentText("Settings could not be loaded. Please check does the file exist.");
            alert.showAndWait();
        }

        nameTextField.setText(settings.getProperty("marketName"));
        addressTextField.setText(settings.getProperty("marketAddress"));
        currencyChoiceBox.setValue(settings.getProperty("currency"));

    }

    public void initUser(User user){
        currentUser = user;
    }


    public void saveSettings() throws IOException, SQLException {
        String nameText = nameTextField.getText();
        String addressText = addressTextField.getText();
        String currency = currencyChoiceBox.getValue();

        FileOutputStream out = new FileOutputStream(settingsLoader.getSettingsPath());
        settings.setProperty("marketName", nameText);
        settings.setProperty("marketAddress", addressText);
        settings.setProperty("currency", currency);
        settings.store(out, null);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainMenu.fxml"));
        loader.load();

        MainMenuController mainMenuController = loader.getController();
        mainMenuController.showMain();
    }
}
