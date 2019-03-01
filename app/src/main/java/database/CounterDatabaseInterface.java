package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import models.Counter;

public class CounterDatabaseInterface {

    private CounterDatabaseHelper helper;
    private SQLiteDatabase db;


    public void DatabaseInterface(Context context) {
        helper = new CounterDatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public void addEntry(Counter counter) {
        ContentValues values = contentValuesFromCounter(counter);
        db.insert(CounterContract.CounterEntry.TABLE_NAME, null, values);
    }

    public void updateEntry(Counter counter) {
        String selection = CounterContract.CounterEntry.COLUMN_NAME_NAME + " = ?";
        String[] args = {counter.getName()};
        ContentValues values = contentValuesFromCounter(counter);
        db.update(CounterContract.CounterEntry.TABLE_NAME, values, selection, args);
    }

    public boolean exists(Counter counter) {
        return existsCounterWithName(counter.getName());
    }

    public boolean existsCounterWithName(String name) {
        String[] projection = {
                CounterContract.CounterEntry.COLUMN_NAME_NAME
        };

        String selection = CounterContract.CounterEntry.COLUMN_NAME_NAME + " = ?";

        String[] args = {name};

        String sortOrder = CounterContract.CounterEntry.COLUMN_NAME_NAME +" DESC";

        Cursor cursor = db.query(
                CounterContract.CounterEntry.TABLE_NAME,
                projection,
                selection,
                args,
                null,
                null,
                sortOrder
        );
        return cursor.getCount() > 0;
    }

    private ContentValues contentValuesFromCounter(Counter counter) {
        ContentValues values = new ContentValues();
        values.put(CounterContract.CounterEntry.COLUMN_NAME_NAME, counter.getName());
        values.put(CounterContract.CounterEntry.COLUMN_NAME_VALUE, counter.get());
        return values;
    }



}
