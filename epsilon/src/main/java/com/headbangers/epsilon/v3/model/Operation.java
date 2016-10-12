package com.headbangers.epsilon.v3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@JsonIgnoreProperties({"class"})
public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("tiers")
    private String tiers;
    @JsonProperty("category")
    private String category;
    @JsonProperty("note")
    private String note;
    @JsonProperty("dateApplication")
    private Date dateApplication;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("sign")
    private String sign;
    @JsonProperty("pointed")
    private boolean pointed;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;

    private String formatedDateApplication;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTiers() {
        return tiers;
    }

    public void setTiers(String tiers) {
        this.tiers = tiers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDateApplication() {
        return dateApplication;
    }

    public void setDateApplication(Date dateApplication) {
        this.dateApplication = dateApplication;
        if (dateApplication != null)
            this.formatedDateApplication = sdf.format(dateApplication);
    }

    public String getFormatedDateApplication() {
        return formatedDateApplication;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean isPointed() {
        return pointed;
    }

    public void setPointed(boolean pointed) {
        this.pointed = pointed;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
