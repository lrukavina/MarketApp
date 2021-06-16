package main.test;

import enumeration.ItemType;
import model.Item;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {

        Item item = new Item(10L, "test", ItemType.FOOD, 1, BigDecimal.valueOf(1));
        System.out.println(item.getCode());
    }
}
