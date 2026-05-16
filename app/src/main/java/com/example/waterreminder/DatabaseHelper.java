package com.example.waterreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //columns of etc

    private static final String TABLE_NAME = "heron_User";
    private static final String COLUMN_PASSWORD = "user_password";
    private static final String COLUMN_EMAIL = "user_email";
    private static final String COLUMN_BIRTHDAY = "user_birthday";
    private static final String COLUMN_GENDER = "user_gender";
    private static final String COLUMN_WEIGHT = "user_weight";
    private static final String COLUMN_GOAL = "user_goal";
    private static final String COLUMN_ACTIVITY_LEVEL = "user_activity_level";
    private static final String COLUMN_PROFILE_IMAGE = "user_profile_image";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAM, null, DATABASE_VERSION);
    }
}
