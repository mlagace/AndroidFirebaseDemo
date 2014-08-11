package ca.legacy.firebasedemo.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.Map.Entry;

import ca.legacy.firebasedemo.AppController;
import ca.legacy.firebasedemo.R;
import ca.legacy.firebasedemo.fragments.AddRoomDialogFragment;
import ca.legacy.firebasedemo.fragments.ChatRoomFragment;
import ca.legacy.firebasedemo.fragments.ChatRoomMessageListFragment;
import ca.legacy.firebasedemo.fragments.ChatRoomMessagesFragment;
import ca.legacy.firebasedemo.fragments.ChatRoomUsersFragment;
import ca.legacy.firebasedemo.models.ChatMessage;
import ca.legacy.firebasedemo.models.Room;
import ca.legacy.firebasedemo.models.User;

public class MainLobbyActivity extends ActionBarActivity implements ChatRoomFragment.Callbacks,
        ChatRoomMessagesFragment.Callbacks, ChatRoomMessageListFragment.Callbacks,
        AddRoomDialogFragment.Callbacks, ChatRoomUsersFragment.Callbacks {
    private String username;
    private String room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.disableIcon(this);
        setContentView(R.layout.activity_main_lobby);
        username = getIntent().getStringExtra("ca.legacy.firebasedemo.USER");
        setTitle("Logged in: " + username);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_lobby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_room) {
            AddRoomDialogFragment addRoomDialogFragment = AddRoomDialogFragment.newInstance(username);
            addRoomDialogFragment.show(getSupportFragmentManager(), "new_room");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRoomSelected(String room) {
        if (room != null && room.length() > 0) {
            AppController.getFirebaseRef().child("rooms/" + room + "/users/" + username).setValue(new User(username));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.msg_list, ChatRoomMessageListFragment.newInstance(room, username))
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.chat_users_container, ChatRoomUsersFragment.newInstance(room, username))
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.chat_messages_container,
                            ChatRoomMessagesFragment.newInstance(room, username))
                    .commit();
        }
    }

    @Override
    public void onSendMessage(ChatMessage chatMessage, String room) {
        if (chatMessage != null && room != null && room.length() > 0) {
            AppController.getFirebaseRef().child("rooms/" + room + "/messages")
                    .push()
                    .setValue(chatMessage);
        }
    }

    @Override
    public void stopProgress() {

    }

    @Override
    public void onMessageClick(ChatMessage chatMessage) {

    }

    @Override
    public void onCreate(Room room) {
        if (room != null) {
            AppController.getFirebaseRef().child("rooms").child(room.getName()).setValue(room);
        }
    }

    @Override
    public void onUserSelected(String username) {

    }

    @Override
    public void setupUsersOnAutoComplete(final AutoCompleteTextView auto) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "https://vivid-fire-8562.firebaseio.com/rooms/Blah/users";
//
//        // Request a string response from the provided URL.
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        List<String> tmpUsers = new ArrayList<String>();
//                        while (response.keys().hasNext()) {
//                            tmpUsers.add(response.keys().next());
//                        }
//                        String[] users = new String[tmpUsers.size()];
//                        for (int i=0; i<tmpUsers.size(); i++) {
//                            users[i] = tmpUsers.get(i);
//                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, users);
//
//                        if (auto != null) {
//                            adapter.setNotifyOnChange(true);
//                            adapter.notifyDataSetChanged();
//                            auto.setAdapter(adapter);
//                        } else {
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppController.getFirebaseRef().child("rooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    Room childRoom = (Room) dataSnapshot.getValue(Room.class);
                    Map<String, User> users = childRoom.getUsers();
                    if (users != null && users.size() > 0) {
                        for (Entry<String, User> userObj : childRoom.getUsers().entrySet()) {
                            if (userObj.getKey().equals(username)) {
                                AppController.getFirebaseRef().child("rooms/" + childRoom.getName() + "/users/" + username).removeValue();
                            }
                        }
                    }
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
}
