package com.headbangers.epsilon.v3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties({"class"})
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @JsonProperty("id")
    private String id;
    @JsonProperty("bank")
    private String bank;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("dateOpened")
    private Date dateOpened;
    @JsonProperty("sold")
    private Double sold;
    @JsonProperty("description")
    private String description;
    @JsonProperty("mobileDefault")
    private boolean mobileDefault;
    @JsonProperty("lastFiveOperations")
    private List<Operation> lastFiveOperations;

    private String formatedDateOpened;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(Date dateOpened) {
        this.dateOpened = dateOpened;
        this.formatedDateOpened = sdf.format(dateOpened);
    }

    public String getFormatedDateOpened() {
        return formatedDateOpened;
    }

    public Double getSold() {
        return sold;
    }

    public void setSold(Double sold) {
        this.sold = sold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMobileDefault() {
        return mobileDefault;
    }

    public void setMobileDefault(boolean mobileDefault) {
        this.mobileDefault = mobileDefault;
    }

    public List<Operation> getLastFiveOperations() {
        return lastFiveOperations;
    }

    public void setLastFiveOperations(List<Operation> lastFiveOperations) {
        this.lastFiveOperations = lastFiveOperations;
    }

}
