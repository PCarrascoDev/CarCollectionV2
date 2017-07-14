package com.desafiolatam.carcollectionv2.model;

import java.io.Serializable;

/**
 * Created by Pedro on 04-07-2017.
 */

public class Car implements Serializable{
    private String key, brand, model, body, photo;
    private int year;
    private boolean abs;

    public Car() {
    }

    public Car(String key, String brand, String model, String body, int year, boolean abs, String photo) {
        this.key = key;
        this.brand = brand;
        this.model = model;
        this.body = body;
        this.year = year;
        this.abs = abs;
        this.photo = photo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isAbs() {
        return abs;
    }

    public void setAbs(boolean abs) {
        this.abs = abs;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
