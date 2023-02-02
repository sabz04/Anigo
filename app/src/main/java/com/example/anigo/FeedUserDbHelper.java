package com.example.anigo;

import static com.example.anigo.FeedReaderContract.FeedEntry.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FeedUserDbHelper implements FeedUserDbContract {

    FeedReaderDbHelper db_helper;

    public FeedUserDbHelper(Context context) {

        db_helper = new FeedReaderDbHelper(context);

    }

    @Override
    public void Create(String login, String password, String token) {

        SQLiteDatabase db = db_helper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.USER_LOGIN, login );
        values.put(FeedReaderContract.FeedEntry.USER_PASSWORD, password);

        values.put(FeedReaderContract.FeedEntry.USER_TOKEN, token);

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();

        Log.d("User_creating",String.format("user %s created in inner database", login));
    }

    @Override
    public boolean Delete() {
        int id = -1;

        SQLiteDatabase db = db_helper.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "*" },
                null,
                null, null, null, null, null);

        if(cursor != null)
        {
            if (cursor.moveToFirst()) {

                id = cursor.getInt(0);
                String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";
// Specify arguments in placeholder order.
                String[] selectionArgs = { String.valueOf(id) };
// Issue SQL statement.
                int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
                cursor.close();

            }
            else {
                return  false;
            }

        }
        else {
            return false;
        }

        db.close();
        return true;
    }

    @Override
    public FeedUserLocal CheckIfExist() {

        int id = 0;
        String login ="";
        String password = "";
        String token = "";

        SQLiteDatabase db = db_helper.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "*" },
                null,
                null, null, null, null, null);

        if(cursor != null)
        {
            if (cursor.moveToFirst()) {

                id = cursor.getInt(0);
                login = cursor.getString(1); // login
                password = cursor.getString(2); // password
                token = cursor.getString(3); // token
                cursor.close();
            }
            else {
                return null;
            }

        }
        else {
            return null;
        }

        db.close();

        FeedUserLocal user = new FeedUserLocal();

        user.Id = id;
        user.Login = login;
        user.Password = password;
        user.Token = token;

        return user;
    }

    @Override
    public FeedUserLocal GetUser(String login, String password) {
        SQLiteDatabase db = db_helper.getReadableDatabase();
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.USER_LOGIN,
                FeedReaderContract.FeedEntry.USER_PASSWORD,
                FeedReaderContract.FeedEntry.USER_TOKEN
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry.USER_LOGIN + " = ? AND " + FeedReaderContract.FeedEntry.USER_PASSWORD + " = ?";
        String[] selectionArgs = { login, password };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.USER_LOGIN + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        FeedUserLocal user_local = new FeedUserLocal();

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String user_local_login = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.USER_LOGIN));

            String user_local_password = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.USER_LOGIN));

            String user_local_token = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.USER_TOKEN));

            int user_local_id = (int)cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));

            user_local.Id = user_local_id;
            user_local.Login =  user_local_login;
            user_local.Password = user_local_password;
            user_local.Token = user_local_token;

            itemIds.add(user_local);
        }
        cursor.close();

        db.close();

        return (FeedUserLocal) itemIds.get(0);
    }

    @Override
    public String GetToken(String login, String password) {
        return null;
    }

    @Override
    public boolean IsExist() {
        SQLiteDatabase db = db_helper.getReadableDatabase();

        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        if(count>0){
            return true;
        }
        return false;
    }
}
