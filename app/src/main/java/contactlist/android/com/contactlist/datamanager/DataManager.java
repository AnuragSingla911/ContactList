package contactlist.android.com.contactlist.datamanager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.ArrayList;

import contactlist.android.com.contactlist.database.ContactListTable;
import contactlist.android.com.contactlist.modal.Contact;
import contactlist.android.com.contactlist.server.ServerInteractor;
import contactlist.android.com.contactlist.utility.Utils;

/**
 * Created by jade on 9/4/17.
 */

public class DataManager implements ContactListTable.OnDBUpdated {

    private Context mContext;
    private ContactListTable mDatabaseHandler;
    private OnDataLoadedReceiver mListener;
    private Handler uiHandler;

    public void setmListener(OnDataLoadedReceiver mListener) {
        this.mListener = mListener;
        if(mListener == null){
            mDatabaseHandler.setmUpdateListener(null);
        }else{
            mDatabaseHandler.setmUpdateListener(this);
        }
    }

    @Override
    public void onInsertNewValues(final ArrayList<Contact> list) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mListener != null){
                    mListener.onDataLoaded(list);
                }
            }
        });
    }

    public void deleteContact(Contact contact, int position){
        mDatabaseHandler.deleteAnyContact(contact, position);
    }

    public void undoContact(Contact contact , int position){
        mDatabaseHandler.undoAnyContact(contact, position);
    }

    @Override
    public void onDeleteAnyRow(final Contact contact , final int position) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mListener != null){
                    mListener.onOnePositionDeleted(contact , position);
                }
            }
        });
    }

    @Override
    public void onUndoAnyRow(final Contact contact , final int position) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mListener != null){
                    mListener.onOnePositionUndo(contact , position);
                }
            }
        });
    }

    public interface OnDataLoadedReceiver {
        public void onDataLoaded(ArrayList<Contact> list);
        public void onOnePositionDeleted(Contact contact , int position);
        public void onOnePositionUndo(Contact contact , int position);
    }

    private static DataManager mINSTANCE;

    public static DataManager getInstance(Context context){
        if(mINSTANCE == null){
            synchronized (DataManager.class){
                if(mINSTANCE == null){
                    mINSTANCE = new DataManager(context);
                }
            }
        }
        return mINSTANCE;
    }

    private DataManager(Context context){
        mContext = context;
        mDatabaseHandler = ContactListTable.getInstance(context);
        uiHandler = new Handler();
    }

    public void fetchData(){
        if(Utils.isInternetAvailable(mContext)){
            Intent intent = new Intent(mContext , ServerInteractor.class);
            mContext.startService(intent);
        }else{
            mDatabaseHandler.fetchAllContacts();
        }
    }


}
