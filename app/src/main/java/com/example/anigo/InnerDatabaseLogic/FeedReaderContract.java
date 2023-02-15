package com.example.anigo.InnerDatabaseLogic;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String USER_IDENTIFICATOR = "User";
        public static final String TABLE_NAME = "User";
        public static final String USER_LOGIN = "Login";
        public static final String USER_PASSWORD = "Password";
        public static final String USER_TOKEN = "Token";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        FeedEntry.USER_LOGIN + " TEXT," +
                        FeedEntry.USER_PASSWORD + " TEXT," +
                        FeedEntry.USER_TOKEN + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }
}
