package com.trysafe.trysafe.SqlLiteHelper;

import android.provider.BaseColumns;

public class NotesContract {

    private NotesContract(){
    }

    public static class NoteTable implements BaseColumns{

        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";

    }
}
