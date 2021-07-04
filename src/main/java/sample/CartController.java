package main.java.sample;

import com.itextpdf.text.DocumentException;
import database.Database;
import enumeration.ItemType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import pdf.GeneratePdf;
import settings.SettingsLoader;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CartController implements Initializable {

    private static ObservableList<Item> itemObservableList;
    private static ObservableList<Item> allItemsObservableList;
    private static ObservableList<Item> selectedItemObservableList;
    private static ObservableList<String> searchByObservableList;
    private static User currentUser;
    private static Item selectedItem = new Item();
    private List<Item> items = new ArrayList<>();
    private List<Item> selectedItems = new ArrayList<>();
    private SettingsLoader settingsLoader = new SettingsLoader();
    private Properties settings = new Properties();

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

        try {
            settings = settingsLoader.loadSettings();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Settings error");
            alert.setHeaderText("Error loading settings");
            alert.setContentText("Settings could not be loaded. Please check does the file exist.");
            alert.showAndWait();
        }

        itemObservableList = FXCollections.observableArrayList();
        allItemsObservableList = FXCollections.observableArrayList();
        searchByObservableList = FXCollections.observableArrayList();
        priceLabel.setText("0 "+settings.getProperty("currency"));

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
        selectedItemTableView.setPlaceholder(new Label("Cart is empty, please select items"));
        selectedItemTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    removeItemFromCart(selectedItem);
                }
            }
        });

        selectedItemTableView.setRowFactory(SelectedItemTableView -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())){
                    Item item = row.getItem();
                    selectItem(item);
                }
            });
            return row;
        });


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
                    addItemToCart(item);
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
        selectedItem = item;
    }


    private void addItemToCart(Item item){

        boolean itemFound = false;

        for (Item selectedItem: selectedItemObservableList){
            if(selectedItem.equals(item)){

                selectedItem.setQuantity(selectedItem.getQuantity() + 1);
                selectedItem.calculatePrice();

                List<Item> refreshCartItems = new ArrayList<>();
                refreshCartItems.addAll(selectedItemObservableList);
                selectedItemObservableList.clear();

                selectedItemObservableList.addAll(refreshCartItems);
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
        
        priceLabel.setText(calculateTotalPrice(selectedItemObservableList)+" "+settings.getProperty("currency"));
    }

    private void removeItemFromCart(Item item){
        List<Item> refreshCartItems = new ArrayList<>();

        if(item.getQuantity() > 1){
            item.setQuantity(item.getQuantity() - 1);
        }
        else {
            selectedItemObservableList.remove(item);
        }

        refreshCartItems.addAll(selectedItemObservableList);
        selectedItemObservableList.clear();
        selectedItemObservableList.addAll(refreshCartItems);

        priceLabel.setText(calculateTotalPrice(selectedItemObservableList)+" "+settings.getProperty("currency"));
    }

    private String calculateTotalPrice(List<Item> items){

        BigDecimal totalPrice = BigDecimal.valueOf(0);

        for(Item item: items){
            item.calculatePrice();
            totalPrice = totalPrice.add(item.getPrice());

        }

        return totalPrice.toString();
    }

    @FXML
    public void finishPurchase() throws IOException, DocumentException, SQLException {

        if(calculateTotalPrice(selectedItemObservableList).equals("0")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot finish purchase");
            alert.setHeaderText("Cart is empty");
            alert.setContentText("Cannot finish purchase because cart is empty, please select items " +
                    "by double clicking them");
            alert.showAndWait();
            return;
        }

        GeneratePdf generatePdf = new GeneratePdf();
        LocalDate dateIssued = LocalDate.now();
        LocalTime timeIssued = LocalTime.now();
        String receiptName = "RT-"+dateIssued+timeIssued.getHour()+timeIssued.getHour()+timeIssued.getSecond();

        String userNameSurname = currentUser.getName() + " " + currentUser.getSurname();
        Receipt receipt = new Receipt(userNameSurname, selectedItemObservableList,
                LocalDate.now(), LocalTime.now(),
                BigDecimal.valueOf(Double.parseDouble(calculateTotalPrice(selectedItemObservableList))));
        receipt.setName(receiptName);

        Database.saveReceipt(receipt);
        generatePdf.generateReceipt(receipt);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finished purchase");
        alert.setHeaderText("Finished purchase and saved receipt");
        alert.setContentText("Purchase has been finished on "+ LocalDate.now()+" " +
                ""+LocalTime.now().getHour()+":"+LocalTime.now().getHour()+":"+LocalTime.now().getSecond()+" and the receipt " +
                "has been saved");
        alert.showAndWait();

        MainMenuController mainMenuController = new MainMenuController();
        mainMenuController.showCart();
    }

}
