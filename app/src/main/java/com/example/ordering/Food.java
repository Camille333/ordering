package com.example.ordering;

import java.math.BigDecimal;

public class Food {
    private String food_name;
    private BigDecimal food_price;
    private int food_count;
    private byte[] food_image;

    public BigDecimal getFood_price() {
        return food_price;
    }

    public void setFood_price(BigDecimal food_price) {
        this.food_price = food_price;
    }

    public int getFood_count() {
        return food_count;
    }

    public void setFood_count(int food_count) {
        this.food_count = food_count;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public byte[] getFood_image() {
        return food_image;
    }

    public void setFood_image(byte[] food_image) {
        this.food_image = food_image;
    }
}
