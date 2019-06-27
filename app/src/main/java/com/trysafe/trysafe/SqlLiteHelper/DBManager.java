package com.trysafe.trysafe.SqlLiteHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import com.trysafe.trysafe.Models.NotesModel;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    Context context;
    private static final String DATABASE_NAME = "trysafenotes.db";
    private static final int DATABASE_VERSION = 1;


    private SQLiteDatabase db;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                NotesContract.NoteTable.TABLE_NAME + " ( " +
                NotesContract.NoteTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NotesContract.NoteTable.COLUMN_TITLE + " TEXT, " +
                NotesContract.NoteTable.COLUMN_DESCRIPTION + " TEXT " +
                ")";

        db.execSQL(SQL_CREATE_TABLE);
   //     fill();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotesContract.NoteTable.TABLE_NAME);
        onCreate(db);
    }


    private void fill(){
        NotesModel a = new NotesModel("title","dshdsuid duyd duhsy");
   //     add(a);
    }

//    private void add(NotesModel notesModel){
//        Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();
//        ContentValues c = new ContentValues();
//        c.put(NotesContract.NoteTable.COLUMN_TITLE,notesModel.getTitle());
//        c.put(NotesContract.NoteTable.COLUMN_DESCRIPTION,notesModel.getDescription());
//        db.insert(NotesContract.NoteTable.TABLE_NAME,null,c);
//    }

    public long insert(ContentValues contentValues){
        db = getWritableDatabase();
       long id = db.insert(NotesContract.NoteTable.TABLE_NAME,"",contentValues);

       return id;
    }

    public List<NotesModel> getallNotes(){
        List<NotesModel> notesList = new ArrayList<>();
        db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + NotesContract.NoteTable.TABLE_NAME,null);

        if (c.moveToLast()){
            do {
                NotesModel noteM = new NotesModel();
                noteM.setTitle(c.getString(c.getColumnIndex(NotesContract.NoteTable.COLUMN_TITLE)));
                noteM.setDescription(c.getString(c.getColumnIndex(NotesContract.NoteTable.COLUMN_DESCRIPTION)));
                notesList.add(noteM);
            }while (c.moveToPrevious());
        }

        c.close();
        return notesList;
    }
}
