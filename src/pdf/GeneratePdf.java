package pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Item;
import model.Receipt;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GeneratePdf {

    public GeneratePdf(){}

    public void generateReceipt(Receipt receipt) throws FileNotFoundException, DocumentException {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        String receiptName = localDate.toString() + "-" + localTime.getHour() + "-"
                + localTime.getMinute() + "-" + localTime.getSecond();

        String fileName ="C:\\pdf\\" + "RT-" + receiptName +".pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        Paragraph paragraph = new Paragraph("MarketApp");

        document.add(paragraph);
        paragraph.clear();
        paragraph.add("Market for all");
        document.add(paragraph);
        paragraph.clear();
        paragraph.add(" ");
        document.add(paragraph);
        paragraph.clear();
        paragraph.add("Employee: " + receipt.getUser().getName() + " " + receipt.getUser().getSurname());
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
            table.addCell(receiptItem.getPrice().toString());
        }

        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");

        table.addCell("TOTAL: ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(receipt.calculatePrice().toString());

        document.add(table);

        paragraph.clear();
        paragraph.add(" ");
        document.add(paragraph);

        paragraph.clear();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        paragraph.add("Date: " + receipt.getDateIssued().format(dateFormatter));
        document.add(paragraph);
        paragraph.clear();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss " + "h");
        paragraph.add("Time: "+ receipt.getTimeIssued().format(timeFormatter));
        document.add(paragraph);
        document.close();
    }

}
