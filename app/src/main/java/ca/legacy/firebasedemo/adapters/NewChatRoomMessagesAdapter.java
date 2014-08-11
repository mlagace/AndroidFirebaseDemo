package ca.legacy.firebasedemo.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.legacy.firebasedemo.R;
import ca.legacy.firebasedemo.models.ChatMessage;

/**
 * Created by matthewlagace on 14-08-11.
 */
public class NewChatRoomMessagesAdapter extends BaseAdapter {
    private Firebase ref;
    private int layout;
    private LayoutInflater inflater;
    private List<ChatMessage> models;
    private Map<String, ChatMessage> modelNames;
    private ChildEventListener listener;
    private String loggedUser;
    private Activity activity;

    /**
     * @param ref The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *            combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param layout This is the layout used to represent a single list item. You will be responsible for populating an
     *               instance of the corresponding view with the data from an instance of modelClass.
     * @param activity The activity containing the ListView
     */
    public NewChatRoomMessagesAdapter(final Firebase ref, String loggedUser, Activity activity, int layout) {
        this.ref = ref;
        this.layout = layout;
        this.inflater = activity.getLayoutInflater();
        this.models = new ArrayList<ChatMessage>();
        this.modelNames = new HashMap<String, ChatMessage>();
        this.loggedUser = loggedUser;
        this.activity = activity;

        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        listener = this.ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                ChatMessage model = dataSnapshot.getValue(ChatMessage.class);
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
        ChatMessage chatMessage = (ChatMessage) getItem(position);
        if (convertView == null) {
            view = inflater.inflate(layout, parent, false);
        } else {
            view = convertView;
        }
        viewHolder.userInfo = (TextView) view.findViewById(R.id.txt_room_username);
        viewHolder.message = (TextView) view.findViewById(R.id.txt_room_msg);
        viewHolder.position = position;
        viewHolder.userInfo.setText(chatMessage.getUser() + " (" + new DateTime(chatMessage.getCreatedAt()).toString("hh:mm") + ")" + ":");
        viewHolder.message.setText(chatMessage.getMessage());

        if (loggedUser.equals(chatMessage.getUser())) {
            viewHolder.userInfo.setTextColor(activity.getResources().getColor(R.color.blue_light_dark));
        } else {
            viewHolder.userInfo.setTextColor(Color.DKGRAY);
        }

        view.setTag(viewHolder);
        return view;
    }

    static class ViewHolder {
        TextView userInfo;
        TextView message;
        int position;
    }
}
