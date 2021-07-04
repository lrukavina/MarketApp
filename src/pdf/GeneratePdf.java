package pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Item;
import model.Receipt;
import settings.SettingsLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class GeneratePdf {

    private SettingsLoader settingsLoader = new SettingsLoader();
    private Properties settings = new Properties();

    public GeneratePdf(){}

    public void generateReceipt(Receipt receipt) throws IOException, DocumentException {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        /*String receiptName = localDate.toString() + "-" + localTime.getHour() + "-"
                + localTime.getMinute() + "-" + localTime.getSecond();*/

        settings = settingsLoader.loadSettings();

        String fileName ="receipts\\" + receipt.getName() +".pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        Paragraph paragraph = new Paragraph(settings.getProperty("marketName"));

        document.add(paragraph);
        paragraph.clear();
        File fontFile = new File("font/arial.ttf");
        BaseFont baseFont = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12, Font.NORMAL);
        paragraph.setFont(font);
        paragraph.add(settings.getProperty("marketAddress"));
        document.add(paragraph);
        paragraph.clear();
        paragraph.add(" ");
        document.add(paragraph);
        paragraph.clear();
        paragraph.add("Employee: " + receipt.getUserNameSurname());
        document.add(paragraph);

        paragraph.clear();
        paragraph.add(" ");
        document.add(paragraph);

        PdfPTable table = new PdfPTable(4);
        PdfPCell cell = new PdfPCell(new Phrase("Item name"));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Item code"));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Quantity"));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Price"));
        table.addCell(cell);
        table.setHeaderRows(1);

        for(Item receiptItem: receipt.getItems()){
            table.addCell(receiptItem.getName());
            table.addCell(receiptItem.getCode());
            table.addCell(receiptItem.getQuantity().toString());
            table.addCell(receiptItem.getPrice().toString() + settings.getProperty("currency"));
        }

        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");

        table.addCell("TOTAL: ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(receipt.calculatePrice().toString() + settings.getProperty("currency"));

        document.add(table);

        paragraph.clear();
        paragraph.add(" ");
        document.add(paragraph);

        paragraph.clear();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        paragraph.add("Date: " + receipt.getDateIssued().format(dateFormatter));
        document.add(paragraph);
        paragraph.clear();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        paragraph.add("Time: "+ receipt.getTimeIssued().format(timeFormatter) + " h");
        document.add(paragraph);
        document.close();
    }

}
