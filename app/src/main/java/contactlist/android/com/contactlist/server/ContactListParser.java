package contactlist.android.com.contactlist.server;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import contactlist.android.com.contactlist.database.ContactListTable;
import contactlist.android.com.contactlist.modal.Contact;

/**
 * Created by jade on 9/4/17.
 */

public class ContactListParser {

    public void parse(String json, Context context){
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.optJSONArray("result");
            if(array != null && array.length() > 0){
                ArrayList<Contact> list = new ArrayList<>();
                for(int i =0; i< array.length() ; i++){
                    JSONObject object1 =  array.optJSONObject(i);
                    if(object1 != null){
                        Contact contact = new Contact();
                        contact.setId(Integer.parseInt(object1.optString("uid")));
                        contact.setName(object1.optString("name"));
                        list.add(contact);
                    }
                }

                ContactListTable.getInstance(context).insertContacts(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
