package com.acushlakoncept.marketmoni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgentDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AgentsInfo.db";

    // User table name
    private static final String TABLE_AGENT = "agents";


    // Agent Table Columns names
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
    private String CREATE_AGENT_TABLE = "CREATE TABLE " + TABLE_AGENT + "("
            + COLUMN_AGENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_AGENT_NAME + " TEXT, "
            + COLUMN_AGENT_SEX + " TEXT, "
            + COLUMN_AGENT_ADDRESS + " TEXT, "
            + COLUMN_AGENT_PHONE + " TEXT, "
            + COLUMN_AGENT_EMAIL + " TEXT, "
            + COLUMN_AGENT_PASSWORD + " TEXT, "
            + COLUMN_AGENT_IMAGE_PATH + " BLOB, "
            + COLUMN_AGENT_DATE + " TEXT )";

    // Drop agent table sql query
    private String DROP_AGENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_AGENT;

    // Constructor
    public AgentDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AGENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // on upgrade drop older tables
        db.execSQL(DROP_AGENT_TABLE);

        // Create new tables again
        onCreate(db);
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
        db.insert(TABLE_AGENT, null, values);
        db.close();
    }

    public String getImagePath(){
        SQLiteDatabase db = this.getReadableDatabase();
        String imagePath = "";

        String select_query = "SELECT " + COLUMN_AGENT_IMAGE_PATH + " FROM " + TABLE_AGENT +
                " WHERE " + COLUMN_AGENT_ID + " = " + String.valueOf(dbInsert) ;
        Cursor cursor = db.rawQuery(select_query,null);
        int iPicPath = cursor.getColumnIndex(COLUMN_AGENT_IMAGE_PATH);

        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            imagePath = cursor.getString(iPicPath);

        }
        return imagePath;
    }


    //This method checks if user exist or not
    public boolean checkAgent(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_AGENT_ID        };
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
        Cursor cursor = db.query(TABLE_AGENT, // Table to query
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
        Cursor cursor = db.query(TABLE_AGENT, //Table to query
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

    public Bitmap getBitmapImage(String email){
        SQLiteDatabase db = getWritableDatabase();
        Bitmap bt = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AGENT + " WHERE " + COLUMN_AGENT_EMAIL + "=?", new String[]{email});
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
        Cursor cursor = db.query(TABLE_AGENT, //Table to query
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
                //userInfo.put("Type", cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_IMAGE_PATH)));
                userInfo.put("Date", cursor.getString(cursor.getColumnIndex(COLUMN_AGENT_DATE)));

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
