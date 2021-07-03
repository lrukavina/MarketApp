package main.java.sample;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Item;
import model.Receipt;
import model.User;
import pdf.PdfManager;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageReceiptsController implements Initializable {
    private static User currentUser = new User();
    private static Receipt selectedReceipt = new Receipt();
    private static ObservableList<Receipt> receiptsObservableList;
    private List<Receipt> receipts = new ArrayList<>();

    @FXML
    private TableView<Receipt> receiptTableView;

    @FXML
    private TableColumn<Receipt, String> receiptNameColumn;

    @FXML
    private TableColumn<Receipt, User> receiptUserColumn;

    @FXML
    private TableColumn<Receipt, LocalDate> receiptDateColumn;

    @FXML
    private TableColumn<Receipt, LocalTime> receiptTimeColumn;

    @FXML
    private TableColumn<Receipt, BigDecimal> receiptPriceColumn;

    private ContextMenu contextMenu = new ContextMenu();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        receiptsObservableList = FXCollections.observableArrayList();
        contextMenu.getItems().add(new MenuItem("Print receipt"));
        contextMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    printReceipt(selectedReceipt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            receipts = Database.fetchAllReceipts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        receiptsObservableList.addAll(receipts);
        receiptTableView.setPlaceholder(new Label("No receipts found, please make a purchase"));
        receiptTableView.setItems(receiptsObservableList);
        receiptTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    contextMenu.show(receiptTableView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });

        receiptNameColumn.setCellValueFactory(new PropertyValueFactory<Receipt, String>("name"));
        receiptUserColumn.setCellValueFactory(new PropertyValueFactory<Receipt, User>("user"));
        receiptDateColumn.setCellValueFactory(new PropertyValueFactory<Receipt, LocalDate>("dateIssued"));
        receiptTimeColumn.setCellValueFactory(new PropertyValueFactory<Receipt, LocalTime>("timeIssued"));
        receiptPriceColumn.setCellValueFactory(new PropertyValueFactory<Receipt, BigDecimal>("price"));

        receiptTableView.setRowFactory(receiptTableView -> {
            TableRow<Receipt> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Receipt receipt = row.getItem();
                    try {
                        viewReceipt(receipt);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(event.getClickCount() == 1 && (!row.isEmpty())){
                    Receipt receipt = row.getItem();
                    selectReceipt(receipt);
                }
            });
            return row;
        });
    }

    public void initUser(User user){
        currentUser = user;
    }

    private void selectReceipt(Receipt receipt){
        selectedReceipt = receipt;
    }

    public void viewReceipt(Receipt receipt) throws IOException {
        PdfManager pdfManager = new PdfManager();
        pdfManager.openReceipt(receipt);

    }

    public void printReceipt(Receipt receipt) throws IOException {
        PdfManager pdfManager = new PdfManager();
        pdfManager.printReceipt(receipt);
    }
}
