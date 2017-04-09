package contactlist.android.com.contactlist.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import contactlist.android.com.contactlist.BR;
import contactlist.android.com.contactlist.R;
import contactlist.android.com.contactlist.databinding.ListItemBinding;
import contactlist.android.com.contactlist.datamanager.DataManager;
import contactlist.android.com.contactlist.modal.Contact;

/**
 * Created by jade on 9/4/17.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    private ArrayList<Contact> mContactList;
    private Context mContext;

    public ContactListAdapter(Context context){
        mContext = context;
    }

    public ContactListAdapter(Context context, ArrayList<Contact> list) {
        mContactList = list;
        mContext = context;
    }

    public void refershContactList(ArrayList<Contact> mContactList) {
        this.mContactList = mContactList;
        notifyDataSetChanged();
    }

    public void addPosition(Contact contact , int position){
        mContactList.add(position, contact);
        notifyItemInserted(position);
    }
    public void removePosition(int position){
        mContactList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item , parent, false));
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.getItemBinding().setHandlers(this);
        holder.getItemBinding().setVariable(BR.contact , mContactList.get(position));
        holder.getItemBinding().executePendingBindings();
        holder.getItemBinding().image.setTag(position);
    }

    public void onItemClick(View view){
        int position = (int)view.getTag();
        DataManager.getInstance(mContext).deleteContact(mContactList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mContactList != null ? mContactList.size() : 0;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        private ListItemBinding itemBinding;

        public ContactViewHolder(View itemView) {
            super(itemView);

            itemBinding = DataBindingUtil.bind(itemView);
        }

        public ListItemBinding getItemBinding(){
            return itemBinding;
        }
    }
}
