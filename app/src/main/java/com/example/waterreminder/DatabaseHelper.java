package com.example.waterreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.waterreminder.models.WaterLogInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WaterIntake.db";
    private static final int DATABASE_VERSION = 3;

    // User Info Table
    private static final String TABLE_NAME = "user_info";
    private static final String COLUMN_ID = "userID";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "user_password";
    private static final String COLUMN_EMAIL = "user_email";
    private static final String COLUMN_GENDER = "user_gender";
    private static final String COLUMN_WEIGHT = "user_weight";
    private static final String COLUMN_AGE = "user_age";
    private static final String COLUMN_ACTIVITY_LEVEL = "user_activity_level";
    private static final String COLUMN_WATER_GOAL = "user_daily_goal";
    private static final String COLUMN_WEATHER = "current_weather";
    private static final String COLUMN_IS_DELETED = "deleted";

    // Water Log Table
    private static final String NAME_TABLE = "water_log";
    private static final String COL_ID = "id";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_TIMESTAMP = "timestamp";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_AGE + " INTEGER, "
                + COLUMN_WEIGHT + " REAL, "
                + COLUMN_ACTIVITY_LEVEL + " TEXT, "
                + COLUMN_WATER_GOAL + " INTEGER, "
                + COLUMN_WEATHER + " TEXT, "
                + COLUMN_IS_DELETED + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_LOG_TABLE = "CREATE TABLE " + NAME_TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT, "
                + COL_AMOUNT + " INTEGER, "
                + COL_TIMESTAMP + " TEXT)";
        db.execSQL(CREATE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE);
        onCreate(db);
    }

    public boolean addUser(String username, String password, String email,
                           String gender, int weight, String activity_level,
                           double water_goal, String weather) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            if (isUserExists(db, username, email)) {
                return false;
            }

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USERNAME, username);
            cv.put(COLUMN_PASSWORD, password);
            cv.put(COLUMN_EMAIL, email);
            cv.put(COLUMN_GENDER, gender);
            cv.put(COLUMN_WEIGHT, weight);
            cv.put(COLUMN_ACTIVITY_LEVEL, activity_level);
            cv.put(COLUMN_WATER_GOAL, (int) water_goal);
            cv.put(COLUMN_WEATHER, weather);
            cv.put(COLUMN_IS_DELETED, 0);

            long result = db.insert(TABLE_NAME, null, cv);
            return result != -1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private boolean isUserExists(SQLiteDatabase db, String username, String email) {
        Cursor cursor = null;
        try {
            String query = "SELECT 1 FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_USERNAME + "=? OR " + COLUMN_EMAIL + "=?";
            cursor = db.rawQuery(query, new String[]{username, email});
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean searchUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_IS_DELETED + " = 0";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null, "1");
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public void addLog(int amount, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AMOUNT, amount);
        values.put(COLUMN_EMAIL, email);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        values.put(COL_TIMESTAMP, timestamp);
        db.insert(NAME_TABLE, null, values);
        db.close();
    }

    public int getDailyTotal(String email) {
        if (email == null || email.isEmpty()) {
            return 0;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        int total = 0;

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String query = "SELECT SUM(" + COL_AMOUNT + ") FROM " + TABLE_NAME
                + " WHERE " + COLUMN_EMAIL + " = ?"
                + " AND date(" + COL_TIMESTAMP + ") = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, today});
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    public ArrayList<WaterLogInfo> getHistory(String email) {
        ArrayList<WaterLogInfo> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COL_AMOUNT + ", " + COL_TIMESTAMP + " FROM "
                + NAME_TABLE + " WHERE " + COLUMN_EMAIL + " = ?"
                + " ORDER BY " + COL_TIMESTAMP + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                WaterLogInfo waterLogInfo = new WaterLogInfo(cursor.getInt(0), cursor.getString(1));
                historyList.add(waterLogInfo);
            }
            cursor.close();
        }
        db.close();
        return historyList;
    }

    public int waterGoal(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT " + COLUMN_AGE + ", " + COLUMN_WEATHER + " FROM " + NAME_TABLE
                + " WHERE " + COLUMN_EMAIL + " = ?";

        cursor = db.rawQuery(query, new String[]{email});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return 2000;
        }
}
    public boolean softDeleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IS_DELETED, 1);
        int result = db.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();
        return result > 0;
    }
}