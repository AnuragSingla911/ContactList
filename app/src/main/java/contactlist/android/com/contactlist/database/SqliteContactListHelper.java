package contactlist.android.com.contactlist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jade on 9/4/17.
 */

public class SqliteContactListHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "contactDB";
    private static final int DATABASE_VERSION = 1;

    public SqliteContactListHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactListTable.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
