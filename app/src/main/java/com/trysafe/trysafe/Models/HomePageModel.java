package com.trysafe.trysafe.Models;

import com.trysafe.trysafe.BloggerModels.Item;

import java.util.List;

public class HomePageModel {
    public static final int HORIZONTAL_JAVA_VIEW = 0;
    public static final int CATEGORIES_VIEW = 1;

    private int type;

    private String title;
    private String label;
    private List<Item> items;
    private List<GridLayoutModel> gridLayoutModelList;

    public HomePageModel(int type, String title,String label, List<Item> items,List<GridLayoutModel> gridLayoutModelList) {
        this.type = type;
        this.title = title;
        this.label = label;
        this.items = items;
        this.gridLayoutModelList = gridLayoutModelList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<GridLayoutModel> getGridLayoutModelList() {
        return gridLayoutModelList;
    }

    public void setGridLayoutModelList(List<GridLayoutModel> gridLayoutModelList) {
        this.gridLayoutModelList = gridLayoutModelList;
    }

    //Grid Layout Model//

}

