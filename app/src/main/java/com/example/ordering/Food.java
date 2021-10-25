package com.example.ordering;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Food implements Parcelable {
    private String food_name;
    private float food_price;
    private int food_count;

    public Food(String food_name, float food_price, int food_count) {
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_count = food_count;
    }

    public float getFood_price() {
        return food_price;
    }

    public void setFood_price(float food_price) {
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

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel parcel) {
            return new Food(parcel.readString(), parcel.readFloat(), parcel.readInt());
        }

        @Override
        public Food[] newArray(int i) {
            return new Food[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(food_name);
        parcel.writeFloat(food_price);
        parcel.writeInt(food_count);
    }
}
