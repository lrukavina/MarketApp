package pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import model.Receipt;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;

public class GeneratePdf {

    public GeneratePdf(){}

    public void GenerateReceipt(Receipt receipt) throws FileNotFoundException, DocumentException {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        String fileName = "RT-" + localDate.toString();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        Paragraph paragraph = new Paragraph("MarketApp");

        document.add(paragraph);
        paragraph.clear();
        paragraph.add("Market for all");
        document.add(paragraph);


        document.close();
    }
}
