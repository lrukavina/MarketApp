package main.java.sample;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        receiptsObservableList = FXCollections.observableArrayList();

        try {
            receipts = Database.fetchAllReceipts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        receiptsObservableList.addAll(receipts);
        receiptTableView.setPlaceholder(new Label("No receipts found, please make a purchase"));
        receiptTableView.setItems(receiptsObservableList);

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
                        selectReceipt(receipt);
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error opening receipt");
                        alert.setHeaderText("Cannot open receipt");
                        alert.setContentText("Receipt cannot be open, file might be deleted");
                        alert.showAndWait();
                    }
                }
            });
            return row;
        });
    }

    public void initUser(User user){
        currentUser = user;
    }

    public void selectReceipt(Receipt receipt) throws IOException {
        PdfManager pdfManager = new PdfManager();
        pdfManager.openReceipt(receipt);

    }
}
