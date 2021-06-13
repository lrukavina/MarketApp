package model;

import enumeration.ArticleType;

import java.math.BigDecimal;

public class Article {
    private Long id;
    private String name;
    private ArticleType articleType;
    private Integer quantity;
    private BigDecimal price;

    public Article(Long id, String name, ArticleType articleType, Integer quantity, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.articleType = articleType;
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

    public ArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
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
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", articleType=" + articleType +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
