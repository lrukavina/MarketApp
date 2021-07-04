package pdf;

import com.itextpdf.text.DocumentException;
import javafx.scene.control.Alert;
import model.Receipt;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PdfManager {

    public PdfManager(){}

    public void openReceipt(Receipt receipt) throws IOException, DocumentException {
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
            GeneratePdf generatePdf = new GeneratePdf();
            generatePdf.generateReceipt(receipt);
            desktop.open(file);
        }
    }

    public void printReceipt(Receipt receipt) throws IOException, DocumentException {
        File file = new File("receipts\\"+receipt.getName()+".pdf");

        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();

        if(file.exists()){
            desktop.print(file);
        }
        else {
            GeneratePdf generatePdf = new GeneratePdf();
            generatePdf.generateReceipt(receipt);
            desktop.print(file);
        }
    }
}
