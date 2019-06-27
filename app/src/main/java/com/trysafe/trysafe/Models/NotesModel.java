package com.trysafe.trysafe.Models;

public class NotesModel {
    public  String title;
    public  String description;

    public NotesModel(){
    }

    public NotesModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
