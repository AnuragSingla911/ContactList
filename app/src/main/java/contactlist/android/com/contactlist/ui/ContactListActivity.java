package contactlist.android.com.contactlist.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import contactlist.android.com.contactlist.R;

public class ContactListActivity extends AppCompatActivity {

    public static final String TAG = ContactListActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if(fragment == null){
            fragment = new ContactListFragment();
            fragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , fragment , TAG).commit();
        }
    }
}
