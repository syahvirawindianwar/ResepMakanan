package com.example.resepmakanan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "resepmakanan.db";

    // User table name
    private static final String TABLE_USER = "pengguna";
    private static final String TABLE_RESEP = "resep";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Transaksi Table Columns names
    private static final String COLUMN_RESEP_ID = "id_resep";
    private static final String COLUMN_RESEP_USERNAME = "username";
    private static final String COLUMN_RESEP_MAKANAN = "makanan";
    private static final String COLUMN_RESEP_PORSI = "porsi";
    private static final String COLUMN_RESEP_DURASI = "durasi";
    private static final String COLUMN_RESEP_BAHAN = "bahan";
    private static final String COLUMN_RESEP_LANGKAH = "langkah";
    private static final String COLUMN_RESEP_DESKRIPSI = "deskripsi";
    private static final String COLUMN_RESEP_FOTO = "foto";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_RESEP_TABLE = "CREATE TABLE " + TABLE_RESEP + "("
            + COLUMN_RESEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_RESEP_USERNAME + " TEXT,"
            + COLUMN_RESEP_MAKANAN + " TEXT,"
            + COLUMN_RESEP_PORSI + " INTEGER,"
            + COLUMN_RESEP_DURASI + " INTEGER,"
            + COLUMN_RESEP_BAHAN + " TEXT,"
            + COLUMN_RESEP_LANGKAH + " TEXT,"
            + COLUMN_RESEP_DESKRIPSI + " TEXT,"
            + COLUMN_RESEP_FOTO + " BLOB" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_RESEP_TABLE = "DROP TABLE IF EXISTS " + TABLE_RESEP;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_RESEP_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_RESEP_TABLE);

        // Create tables again
        onCreate(db);

    }

    public void tambahPengguna(Pengguna pengguna) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, pengguna.getNama());
        values.put(COLUMN_USER_PASSWORD, pengguna.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void tambahResep(Resep resep) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RESEP_USERNAME, resep.getUsername());
        values.put(COLUMN_RESEP_MAKANAN, resep.getMakanan());
        values.put(COLUMN_RESEP_PORSI, resep.getPorsi());
        values.put(COLUMN_RESEP_DURASI, resep.getDurasi());
        values.put(COLUMN_RESEP_BAHAN, resep.getBahan());
        values.put(COLUMN_RESEP_LANGKAH, resep.getLangkah());
        values.put(COLUMN_RESEP_DESKRIPSI, resep.getDeskripsi());
        values.put(COLUMN_RESEP_FOTO, resep.getFoto());

        // Inserting Row
        db.insert(TABLE_RESEP, null, values);
        db.close();
    }

    public List<Pengguna> getAllUser(String username) {

        List<Pengguna> userList = new ArrayList<Pengguna>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_USER
                +" WHERE "+COLUMN_USER_NAME+" = '"+username+"'", null);


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pengguna user = new Pengguna();
                user.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }
    public List<Resep> getAllResep(String username) {
        List<Resep> reseps = new ArrayList<Resep>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM resep"
                +" WHERE username = '"+username+"'", null);

        if (cursor.moveToFirst()) {
            do {
                Resep resep = new Resep();
                resep.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_RESEP_USERNAME)));
                resep.setMakanan(cursor.getString(cursor.getColumnIndex(COLUMN_RESEP_MAKANAN)));
                resep.setBahan(cursor.getString(cursor.getColumnIndex(COLUMN_RESEP_BAHAN)));
                resep.setPorsi(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RESEP_PORSI))));
                resep.setDurasi(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RESEP_DURASI))));
                resep.setLangkah(cursor.getString(cursor.getColumnIndex(COLUMN_RESEP_LANGKAH)));
                resep.setDeskripsi(cursor.getString(cursor.getColumnIndex(COLUMN_RESEP_DESKRIPSI)));
                resep.setFoto(cursor.getBlob(cursor.getColumnIndex(COLUMN_RESEP_FOTO)));
                reseps.add(resep);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return reseps;
    }
    public boolean checkUser(String email) {

        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkUser(String email, String password) {

        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER, //Table to query
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

}
