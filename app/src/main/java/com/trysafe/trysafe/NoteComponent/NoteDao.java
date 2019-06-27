package com.trysafe.trysafe.NoteComponent;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.trysafe.trysafe.DBquery;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM NOTE_TABLE")
    void deleteAllNotes();

    @Query("SELECT * FROM NOTE_TABLE ORDER BY id DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT COUNT(title) FROM NOTE_TABLE")
    LiveData<Integer> getCount();



}
