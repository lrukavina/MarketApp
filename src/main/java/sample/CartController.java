package main.java.sample;

import com.itextpdf.text.DocumentException;
import database.Database;
import enumeration.ItemType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;
import model.Receipt;
import model.User;
import pdf.GeneratePdf;

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
    private static ObservableList<String> searchByObservableList;
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

    @FXML
    private ChoiceBox<String> searchByChoiceBox = new ChoiceBox<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        itemObservableList = FXCollections.observableArrayList();
        allItemsObservableList = FXCollections.observableArrayList();
        searchByObservableList = FXCollections.observableArrayList();
        priceLabel.setText("0 kn");

        searchByObservableList.add("By name");
        searchByObservableList.add("By code");
        searchByObservableList.add("By price");
        searchByChoiceBox.setItems(searchByObservableList);
        searchByChoiceBox.setValue("By name");

        try {
            items = Database.fetchAllItems();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        itemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemCodeColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("code"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<Item, ItemType>("itemType"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("price"));

        selectedItemObservableList = FXCollections.observableArrayList();
        selectedItemObservableList.addAll(selectedItems);
        selectedItemTableView.setItems(selectedItemObservableList);
        selectedItemTableView.setPlaceholder(new Label("Selected items"));

        selectedItemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        selectedItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
        selectedItemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("price"));

        FilteredList<Item> filteredData = new FilteredList<>(FXCollections.observableList(items));
        itemTableView.setItems(filteredData);
        itemTableView.setPlaceholder(new Label("No items found"));

        searchTextField.textProperty().addListener((observable, oldValue, newValue) ->
                itemTableView.setItems(filterList(filteredData, newValue, searchByChoiceBox.getValue()))
        );

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

    private ObservableList<Item> filterList(List<Item> list, String searchText, String searchBy){
        List<Item> filteredList = list.stream()
                                    .filter(item -> item.getName().toLowerCase().contains(searchText.toLowerCase())
                                    || item.getCode().toUpperCase().contains(searchText.toUpperCase()))
                                    .collect(Collectors.toList());

        return FXCollections.observableList(filteredList);
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

}
