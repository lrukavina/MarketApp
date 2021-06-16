package model;

import enumeration.ItemType;

import java.math.BigDecimal;
import java.util.Random;

public class Item {
    private Long id;
    private String name;
    private String code;
    private ItemType itemType;
    private Integer quantity;
    private BigDecimal price;

    public Item(Long id, String name, String code, ItemType itemType, Integer quantity, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.itemType = itemType;
        this.quantity = quantity;
        this.price = price;
    }

    public Item(Long id, String name, ItemType itemType, Integer quantity, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.itemType = itemType;
        this.code = generateCode(itemType);
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", itemType=" + itemType +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    private String generateCode(ItemType itemType){
        String code = "";
        Random random = new Random();

        switch (itemType){
            case FOOD -> code = "FD";
        }

        return code += String.format("%04d", random.nextInt(10000));
    }
}
