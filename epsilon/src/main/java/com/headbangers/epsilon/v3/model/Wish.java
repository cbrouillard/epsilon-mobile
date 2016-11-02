package com.headbangers.epsilon.v3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties({"class"})
public class Wish implements Serializable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("webShopUrl")
    private String webShopUrl;
    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;
    @JsonProperty("bought")
    private Boolean bought;
    @JsonProperty("boughtDate")
    private Date boughtDate;
    @JsonProperty("previsionBuy")
    private Date previsionBuy;
    @JsonProperty("account")
    private String account;
    @JsonProperty("category")
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getWebShopUrl() {
        return webShopUrl;
    }

    public void setWebShopUrl(String webShopUrl) {
        this.webShopUrl = webShopUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
    }

    public Date getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(Date boughtDate) {
        this.boughtDate = boughtDate;
    }

    public Date getPrevisionBuy() {
        return previsionBuy;
    }

    public void setPrevisionBuy(Date previsionBuy) {
        this.previsionBuy = previsionBuy;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrevisionBuyFormated() {
        if (previsionBuy != null) {
            return Account.sdf.format(this.previsionBuy);
        }
        return "n/a";
    }
}
