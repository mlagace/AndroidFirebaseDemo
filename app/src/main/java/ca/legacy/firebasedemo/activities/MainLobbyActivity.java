package ca.legacy.firebasedemo.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import ca.legacy.firebasedemo.AppController;
import ca.legacy.firebasedemo.R;
import ca.legacy.firebasedemo.fragments.AddRoomDialogFragment;
import ca.legacy.firebasedemo.fragments.ChatRoomFragment;
import ca.legacy.firebasedemo.fragments.ChatRoomMessageListFragment;
import ca.legacy.firebasedemo.fragments.ChatRoomMessagesFragment;
import ca.legacy.firebasedemo.fragments.ChatRoomUsersFragment;
import ca.legacy.firebasedemo.models.ChatMessage;
import ca.legacy.firebasedemo.models.Room;

public class MainLobbyActivity extends ActionBarActivity implements ChatRoomFragment.Callbacks,
        ChatRoomMessagesFragment.Callbacks, ChatRoomMessageListFragment.Callbacks,
        AddRoomDialogFragment.Callbacks, ChatRoomUsersFragment.Callbacks {
    private String username;

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
    public void onRoomSelected(Room selectedRoom) {
        if (selectedRoom != null) {
            String room;
            if (selectedRoom.getOriginalName() != null) {
                room = selectedRoom.getOriginalName();
            } else {
                room = selectedRoom.getName();
            }

            AppController.getFirebaseRef().child("members/" + room + "/" + username).setValue(true);
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
            AppController.getFirebaseRef().child("messages/" + room)
                    .push()
                    .setValue(chatMessage);
        }
    }

    @Override
    public void onCreate(Room room) {
        if (room != null) {
            AppController.getFirebaseRef().child("rooms/" + room.getName()).setValue(room);
        }
    }

    @Override
    public void onUserSelected(String usernameRoom) {
        if (usernameRoom != null) {
            AppController.getFirebaseRef().child("rooms/" + usernameRoom).setValue(new Room(usernameRoom, username, true));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppController.getFirebaseRef().child("members").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getName().equals(username)) {
                            AppController.getFirebaseRef().child("members/" + dataSnapshot.getName() + "/" + username).removeValue();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getFirebaseRef().child("members").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getName().equals(username)) {
                            AppController.getFirebaseRef().child("members/" + dataSnapshot.getName() + "/" + username).removeValue();
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
