package ca.legacy.firebasedemo.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
import java.util.List;

import ca.legacy.firebasedemo.R;
import ca.legacy.firebasedemo.models.ChatMessage;

/**
 * Created by matthewlagace on 14-08-11.
 */
public class NewChatRoomMessagesAdapter extends BaseAdapter {
    private final int layout;
    private final LayoutInflater inflater;
    private final List<ChatMessage> models;
    private final String loggedUser;
    private final Activity activity;

    /**
     * @param ref The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *            combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param layout This is the layout used to represent a single list item. You will be responsible for populating an
     *               instance of the corresponding view with the data from an instance of modelClass.
     * @param activity The activity containing the ListView
     */
    public NewChatRoomMessagesAdapter(final Firebase ref, String loggedUser, Activity activity, int layout) {
        this.layout = layout;
        this.inflater = activity.getLayoutInflater();
        this.models = new ArrayList<ChatMessage>();
        this.loggedUser = loggedUser;
        this.activity = activity;

        // Load all Chat Messages for the current Chat Room the user is in
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                ChatMessage model = dataSnapshot.getValue(ChatMessage.class);
                models.add(model);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
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
        viewHolder.userInfo.setText(chatMessage.getUser() + " (" +
                new DateTime(chatMessage.getCreatedAt()).toString("hh:mm") + ")" + ":");
        viewHolder.message.setText(chatMessage.getMessage());

        // Color code the user's own messages
        if (loggedUser.equals(chatMessage.getUser())) {
            viewHolder.userInfo.setTextColor(activity.getResources().
                    getColor(R.color.blue_light_dark));
        } else {
            viewHolder.userInfo.setTextColor(Color.DKGRAY);
        }

        // Color code messages that contain the user
        if (chatMessage.getMessage().contains("@" + loggedUser)) {
            String combined = "@" + loggedUser;
            int startIdx = chatMessage.getMessage().indexOf(combined);
            if (startIdx != -1) {
                Spannable str = new SpannableString(viewHolder.message.getText());
                str.setSpan(new BackgroundColorSpan(activity.getResources()
                        .getColor(R.color.blue_light_dark)), 0, combined.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, combined.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, combined.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.message.setText(str);
            }
        }

        view.setTag(viewHolder);
        return view;
    }


    // User ViewHolder for smooth scrolling lists
    static class ViewHolder {
        TextView userInfo;
        TextView message;
        int position;
    }
}
