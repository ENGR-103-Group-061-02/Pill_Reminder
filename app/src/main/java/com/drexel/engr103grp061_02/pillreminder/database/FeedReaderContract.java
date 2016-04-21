package com.drexel.engr103grp061_02.pillreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

/**
 * Created by Matthew Rassmann on 4/10/2016.
 */
public final class FeedReaderContract {
    public FeedReaderContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "pills";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_HOURS = "hours";
        public static final String COLUMN_NAME_MINUTES = "minutes";
        public static final String COLUMN_NAME_INSRUCTIONS = "instructions";

        public static final String TEXT_TYPE = " TEXT";
        public static final String INTEGER_TYPE = " INTEGER";
        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_QUANTITY + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_HOURS + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_MINUTES + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_INSRUCTIONS + TEXT_TYPE + COMMA_SEP + " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    }

    public class FeedReaderDbHelper extends SQLiteOpenHelper {

        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(FeedReaderContract.FeedEntry.SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(FeedReaderContract.FeedEntry.SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public void addData(SQLiteDatabase db, int id, String name, int quantity, int hours, int minutes, String instructions) {

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(FeedEntry._ID, id);
            values.put(FeedEntry.COLUMN_NAME_NAME, name);
            values.put(FeedEntry.COLUMN_NAME_QUANTITY, quantity);
            values.put(FeedEntry.COLUMN_NAME_HOURS, hours);
            values.put(FeedEntry.COLUMN_NAME_MINUTES, minutes);
            values.put(FeedEntry.COLUMN_NAME_INSRUCTIONS, instructions);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(
                    FeedEntry.TABLE_NAME,
                    "null",
                    values);
        }

        public void deleteData(SQLiteDatabase db, String name) {
            // Define 'where' part of query.
            String selection =  FeedEntry.COLUMN_NAME_NAME+" LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {name};
            // Issue SQL statement.
            db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
        }

        public void updatePill(Pill oldPill, Pill newPill, SQLiteDatabase db){
            String oldName = oldPill.getName();
            int oldHours = oldPill.getHours();
            int oldMinutes = oldPill.getMinutes();
            String newName = newPill.getName();
            int newQuantity = newPill.getQuantity();
            int newHours = newPill.getHours();
            int newMinutes = newPill.getMinutes();

            String newInstructions = newPill.getInstructions();

            SQLiteStatement getRowIDFromNameAndTime;
            String sql = "select "+FeedEntry._ID+" in " + FeedEntry.TABLE_NAME +
                    " where " + FeedEntry.COLUMN_NAME_NAME + "='" + oldName + "' and " + FeedEntry.COLUMN_NAME_HOURS
                    + "= ? and " + FeedEntry.COLUMN_NAME_MINUTES +
                    "= ?";
            getRowIDFromNameAndTime = db.compileStatement(sql);
            getRowIDFromNameAndTime.bindLong(1, oldHours);
            getRowIDFromNameAndTime.bindLong(2, oldMinutes);
            int rowId = (int) getRowIDFromNameAndTime.executeInsert();

            ContentValues values = new ContentValues();
            values.put(FeedEntry.COLUMN_NAME_NAME, newName);
            values.put(FeedEntry.COLUMN_NAME_QUANTITY, newQuantity);
            values.put(FeedEntry.COLUMN_NAME_HOURS, newHours);
            values.put(FeedEntry.COLUMN_NAME_MINUTES, newMinutes);
            values.put(FeedEntry.COLUMN_NAME_INSRUCTIONS, newInstructions);

            // Which row to update, based on the ID
            String selection = FeedEntry._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(rowId) };

            int count = db.update(
                    FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        }

        public void updateName(SQLiteDatabase db, String currName, String newName){
            SQLiteStatement getRowIDFromName;
            String sql = "select "+FeedEntry._ID+" in " + FeedEntry.TABLE_NAME +
                    " where " + FeedEntry.COLUMN_NAME_NAME + "='" + currName + "'";
            getRowIDFromName = db.compileStatement(sql);
            int rowId = (int) getRowIDFromName.executeInsert();
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(FeedEntry.COLUMN_NAME_NAME, newName);

            // Which row to update, based on the ID
            String selection = FeedEntry._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(rowId) };

            int count = db.update(
                    FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

        public void updateQuantity(SQLiteDatabase db, long currQuantity, int newQuantity){
            SQLiteStatement getRowIDFromQuantity;
            String sql = "select "+FeedEntry._ID+" in " + FeedEntry.TABLE_NAME +
                    " where " + FeedEntry.COLUMN_NAME_QUANTITY + "= ?";
            getRowIDFromQuantity = db.compileStatement(sql);
            getRowIDFromQuantity.bindLong(1, currQuantity);
            int rowId = (int) getRowIDFromQuantity.executeInsert();
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(FeedEntry.COLUMN_NAME_QUANTITY, newQuantity);

            // Which row to update, based on the ID
            String selection = FeedEntry._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(rowId) };

            int count = db.update(
                    FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

        public void updateTime(SQLiteDatabase db, long currHours, long currMinutes, int newHours, int newMinutes){
            SQLiteStatement getRowIDFromTime;
            String sql = "select "+FeedEntry._ID+" in " + FeedEntry.TABLE_NAME +
                    " where " + FeedEntry.COLUMN_NAME_HOURS  + "= ? and " + FeedEntry.COLUMN_NAME_MINUTES +
                    "= ?";
            getRowIDFromTime = db.compileStatement(sql);
            getRowIDFromTime.bindLong(1, currHours);
            getRowIDFromTime.bindLong(2, currMinutes);
            int rowId = (int) getRowIDFromTime.executeInsert();
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(FeedEntry.COLUMN_NAME_HOURS, newHours);
            values.put(FeedEntry.COLUMN_NAME_MINUTES, newMinutes);

            // Which row to update, based on the ID
            String selection = FeedEntry._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(rowId) };

            int count = db.update(
                    FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }
}
