package pdf;

import javafx.scene.control.Alert;
import model.Receipt;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PdfManager {

    public PdfManager(){}

    public void openReceipt(Receipt receipt) throws IOException {
        File file = new File("receipts\\"+receipt.getName()+".pdf");

        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();

        if(file.exists()){
            desktop.open(file);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error opening receipt");
            alert.setHeaderText("Cannot open receipt");
            alert.setContentText("Receipt cannot be open, file might be deleted or is not on this machine");
            alert.showAndWait();
        }
    }
}
