package ca.legacy.firebasedemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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


    // Register new user with the user's device if the user doesn't exist
    @Override
    public void registerUser(final String username) {
        progress.setMessage("Registering as " + username);
        progress.show();
        final String deviceId = AppController.getDeviceId();
        AppController.getFirebaseRef().child("users/" + username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    AppController.getFirebaseRef().child("users").child(username).setValue(new User(username, deviceId));
                    AppController.getUserPrefs(getApplicationContext()).edit().putString("username", username).commit();
                    Intent intent = new Intent();
                    intent.putExtra(AppController.getUserPrefsLocation(), username);
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

    // Login the user and check if the user is logging from the proper device
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
                    Map<String, String> user = dataSnapshot.getValue(Map.class);
                    if (user.get("deviceId").equals(AppController.getDeviceId())) {
                        Intent intent = new Intent();
                        intent.putExtra(AppController.getUserPrefsLocation(), username);
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

    @Override
    protected void onStop() {
        super.onStop();
        progress.hide();
    }
}
