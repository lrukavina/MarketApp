package main.java.sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class aboutController implements Initializable {

    @FXML
    TextArea aboutTextArea = new TextArea();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        aboutTextArea.setEditable(false);
    }
}
