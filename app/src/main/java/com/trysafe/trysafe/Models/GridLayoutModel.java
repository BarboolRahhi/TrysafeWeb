package com.trysafe.trysafe.Models;

public class GridLayoutModel {

    private String title;
    private String label;
    private String image;


    public GridLayoutModel(String title, String label, String image) {
        this.title = title;
        this.label = label;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
