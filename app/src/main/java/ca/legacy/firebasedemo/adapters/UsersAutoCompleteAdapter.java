package ca.legacy.firebasedemo.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import ca.legacy.firebasedemo.AppController;

/**
 * Created by matthewlagace on 14-08-11.
 */
public class UsersAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private final ArrayList<String> resultList = new ArrayList<String>();

    // Loads all the online users in the current Chat Room into an AutoCompleteTextView
    // when the user starts typing "@"
    public UsersAutoCompleteAdapter(Context context, final int resource, String room) {
        super(context, resource);
        AppController.getFirebaseRef().child("members/" + room)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    resultList.add("@" + dataSnapshot.getName());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    // You need to implement Filterable when using custom data to show in
    // an AutoCompleteTextView widget
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    String strConstraint = constraint.toString();
                    if (strConstraint.endsWith("@")) {
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
