package contactlist.android.com.contactlist.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import contactlist.android.com.contactlist.R;
import contactlist.android.com.contactlist.datamanager.DataManager;
import contactlist.android.com.contactlist.datamanager.DataManager.OnDataLoadedReceiver;
import contactlist.android.com.contactlist.modal.Contact;

/**
 * Created by jade on 9/4/17.
 */

public class ContactListFragment extends Fragment implements OnDataLoadedReceiver, SwipeRefreshLayout.OnRefreshListener{

    private ContactListAdapter mAdapter;
    private DataManager manager;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = DataManager.getInstance(getContext());
        manager.setmListener(this);
        manager.fetchData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_contact_list, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.list);
        mRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout.setOnRefreshListener(this);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext() , DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(llm);
        mAdapter = new ContactListAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.setmListener(null);
    }

    @Override
    public void onDataLoaded(ArrayList<Contact> list) {
        mRefreshLayout.setRefreshing(false);
        if(mAdapter != null){
            mAdapter.refershContactList(list);
        }
    }

    @Override
    public void onOnePositionDeleted(final Contact contact , final int position) {
        if(mAdapter != null){
            mAdapter.removePosition(position);
        }
        Snackbar snackbar = Snackbar
                .make(recyclerView, "Delete By Mistake", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DataManager.getInstance(getContext()).undoContact(contact , position);
                    }
                });

        snackbar.show();
    }

    @Override
    public void onOnePositionUndo(Contact contact , int position) {
        if(mAdapter != null){
            mAdapter.addPosition(contact, position);
        }
    }

    @Override
    public void onRefresh() {
        manager.fetchData();
    }
}
