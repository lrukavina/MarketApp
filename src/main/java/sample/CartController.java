package main.java.sample;

import com.itextpdf.text.DocumentException;
import database.Database;
import enumeration.ItemType;
import enumeration.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;
import model.Receipt;
import model.User;
import pdf.GeneratePdf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CartController implements Initializable {

    private static ObservableList<Item> itemObservableList;
    private static ObservableList<Item> allItemsObservableList;
    private static ObservableList<Item> selectedItemObservableList;
    private static User currentUser;
    private List<Item> items = new ArrayList<>();
    private List<Item> selectedItems = new ArrayList<>();

    @FXML
    private TableView<Item> itemTableView;

    @FXML
    private TableView<Item> selectedItemTableView;

    @FXML
    private TableColumn<Item, String> selectedItemNameColumn;

    @FXML
    private TableColumn<Item, Integer> selectedItemQuantityColumn;

    @FXML
    private TableColumn<Item, BigDecimal> selectedItemPriceColumn;

    @FXML
    private TableColumn<Item, String> itemNameColumn;

    @FXML
    private TableColumn<Item, String> itemCodeColumn;

    @FXML
    private TableColumn<Item, ItemType> itemTypeColumn;

    @FXML
    private TableColumn<Item, Integer> itemQuantityColumn;

    @FXML
    private TableColumn<Item, BigDecimal> itemPriceColumn;

    @FXML
    private Label priceLabel;

    @FXML
    private TextField searchTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        itemObservableList = FXCollections.observableArrayList();
        allItemsObservableList = FXCollections.observableArrayList();
        priceLabel.setText("0 kn");

        try {
            items = Database.fetchAllItems();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        itemObservableList.addAll(items);
        allItemsObservableList.addAll(itemObservableList);

        itemTableView.setItems(itemObservableList);

        itemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemCodeColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("code"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<Item, ItemType>("itemType"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("price"));

        selectedItemObservableList = FXCollections.observableArrayList();
        selectedItemObservableList.addAll(selectedItems);
        selectedItemTableView.setItems(selectedItemObservableList);

        selectedItemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        selectedItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
        selectedItemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("price"));


        itemTableView.setRowFactory(itemTableView -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Item item = row.getItem();
                    selectItem(item);
                }
            });
            return row;
        });
    }

    public void initUser(User user){
        currentUser = user;
    }

    private void selectItem(Item item){

        boolean itemFound = false;

        for (Item selectedItem: selectedItemObservableList){
            if(selectedItem.equals(item)){

                selectedItem.setQuantity(selectedItem.getQuantity() + 1);
                selectedItem.calculatePrice();

                List<Item> refreshSelected = new ArrayList<>();
                refreshSelected.addAll(selectedItemObservableList);
                selectedItemObservableList.clear();

                selectedItemObservableList.addAll(refreshSelected);
                itemFound = true;
                break;
            }
            if(itemFound){
                break;
            }
        }

        if(!itemFound){
            selectedItemObservableList.add(item);
        }
        
        priceLabel.setText(calculateTotalPrice(selectedItemObservableList)+" kn");
    }

    private String calculateTotalPrice(List<Item> items){

        BigDecimal totalPrice = BigDecimal.valueOf(0);

        for(Item item: items){
            totalPrice = totalPrice.add(item.getPrice());

        }

        return totalPrice.toString();
    }

    @FXML
    public void finishPurchase() throws IOException, DocumentException, SQLException {
        GeneratePdf generatePdf = new GeneratePdf();
        Receipt receipt = new Receipt(currentUser, selectedItemObservableList,
                LocalDate.now(), LocalTime.now(),
                BigDecimal.valueOf(Double.parseDouble(calculateTotalPrice(selectedItemObservableList))));

        Database.saveReceipt(receipt);
        generatePdf.generateReceipt(receipt);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Receipt saved");
        alert.setHeaderText("Receipt saved");
        alert.showAndWait();

        MainMenuController mainMenuController = new MainMenuController();
        mainMenuController.showCart();
    }

    @FXML
    public void searchItems(){
        String searchText = searchTextField.getText();

        if(searchText.isEmpty()){
            itemObservableList.clear();
            itemObservableList.addAll(allItemsObservableList);
            itemTableView.setItems(itemObservableList);
        }
        else{
            List<Item> filteredItemList = itemObservableList.stream()
                                          .filter(item -> item.getName().toLowerCase().contains(searchText))
                                          .collect(Collectors.toList());

            itemObservableList.clear();
            itemObservableList.addAll(FXCollections.observableArrayList(filteredItemList));
            itemTableView.setItems(itemObservableList);
        }
    }

}
