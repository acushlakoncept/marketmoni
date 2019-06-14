package com.acushlakoncept.marketmoni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelperAgents extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserInfo.db";

    // User table name
    private static final String TABLE_USER = "users";
    private static final String TABLE_AGENTS = "agents";


    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_AGENT_EMAIL_REF = "agent_email";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_SEX = "user_sex";
    private static final String COLUMN_USER_ADDRESS = "user_address";
    private static final String COLUMN_USER_PHONE = "user_phone";
    private static final String COLUMN_USER_TYPE = "biz_type";
    private static final String COLUMN_USER_LOCATION = "biz_location";
    private static final String COLUMN_USER_AREA = "biz_area";
    private static final String COLUMN_USER_IMAGE_PATH = "user_image_path";
    public static final String COLUMN_USER_DATE = "reg_date";

    private static final String COLUMN_AGENT_ID = "agent_id";
    private static final String COLUMN_AGENT_NAME = "agent_name";
    private static final String COLUMN_AGENT_SEX = "agent_sex";
    private static final String COLUMN_AGENT_ADDRESS = "agent_address";
    private static final String COLUMN_AGENT_PHONE = "agent_phone";
    private static final String COLUMN_AGENT_EMAIL = "agent_email";
    private static final String COLUMN_AGENT_PASSWORD = "agent_password";
    private static final String COLUMN_AGENT_IMAGE_PATH = "agent_image_path";
    public static final String COLUMN_AGENT_DATE = "reg_date";


    static long dbInsert ;
    //private final Context myContext;


    // Create user table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_AGENT_EMAIL_REF + " TEXT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_SEX + " TEXT, "
            + COLUMN_USER_ADDRESS + " TEXT, "
            + COLUMN_USER_PHONE + " TEXT, "
            + COLUMN_USER_TYPE + " TEXT, "
            + COLUMN_USER_LOCATION + " TEXT, "
            + COLUMN_USER_AREA + " TEXT, "
            + COLUMN_USER_IMAGE_PATH + " BLOB, "
            + COLUMN_USER_DATE + " TEXT )";


    // Create user table sql query
    private String CREATE_AGENT_TABLE = "CREATE TABLE " + TABLE_AGENTS + "("
            + COLUMN_AGENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_AGENT_NAME + " TEXT, "
            + COLUMN_AGENT_SEX + " TEXT, "
            + COLUMN_AGENT_ADDRESS + " TEXT, "
            + COLUMN_AGENT_PHONE + " TEXT, "
            + COLUMN_AGENT_EMAIL + " TEXT, "
            + COLUMN_AGENT_PASSWORD + " TEXT, "
            + COLUMN_AGENT_IMAGE_PATH + " BLOB, "
            + COLUMN_AGENT_DATE + " TEXT )";

    // Drop user table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    // Drop agent table sql query
    private String DROP_AGENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_AGENTS;

    // Constructor
    public DatabaseHelperAgents(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_AGENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // on upgrade drop older tables
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_AGENT_TABLE);

        // Create new tables again
        onCreate(db);
    }

    //This method is to create new user on registration
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_SEX, user.getSex());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_AGENT_EMAIL_REF, user.getEmail());
        values.put(COLUMN_USER_TYPE, user.getBizType());
        values.put(COLUMN_USER_LOCATION, user.getBizLocation());
        values.put(COLUMN_USER_AREA, user.getBizArea());
        values.put(COLUMN_USER_IMAGE_PATH, user.getImageUrl());
        values.put(COLUMN_USER_DATE, user.getDateTime());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    //This method is to create new user on registration
    public void addAgent(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AGENT_NAME, user.getName());
        values.put(COLUMN_AGENT_SEX, user.getSex());
        values.put(COLUMN_AGENT_ADDRESS, user.getAddress());
        values.put(COLUMN_AGENT_PHONE, user.getPhone());
        values.put(COLUMN_AGENT_EMAIL, user.getEmail());
        values.put(COLUMN_AGENT_PASSWORD, user.getPassword());
        values.put(COLUMN_AGENT_IMAGE_PATH, user.getImageUrl());
        values.put(COLUMN_AGENT_DATE, user.getDateTime());

        // Inserting Row
        db.insert(TABLE_AGENTS, null, values);
        db.close();
    }


    public String getImagePath(){
        SQLiteDatabase db = this.getReadableDatabase();
        String imagePath = "";

        String select_query = "SELECT " + COLUMN_USER_IMAGE_PATH + " FROM " + TABLE_USER +
                " WHERE " + COLUMN_USER_ID + " = " + String.valueOf(dbInsert) ;
        Cursor cursor = db.rawQuery(select_query,null);
        int iPicPath = cursor.getColumnIndex(COLUMN_USER_IMAGE_PATH);

        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            imagePath = cursor.getString(iPicPath);

        }
        return imagePath;
    }


    // This method to update user record
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_SEX, user.getSex());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        values.put(COLUMN_USER_PHONE, user.getPhone());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //This method delete user record
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " =?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //This method checks if user exist or not
    public boolean isBeneficiaryRegistered(String phone) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_PHONE
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection  = COLUMN_USER_PHONE + " =?";

        // slection argument
        String[] selectionArgs = {phone};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'name@marketmoni.com';
         */
        Cursor cursor = db.query(TABLE_USER, // Table to query
                columns,        //columns to return
                selection,      //The column for the WHERE clause
                selectionArgs,  // The values for the WHERE cluase
                null,   // group the rows
                null,   // filter by row groups
                null    // the sort order
        );
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }

        return false;

    }

    //This method checks if user exist or not
    public boolean checkAgent(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_AGENT_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection  = COLUMN_AGENT_EMAIL + " =?";

        // slection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'name@marketmoni.com';
         */
        Cursor cursor = db.query(TABLE_AGENTS, // Table to query
                columns,        //columns to return
                selection,      //The column for the WHERE clause
                selectionArgs,  // The values for the WHERE cluase
                null,   // group the rows
                null,   // filter by row groups
                null    // the sort order
        );
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }

        return false;

    }


    //This method to check user exist or not
    public boolean checkAgent(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_AGENT_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_AGENT_EMAIL + " = ?" + " AND " + COLUMN_AGENT_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_AGENTS, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    public List<User> getAgent(String email) {

        String[] columns = {
                COLUMN_AGENT_ID,
                COLUMN_AGENT_NAME,
                COLUMN_AGENT_SEX,
                COLUMN_AGENT_ADDRESS,
                COLUMN_AGENT_PHONE,
                COLUMN_AGENT_EMAIL,
                COLUMN_AGENT_PASSWORD
        };


        String sql = "SELECT * FROM " + TABLE_AGENTS
                + " WHERE " + this.COLUMN_AGENT_EMAIL + " = " + email;

        // selection criteria
        String selection = COLUMN_AGENT_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_AGENTS,
                columns,
                COLUMN_AGENT_EMAIL,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_NAME)));
                user.setSex(cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_SEX)));
                user.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_ADDRESS)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_PHONE)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_PASSWORD)));
                // Adding user record to list
                userList.add(user);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public byte[] insertImage(String img) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            FileInputStream fs = new FileInputStream(img);
            byte[] imgbyte = new byte[fs.available()];
            fs.read(imgbyte);
            return  imgbyte;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getAgentBitmapImage(String email){
        SQLiteDatabase db = getWritableDatabase();
        Bitmap bt = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AGENTS + " WHERE " + COLUMN_AGENT_EMAIL + "=?", new String[]{email});
        if (cursor.moveToNext()){
            byte[] imag = cursor.getBlob(cursor.getColumnIndex(COLUMN_AGENT_IMAGE_PATH));
            bt = BitmapFactory.decodeByteArray(imag, 0, imag.length);
        }

        return bt;
    }

    public Bitmap getUserBitmapImage(String phone){
        SQLiteDatabase db = getWritableDatabase();
        Bitmap bt = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_PHONE + "=?", new String[]{phone});
        if (cursor.moveToNext()){
            byte[] imag = cursor.getBlob(cursor.getColumnIndex(COLUMN_AGENT_IMAGE_PATH));
            bt = BitmapFactory.decodeByteArray(imag, 0, imag.length);
        }

        return bt;
    }



    // This method is retrieve all user and return the list of user records
    public HashMap<String, String> getAllAgentInfo(String email) {
        String[] columns = {
                COLUMN_AGENT_ID,
                COLUMN_AGENT_NAME,
                COLUMN_AGENT_SEX,
                COLUMN_AGENT_ADDRESS,
                COLUMN_AGENT_PHONE,
                COLUMN_AGENT_EMAIL,
                COLUMN_AGENT_IMAGE_PATH,
                COLUMN_AGENT_DATE
                //COLUMN_USER_PASSWORD
        };

        // sorting orders
        String sortOrders =
                COLUMN_AGENT_NAME + " ASC";
        HashMap<String, String> userInfo = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_AGENT_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query the user table
        /**
         *  Here query function is used to fetch records from user table this function work
         *  SQL query equivalent to this query function is
         *  SELECT user_id, user_name, user_sex.... FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_AGENTS, //Table to query
                columns,  // columns to return
                selection, // columns for the WHERE clause, we use null since nothing is compared
                selectionArgs, // The values for the WHERE clause
                null, // group the rows
                null, // filter by row groups
                sortOrders //the sort order
        );

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                userInfo.put("ID", cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_ID)));
                userInfo.put("Name", cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_NAME)));
                userInfo.put("Sex", cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_SEX)));
                userInfo.put("Address", cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_ADDRESS)));
                userInfo.put("Phone", cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_PHONE)));
                userInfo.put("Email", cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_EMAIL)));
                //userInfo.put("ImageUrl", cursor.getString(cursor.getColumnIndex(COLUMN_USER_IMAGE_PATH)));
                userInfo.put("Date", cursor.getString(cursor.getColumnIndex(COLUMN_USER_DATE)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userInfo;
    }

    public static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

}
