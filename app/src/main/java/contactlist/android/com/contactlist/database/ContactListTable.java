package contactlist.android.com.contactlist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;

import contactlist.android.com.contactlist.modal.Contact;

/**
 * Created by jade on 9/4/17.
 */

public class ContactListTable {

    public static final String TABLE_NAME = "contact";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String IS_DELETED = "is_deleted";
    public static final String CREATE_TABLE_QUERY = "create table " + TABLE_NAME
            + "(" + ID + " integer primary key," + NAME + " text," + IS_DELETED + " integer)";

    private SQLiteDatabase db;
    private SqliteContactListHelper helper;
    private OnDBUpdated mUpdateListener;

    private static ContactListTable mInstance;

    public static ContactListTable getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ContactListTable.class) {
                if (mInstance == null) {
                    mInstance = new ContactListTable(context);
                }
            }
        }
        return mInstance;
    }

    public void setmUpdateListener(OnDBUpdated mUpdateListener) {
        this.mUpdateListener = mUpdateListener;
    }

    public interface OnDBUpdated {
        void onInsertNewValues(ArrayList<Contact> list);

        void onDeleteAnyRow(Contact contact, int position);

        void onUndoAnyRow(Contact contact, int position);
    }

    private ContactListTable(Context context) {
        helper = new SqliteContactListHelper(context);
        db = helper.getWritableDatabase();
    }

    public void deleteAnyContact(Contact contact, int position) {
        ContentValues values = new ContentValues();
        values.put(ID, contact.getId());
        values.put(NAME, contact.getName());
        values.put(IS_DELETED, 1);
        db.update(TABLE_NAME, values, ID + " = " + contact.getId(), null);
        if (mUpdateListener != null) {
            mUpdateListener.onDeleteAnyRow(contact, position);
        }
    }

    public void undoAnyContact(Contact contact, int position) {
        ContentValues values = new ContentValues();
        values.put(ID, contact.getId());
        values.put(NAME, contact.getName());
        values.put(IS_DELETED, 0);
        db.update(TABLE_NAME, values, ID + " = " + contact.getId(), null);
        if (mUpdateListener != null) {
            mUpdateListener.onUndoAnyRow(contact, position);
        }
    }

    public void insertContacts(ArrayList<Contact> list) {
        ArrayList<Contact> toShown = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Contact contact = list.get(i);
            if (insert(contact)) {
                toShown.add(contact);
            }
        }
        if (mUpdateListener != null) {
            mUpdateListener.onInsertNewValues(toShown);
        }
    }

    public void fetchAllContacts(){
        Cursor cursor = db.query(TABLE_NAME, null,
                null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ArrayList<Contact> list = new ArrayList<>();
            while(cursor.moveToNext()){
                int isDeleted = cursor.getInt(cursor.getColumnIndex(IS_DELETED));
                if(isDeleted == 0) {
                    Contact contact = new Contact();
                    contact.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                    contact.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    list.add(contact);
                }
            }
            if(mUpdateListener != null){
                mUpdateListener.onInsertNewValues(list);
            }

        }
    }

    private boolean insert(Contact contact) {
        Cursor cursor = db.query(TABLE_NAME, null,
                ID + " = " + contact.getId(), null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int isDeleted = cursor.getInt(cursor.getColumnIndex(IS_DELETED));
            if (isDeleted == 1) {
                return false;
            }
        }

        ContentValues values = new ContentValues();
        values.put(ID, contact.getId());
        values.put(NAME, contact.getName());
        values.put(IS_DELETED, 0);
        db.insert(TABLE_NAME, null, values);
        return true;
    }
}
