package database;

import enumeration.ItemType;
import model.Item;
import model.User;
import security.PasswordEncoder;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
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
                    itemType = ItemType.FOOD;
            }

            Integer quantity = rs.getInt("quantity");
            BigDecimal price = rs.getBigDecimal("price");

            Item item = new Item(id, name, code, itemType, quantity, price);
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
            String username = rs.getString("username");
            String password = rs.getString("password");

            User user = new User(id, name, surname, username, password);
            users.add(user);
        }
        closeConnection(connection);
        return users;
    }

    public static void registerUser(User user) throws SQLException{
        Connection connection = openConnection();
        PasswordEncoder passwordEncoder = new PasswordEncoder();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO USER (NAME, SURNAME, USERNAME, PASSWORD) VALUES (?,?,?,?)");
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getSurname());
        stmt.setString(3, user.getUsername());
        stmt.setString(4, passwordEncoder.encodePassword(user.getPassword()));

        stmt.executeUpdate();
        closeConnection(connection);
    }

    public static List<Item> fetchUserItems(Long userId) throws SQLException{
        List<Item> items = new ArrayList<>();

        Connection connection = openConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT SELECTED_ITEM.* FROM SELECTED_ITEM INNER JOIN\n" +
                "USER_SELECTED_ITEM ON SELECTED_ITEM.ID = SELECTED_ITEM_ID INNER JOIN\n" +
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
                    itemType = ItemType.FOOD;
            }

            Integer quantity = rs.getInt("quantity");
            BigDecimal price = rs.getBigDecimal("price");

            Item item = new Item(id, name, code, itemType, quantity, price);
            items.add(item);
        }
        closeConnection(connection);
        return items;
    }
}
