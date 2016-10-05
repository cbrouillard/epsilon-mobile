package com.headbangers.epsilon.v3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties({"class"})
public class SimpleResult implements Serializable {
    private static final long serialVersionUID = 1L;

    public static String OK = "ok";
    public static String KO = "ko";

    @JsonProperty("code")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isOk() {
        return getCode().equalsIgnoreCase(OK);
    }
}