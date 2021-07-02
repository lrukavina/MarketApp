package main.test;

import com.itextpdf.text.*;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Test {

    static List<Item> items = new ArrayList<>();

    public static void main(String[] args) throws SQLException, FileNotFoundException, DocumentException {

        Item item = new Item(4L, "test", ItemType.ELECTRONICS, BigDecimal.valueOf(1));
        User user = new User(1L, "Luka", "Rukavina", UserType.ADMIN, "admin","123" );
        System.out.println(item.getCode());
        System.out.println(user);

        Receipt receipt = Database.fetchAllReceipts().get(0);

        items.add(item);
        Receipt testReceipt = new Receipt(user, items, LocalDate.now(), LocalTime.now(), BigDecimal.valueOf(25));

        //Database.saveReceipt(testReceipt);

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        /*String receiptName = localDate.toString() + "-" + localTime.getHour() + "-"
                + localTime.getMinute() + "-" + localTime.getSecond();*/

        String receiptName = "test";
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
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        paragraph.add("Time: "+ receipt.getTimeIssued().format(timeFormatter) + " h");
        document.add(paragraph);
        document.close();

    }
}
