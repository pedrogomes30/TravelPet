package com.example.travelpet.adapter;

public class CustomItem {
    private String spinnerItemName;
    private  String spinnerItemImage;

    public CustomItem(String spinnerItemName, String spinnerItemImage) {
        this.spinnerItemName = spinnerItemName;
        this.spinnerItemImage = spinnerItemImage;
    }

    public String getSpinnerItemName() {
        return spinnerItemName;
    }

    public String getSpinnerItemImage() {
        return spinnerItemImage;
    }
}
