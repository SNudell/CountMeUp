package database;

import android.provider.BaseColumns;

// a contract defining the layout of our Database Table
public class CounterContract {

    // constructor private because this class shouldn't ever be instantiated
    private CounterContract(){}

    public static class CounterEntry implements BaseColumns {
        public static final String TABLE_NAME = "Counters";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VALUE = "value";

    }


    // Querys

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CounterEntry.TABLE_NAME + " (" +
                    CounterEntry.COLUMN_NAME_NAME + " TEXT," +
                    CounterEntry.COLUMN_NAME_VALUE + " BIGINT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CounterEntry.TABLE_NAME;
}
