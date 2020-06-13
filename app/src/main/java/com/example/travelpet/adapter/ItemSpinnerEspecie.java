package com.example.travelpet.adapter;

public class ItemSpinnerEspecie {
    private String spinnerItemName;
    private  String spinnerItemImage;

    public ItemSpinnerEspecie(String spinnerItemName, String spinnerItemImage) {
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
