package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Receipt{
    private String name;
    private String userNameSurname;
    private List<Item> items;
    private LocalDate dateIssued;
    private LocalTime timeIssued;
    private BigDecimal price;

    public Receipt(String userNameSurname, List<Item> items, LocalDate dateIssued, LocalTime timeIssued, BigDecimal price) {
        this.userNameSurname = userNameSurname;
        this.items = items;
        this.dateIssued = dateIssued;
        this.timeIssued = timeIssued;
        this.price = price;
    }

    public Receipt(String name, String userNameSurname, List<Item> items, LocalDate dateIssued, LocalTime timeIssued, BigDecimal price) {
        this.name = name;
        this.userNameSurname = userNameSurname;
        this.items = items;
        this.dateIssued = dateIssued;
        this.timeIssued = timeIssued;
        this.price = price;
    }

    public Receipt() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserNameSurname() {
        return userNameSurname;
    }

    public void setUserNameSurname(String userNameSurname) {
        this.userNameSurname = userNameSurname;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued = dateIssued;
    }

    public LocalTime getTimeIssued() {
        return timeIssued;
    }

    public void setTimeIssued(LocalTime timeIssued) {
        this.timeIssued = timeIssued;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public BigDecimal calculatePrice(){

        BigDecimal price = BigDecimal.valueOf(0);

        for (Item item: this.getItems()){
            price = price.add(item.getPrice());
        }

        return price;
    }
}
