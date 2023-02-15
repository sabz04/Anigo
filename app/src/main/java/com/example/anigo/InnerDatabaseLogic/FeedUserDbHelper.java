package com.example.anigo.InnerDatabaseLogic;

import static com.example.anigo.InnerDatabaseLogic.FeedReaderContract.FeedEntry.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FeedUserDbHelper implements FeedUserDbContract {

    private FeedReaderDbHelper db_helper;

    public FeedUserDbHelper(Context context) {
        db_helper = new FeedReaderDbHelper(context);
    }

    @Override
    public void Create(String login, String password, String token) {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.USER_LOGIN, login );
        values.put(FeedReaderContract.FeedEntry.USER_PASSWORD, password);
        values.put(FeedReaderContract.FeedEntry.USER_TOKEN, token);
        db.insert(TABLE_NAME, null, values);
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
                String[] selectionArgs = { String.valueOf(id) };
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

}
