package main.test;

import enumeration.ItemType;
import enumeration.UserType;
import model.Item;
import model.User;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {

        Item item = new Item(10L, "test", ItemType.ELECTRONICS, 1, BigDecimal.valueOf(1));
        User user = new User(1L, "adminName", "adminSurname", UserType.ADMIN, "admin","123" );
        System.out.println(item.getCode());
        System.out.println(user);
    }
}
