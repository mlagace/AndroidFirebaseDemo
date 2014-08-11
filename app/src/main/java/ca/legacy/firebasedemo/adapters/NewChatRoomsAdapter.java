package ca.legacy.firebasedemo.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.legacy.firebasedemo.models.Room;

/**
 * Created by matthewlagace on 14-08-11.
 */
public class NewChatRoomsAdapter extends BaseAdapter {

    private Firebase ref;
    private int layout;
    private LayoutInflater inflater;
    private List<Room> models;
    private Map<String, Room> modelNames;
    private ChildEventListener listener;

    /**
     * @param ref The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *            combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param layout This is the layout used to represent a single list item. You will be responsible for populating an
     *               instance of the corresponding view with the data from an instance of modelClass.
     * @param activity The activity containing the ListView
     */
    public NewChatRoomsAdapter(final Firebase ref, Activity activity, int layout) {
        this.ref = ref;
        this.layout = layout;
        this.inflater = activity.getLayoutInflater();
        this.models = new ArrayList<Room>();
        this.modelNames = new HashMap<String, Room>();

        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        listener = this.ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                System.out.println(dataSnapshot.getValue());
//                System.out.println(dataSnapshot.getValue().getClass().getSimpleName());
                Room model = dataSnapshot.getValue(Room.class);
//                System.out.println(model);
                modelNames.put(dataSnapshot.getName(), model);
                models.add(model);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

//                // One of the models changed. Replace it in our list and name mapping
//                String modelName = dataSnapshot.getName();
//                T oldModel = modelNames.get(modelName);
//                T newModel = dataSnapshot.getValue(FirebaseListAdapter.this.modelClass);
//                int index = models.indexOf(oldModel);
//
//                models.set(index, newModel);
//                modelNames.put(modelName, newModel);
//
//                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

//                // A model was removed from the list. Remove it from our list and the name mapping
//                String modelName = dataSnapshot.getName();
//                T oldModel = modelNames.get(modelName);
//                models.remove(oldModel);
//                modelNames.remove(modelName);
//                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

//                // A model changed position in the list. Update our list accordingly
//                String modelName = dataSnapshot.getName();
//                T oldModel = modelNames.get(modelName);
//                T newModel = dataSnapshot.getValue(FirebaseListAdapter.this.modelClass);
//                int index = models.indexOf(oldModel);
//                models.remove(index);
//                if (previousChildName == null) {
//                    models.add(0, newModel);
//                } else {
//                    T previousModel = modelNames.get(previousChildName);
//                    int previousIndex = models.indexOf(previousModel);
//                    int nextIndex = previousIndex + 1;
//                    if (nextIndex == models.size()) {
//                        models.add(newModel);
//                    } else {
//                        models.add(nextIndex, newModel);
//                    }
//                }
//                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our listener and forget about all of the models
        ref.removeEventListener(listener);
        models.clear();
        modelNames.clear();
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder = new ViewHolder();
        Room room = (Room) getItem(position);
        if (convertView == null) {
            view = inflater.inflate(layout, parent, false);
        } else {
            view = convertView;
        }
        viewHolder.room = (TextView) view.findViewById(android.R.id.text1);
        viewHolder.position = position;
        viewHolder.room.setText(room.getName());
        view.setTag(viewHolder);
        return view;
    }

    static class ViewHolder {
        TextView room;
        int position;
    }
}
