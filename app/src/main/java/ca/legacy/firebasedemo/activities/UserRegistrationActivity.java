package ca.legacy.firebasedemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

import ca.legacy.firebasedemo.AppController;
import ca.legacy.firebasedemo.R;
import ca.legacy.firebasedemo.fragments.UserRegisterFragment;
import ca.legacy.firebasedemo.models.User;


public class UserRegistrationActivity extends ActionBarActivity implements UserRegisterFragment.Callbacks {
    static ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        setTitle("Setup New Account");
        progress = new ProgressDialog(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void registerUser(final String username) {
        progress.setMessage("Registering as " + username);
        progress.show();
        final String deviceId = AppController.getDeviceId();
        AppController.getFirebaseRef().child("users/" + username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    AppController.getFirebaseRef().child("users").child(username).setValue(new User(username, deviceId, false));
                    AppController.getUserPrefs(getApplicationContext()).edit().putString("username", username).commit();
                    Intent intent = new Intent();
                    intent.putExtra("ca.legacy.firebasedemo.USER", username);
                    intent.setClass(getApplicationContext(), MainLobbyActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                }
                progress.hide();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                progress.hide();
            }
        });
    }

    @Override
    public void loginUser(final String username) {
        progress.setMessage("Logging in as " + username);
        progress.show();
        AppController.getFirebaseRef().child("users/" + username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getApplicationContext(), "Username not found", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> user = (Map<String, String>) dataSnapshot.getValue();
                    if (user.get("deviceId").equals(AppController.getDeviceId())) {
                        Intent intent = new Intent();
                        intent.putExtra("ca.legacy.firebasedemo.USER", username);
                        intent.setClass(getApplicationContext(), MainLobbyActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unauthorized device", Toast.LENGTH_SHORT).show();
                    }
                }
                progress.hide();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                progress.hide();
            }
        });
    }
}
