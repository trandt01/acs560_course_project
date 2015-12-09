package com.dtran.testtemplate1;

import java.util.Date;

public class Account {
    private long id;
    private double weight;
    private double height;
    private double body_fat;
    private String create_date;
    private String update_date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getBodyFat() {
        return body_fat;
    }

    public void setBodyFat(double body_fat) {
        this.body_fat = body_fat;
    }

    public String getCreateDate() {
        return create_date;
    }

    public void setCreateDate(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdateDate() {
        return update_date;
    }

    public void setUpdateDate(String update_date) {
        this.update_date = update_date;
    }

}
