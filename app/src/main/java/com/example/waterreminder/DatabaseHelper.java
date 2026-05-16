package com.example.waterreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //columns of etc
    private static final String COLUMN_ID = "userID";
    private static final String TABLE_NAME = "user_info";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "user_password";
    private static final String COLUMN_EMAIL = "user_email";
    private static final String COLUMN_GENDER = "user_gender";
    private static final String COLUMN_WEIGHT = "user_weight";
    private static final String COLUMN_AGE = "user_age";
    private static final String COLUMN_ACTIVITY_LEVEL = "user_activity_level";
    private static final String COLUMN_WATER_GOAL="user_daily_goal";
    private static final String COLUMN_IS_DELETED="deleted";
    private static final String DATABASE_NAME = "WaterIntake.db";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_WEIGHT + " REAL, "
                + COLUMN_ACTIVITY_LEVEL + " TEXT, "
                + COLUMN_WATER_GOAL + " INTEGER, "
                + COLUMN_IS_DELETED + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean addUser(String user, String pass, String email,
                           String gender, double weight, String activity_level, int water_goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, user);
        cv.put(COLUMN_PASSWORD, pass);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_IS_DELETED, 0);
        cv.put(COLUMN_ACTIVITY_LEVEL, activity_level);
        cv.put(COLUMN_WATER_GOAL, water_goal);

        long result = db.insert(TABLE_NAME, null, cv);

        db.close();

        return result != -1;
    }

    boolean searchUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { COLUMN_ID };

        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_IS_DELETED + " = 0";

        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                "1"
        );

        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }
    public boolean softDeleteUser(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_IS_DELETED, 1);

        int result = db.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();
        return result > 0;
    }
}
