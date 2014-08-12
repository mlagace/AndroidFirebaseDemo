package ca.legacy.firebasedemo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ca.legacy.firebasedemo.R;
import ca.legacy.firebasedemo.models.Room;

/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRoomDialogFragment.Callbacks} interface
 * to handle interaction events.
 * Use the {@link AddRoomDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddRoomDialogFragment extends DialogFragment {

    private Callbacks mListener;
    private String username;

    public static AddRoomDialogFragment newInstance(String username) {
        AddRoomDialogFragment fragment = new AddRoomDialogFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }
    public AddRoomDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        username = getArguments().getString("username");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.fragment_add_room_dialog, null);
        builder.setView(v);
        builder.setTitle("Add New Room");
        builder.setMessage("Enter the name of the new Room you want to create")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText room = (EditText) v.findViewById(R.id.edit_new_room);
                        mListener.onCreate(new Room(room.getText().toString(), username, false));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing let it close
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Callbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface Callbacks {
        public void onCreate(Room room);
    }

}
