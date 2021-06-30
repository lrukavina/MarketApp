package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Receipt{
    private User user;
    private List<Item> items;
    private LocalDate dateIssued;
    private LocalTime timeIssued;
    private BigDecimal price;

    public Receipt(User user, List<Item> items, LocalDate dateIssued, LocalTime timeIssued, BigDecimal price) {
        this.user = user;
        this.items = items;
        this.dateIssued = dateIssued;
        this.timeIssued = timeIssued;
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
