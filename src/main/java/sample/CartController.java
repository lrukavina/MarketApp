package main.java.sample;

import database.Database;
import enumeration.ItemType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
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
    private User currentUser;

    @FXML
    List<Item> items = new ArrayList<>();

    @FXML
    private TableView<Item> itemTableView;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemCodeColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("code"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<Item, ItemType>("itemType"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("price"));

        if(itemObservableList == null){
            itemObservableList = FXCollections.observableArrayList();
        }
    }

    public void initUser(User user) throws SQLException {
        currentUser = user;
        items = Database.fetchUserItems(currentUser.getId());
        itemObservableList.addAll(items);
        itemTableView.setItems(itemObservableList);
    }

}
