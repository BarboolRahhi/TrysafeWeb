package com.trysafe.trysafe.NoteComponent;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.trysafe.trysafe.DBquery;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private String userUid;



    public Note(String title, String description, String userUid) {
        this.title = title;
        this.description = description;
        this.userUid = userUid;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}

