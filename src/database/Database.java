package database;

import enumeration.ItemType;
import enumeration.UserType;
import model.Item;
import model.Receipt;
import model.User;
import security.PasswordEncoder;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {

    private static final String CONFIG_FILE = "src/database/config.properties";

    private static Connection openConnection() {
        Properties properties = new Properties();
        Connection connection = null;

        try {
            properties.load(new FileReader(CONFIG_FILE));

            String databaseUrl = properties.getProperty("databaseUrl");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

            connection = DriverManager.getConnection(databaseUrl, username, password);

        } catch (IOException | SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    private static void closeConnection(Connection connection){
        try{
            connection.close();
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public static List<Item> fetchAllItems() throws SQLException{
        List<Item> items = new ArrayList<>();

        Connection connection = openConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEM");

        while (rs.next()){
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String code = rs.getString("code");
            String itemTypeString = rs.getString("type");

            ItemType itemType = null;
            switch (itemTypeString){
                case "Food":
                    itemType = ItemType.FOOD; break;
                case "Electronics":
                    itemType = ItemType.ELECTRONICS; break;
            }

            BigDecimal price = rs.getBigDecimal("price");

            Item item = new Item(id, name, code, itemType, price);
            items.add(item);
        }
        closeConnection(connection);
        return items;
    }

    public static List<User> fetchAllUsers() throws SQLException{
        List<User> users = new ArrayList<>();

        Connection connection = openConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM USER");

        while (rs.next()){
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String userTypeString = rs.getString("type");

            UserType userType = null;
            switch (userTypeString){
                case "Admin":
                    userType = UserType.ADMIN; break;
                case "User":
                    userType = UserType.USER; break;
            }

            String username = rs.getString("username");
            String password = rs.getString("password");

            User user = new User(id, name, surname, userType, username, password);
            users.add(user);
        }
        closeConnection(connection);
        return users;
    }

    public static void registerUser(User user) throws SQLException{
        Connection connection = openConnection();
        PasswordEncoder passwordEncoder = new PasswordEncoder();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO USER (NAME, SURNAME, TYPE, USERNAME, PASSWORD) VALUES (?,?,?,?,?)");
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getSurname());
        stmt.setString(3, user.getUserType().getDescription());
        stmt.setString(4, user.getUsername());
        stmt.setString(5, passwordEncoder.encodePassword(user.getPassword()));

        stmt.executeUpdate();
        closeConnection(connection);
    }

    public static List<Item> fetchUserItems(Long userId) throws SQLException{
        List<Item> items = new ArrayList<>();

        Connection connection = openConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT ITEM.* FROM ITEM INNER JOIN\n" +
                "USER_ITEM ON ITEM.ID = ITEM_ID INNER JOIN\n" +
                "USER ON USER_ID = USER.ID\n" +
                "WHERE USER.ID = ?");
        stmt.setLong(1, userId);
        ResultSet rs = stmt.executeQuery();


        while (rs.next()){
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String code = rs.getString("code");
            String itemTypeString = rs.getString("type");

            ItemType itemType = null;
            switch (itemTypeString){
                case "Food":
                    itemType = ItemType.FOOD; break;
                case "Electronics":
                    itemType = ItemType.ELECTRONICS; break;
            }

            BigDecimal price = rs.getBigDecimal("price");

            Item item = new Item(id, name, code, itemType, price);
            items.add(item);
        }
        closeConnection(connection);
        return items;
    }

    public static List<Receipt> fetchAllReceipts() throws SQLException {
        List<Receipt> receipts = new ArrayList<>();
        User user;
        List<Item> items;

        Connection connection = openConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM RECEIPT");

        while (rs.next()){
            long id = rs.getLong("id");
            String name = rs.getString("name");
            long userId = rs.getLong("user_id");
            Date dateIssued = rs.getDate("date_issued");
            Time timeIssued = rs.getTime("time_issued");
            BigDecimal price = rs.getBigDecimal("price");

            user = fetchReceiptUser(userId);
            items = fetchReceiptItems(id);
            LocalDate localDateIssued = dateIssued.toLocalDate();
            LocalTime localTimeIssued = timeIssued.toLocalTime();

            Receipt receipt = new Receipt(name, user, items, localDateIssued, localTimeIssued, price);
            receipts.add(receipt);
        }
        closeConnection(connection);
        return receipts;
    }

    private static List<Item> fetchReceiptItems(Long receiptId) throws SQLException {
        List<Item> items = new ArrayList<>();

        Connection connection = openConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT ITEM.* FROM ITEM INNER JOIN\n" +
                "RECEIPT_ITEM ON ITEM.ID = ITEM_ID INNER JOIN\n" +
                "RECEIPT ON RECEIPT_ID = RECEIPT.ID\n" +
                "WHERE RECEIPT.ID = ?");
        stmt.setLong(1, receiptId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()){
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String code = rs.getString("code");
            String itemTypeString = rs.getString("type");

            ItemType itemType = null;
            switch (itemTypeString){
                case "Food":
                    itemType = ItemType.FOOD; break;
                case "Electronics":
                    itemType = ItemType.ELECTRONICS; break;
            }

            BigDecimal price = rs.getBigDecimal("price");

            Item item = new Item(id, name, code, itemType, price);
            items.add(item);
        }
        closeConnection(connection);
        return items;
    }

    private static User fetchReceiptUser(Long userId) throws SQLException {
        User user = new User();

        Connection connection = openConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM USER WHERE ID = ?");
        stmt.setLong(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()){
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String userTypeString = rs.getString("type");

            UserType userType = null;
            switch (userTypeString){
                case "Admin":
                    userType = UserType.ADMIN; break;
                case "User":
                    userType = UserType.USER; break;
            }

            String username = rs.getString("username");
            String password = rs.getString("password");

            User fetchedUser = new User(id, name, surname, userType, username, password);
            user = fetchedUser;
        }
        closeConnection(connection);
        return user;
    }

    public static void saveReceipt(Receipt receipt) throws SQLException {
        Connection connection = openConnection();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO RECEIPT(NAME, USER_ID, DATE_ISSUED, TIME_ISSUED, PRICE)" +
                "VALUES (?,?,?,?,?)");

        stmt.setString(1, receipt.getName());
        stmt.setLong(2, receipt.getUser().getId());
        stmt.setDate(3, Date.valueOf(receipt.getDateIssued()));
        stmt.setTime(4, Time.valueOf(receipt.getTimeIssued()));
        stmt.setBigDecimal(5, receipt.calculatePrice());

        stmt.executeUpdate();
        Long receiptId = fetchLastReceiptId();

        for (Item item: receipt.getItems()){
            saveReceiptItem(receiptId, item.getId());
        }

        closeConnection(connection);
    }

    private static void saveReceiptItem(Long receiptId, Long itemId) throws SQLException {
        Connection connection = openConnection();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO RECEIPT_ITEM(RECEIPT_ID, ITEM_ID)" +
                "VALUES (?,?)");

        stmt.setLong(1, receiptId);
        stmt.setLong(2, itemId);

        stmt.executeUpdate();
        closeConnection(connection);
    }

    private static Long fetchLastReceiptId() throws SQLException {
        Long receiptId = 0L;

        Connection connection = openConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ID FROM RECEIPT ORDER BY ID DESC LIMIT 1");

        while (rs.next()){
            receiptId = rs.getLong("id");
        }
        closeConnection(connection);
        return receiptId;
    }

    public static Boolean checkItemCode(String code) throws SQLException {
        String codeResult = "";

        Connection connection = openConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT CODE FROM ITEM");

        while(rs.next()){
            codeResult = rs.getString("code");

            if(code.equals(codeResult)){
                closeConnection(connection);
                return true;
            }
            else {
                closeConnection(connection);
                return false;
            }
        }
        closeConnection(connection);
        return false;
    }
}
