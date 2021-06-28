package main.java.sample;

import database.Database;
import enumeration.ItemType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;
import model.User;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    private static ObservableList<Item> itemObservableList;
    private static ObservableList<Item> selectedItemObservableList;
    private User currentUser;
    private List<Item> items = new ArrayList<>();
    private List<Item> selectedItems = new ArrayList<>();

    @FXML
    private TableView<Item> itemTableView;

    @FXML
    private TableView<Item> selectedItemTableView;

    @FXML
    private TableColumn<Item, String> selectedItemNameColumn;

    @FXML
    private TableColumn<Item, String> selectedItemCodeColumn;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        itemObservableList = FXCollections.observableArrayList();
        priceLabel.setText("0 kn");

        try {
            items = Database.fetchAllItems();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        itemObservableList.addAll(items);

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
        selectedItemCodeColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("code"));
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
        selectedItemObservableList.add(item);
        priceLabel.setText(calculateTotalPrice(selectedItemObservableList)+" kn");
    }

    private String calculateTotalPrice(List<Item> items){

        BigDecimal totalPrice = BigDecimal.valueOf(0);

        for(Item item: items){
            totalPrice = totalPrice.add(item.getPrice());

        }

        return totalPrice.toString();
    }

}
