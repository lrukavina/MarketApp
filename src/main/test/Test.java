package main.test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import database.Database;
import enumeration.ItemType;
import enumeration.UserType;
import model.Item;
import model.Receipt;
import model.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Test {
    public static void main(String[] args) throws SQLException, FileNotFoundException, DocumentException {

        Item item = new Item(10L, "test", ItemType.ELECTRONICS, 1, BigDecimal.valueOf(1));
        User user = new User(1L, "Luka", "Rukavina", UserType.ADMIN, "admin","123" );
        System.out.println(item.getCode());
        System.out.println(user);

        Receipt receipt = new Receipt(user, Database.fetchAllItems(), LocalDate.now(),
                LocalTime.now(), BigDecimal.valueOf(25));

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

        PdfPTable table = new PdfPTable(3);
        PdfPCell cell = new PdfPCell(new Phrase("Item name"));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Item code"));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Price"));
        table.addCell(cell);
        table.setHeaderRows(1);

        for(Item receiptItem: receipt.getItems()){
            table.addCell(receiptItem.getName());
            table.addCell(receiptItem.getCode());
            table.addCell(receiptItem.getPrice().toString());
        }

        document.add(table);
        document.close();

    }
}
