    package com.example.waterreminder;
    
    import android.content.Context;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.content.ContentValues;
    import android.database.Cursor;
    
    import androidx.annotation.Nullable;

    import com.example.waterreminder.models.UserInfo;
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
                               String gender, int age, int weight, String activity_level,
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
                cv.put(COLUMN_AGE, age);
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
            if (email == null || email.isEmpty()) return 0;

            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = getReadableDatabase();

                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                String query = "SELECT SUM(" + COL_AMOUNT + ") FROM " + NAME_TABLE +
                        " WHERE " + COLUMN_EMAIL + " = ?" +
                        " AND date(" + COL_TIMESTAMP + ") = ?";

                cursor = db.rawQuery(query, new String[]{email, today});

                if (cursor.moveToFirst() && !cursor.isNull(0)) {
                    return cursor.getInt(0);
                }
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            } finally {
                if (cursor != null) cursor.close();
            }
        }


        public int waterGoal(String email) {
            if (email == null || email.isEmpty()) {
                return 2000; // default goal
            }

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;

            try {
                String query = "SELECT " + COLUMN_WATER_GOAL + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_EMAIL + " = ?";

                cursor = db.rawQuery(query, new String[]{email});

                if (cursor.moveToFirst()) {
                    return cursor.getInt(0);
                } else {
                    return 2000;
                }
            } finally {
                if (cursor != null) cursor.close();
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

        public ArrayList<WaterLogInfo> getAllLogs(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            ArrayList<WaterLogInfo> list = new ArrayList<>();

            Cursor cursor = db.rawQuery(
                    "SELECT " + COL_ID + ", " + COL_AMOUNT + ", " + COL_TIMESTAMP + " FROM " + NAME_TABLE +
                            " WHERE " + COLUMN_EMAIL + " = ?",
                    new String[]{email}
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    int amount = cursor.getInt(1);
                    String timestamp = cursor.getString(2);

                    WaterLogInfo waterLogInfo = new WaterLogInfo(id, amount, timestamp);
                    list.add(waterLogInfo);
                }
                cursor.close();
            }
            return list;
        }

        public ArrayList<UserInfo> getUserInfo(String email){
            ArrayList<UserInfo> infoList = new ArrayList<>();
            SQLiteDatabase database = this.getReadableDatabase();

            String query = "SELECT " + COLUMN_USERNAME + ", " + COLUMN_EMAIL + ", " + COLUMN_GENDER + ", "
                    + COLUMN_WEIGHT + ", " + COLUMN_AGE + ", " + COLUMN_ACTIVITY_LEVEL
                    + ", " + COLUMN_WEATHER + " FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
            Cursor cursor = database.rawQuery(query, new String[]{email});
            if(cursor.moveToFirst()){
                do{
                    infoList.add(new UserInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                            cursor.getInt(4), cursor.getString(5), cursor.getString(6)));
                }while(cursor.moveToNext());
            }
            cursor.close();
            return infoList;
        }
        public boolean updateUserAge(int age, String email){
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_AGE, age);
            return database.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email}) > 0;
        }

        public boolean updateUserWeight(int weight, String email){
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_WEIGHT, weight);
            return database.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email}) > 0;
        }
        public boolean updateUserActivityLevel(String activityLevel, String email){
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ACTIVITY_LEVEL, activityLevel);
            return database.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email}) > 0;
        }
        public boolean updateUserWeather(String weather, String email){
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_WEATHER, weather);
            return database.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email}) > 0;
        }

        public boolean updateWaterGoal(double waterGoal, String email) {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_WATER_GOAL, waterGoal);
            return database.update(TABLE_NAME, cv, COLUMN_EMAIL + " = ?", new String[]{email}) > 0;
        }

        public boolean deleteLog(String logId) {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(NAME_TABLE, COL_ID + " = ?", new String[]{logId}) > 0;
        }

        public boolean clearAllLogs(String email) {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(NAME_TABLE, COLUMN_EMAIL + " = ?", new String[]{email}) > 0;
        }
    }
