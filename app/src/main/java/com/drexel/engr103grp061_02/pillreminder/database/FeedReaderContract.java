package com.drexel.engr103grp061_02.pillreminder.database;

import android.provider.BaseColumns;

/**
 * Created by Matthew Rassmann on 4/10/2016.
 */
public final class FeedReaderContract {
    public FeedReaderContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "pills";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_INSRUCTIONS = "instructions";
    }
}
