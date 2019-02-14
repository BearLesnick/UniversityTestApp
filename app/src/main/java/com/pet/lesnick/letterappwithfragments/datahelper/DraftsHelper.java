package com.pet.lesnick.letterappwithfragments.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pet.lesnick.letterappwithfragments.model.Draft;

public class DraftsHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "letter_drafts";
    private static final String COL1 = "id";
    private static final String COL2 = "header";
    private static final String COL3 = "content";


    public DraftsHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT , header TEXT, content TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addData(Draft draft) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, draft.getHeader());
        contentValues.put(COL3, draft.getContent());
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_NAME, null, contentValues);
        // if date is inserted incorrectly, it will return -1
        if (result == -1) {
        }
    }

    public void deleteData(Draft draft) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COL1+" = " + draft.getId(),null);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}