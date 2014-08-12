package ca.legacy.firebasedemo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import ca.legacy.firebasedemo.AppController;
import ca.legacy.firebasedemo.adapters.NewChatRoomUsersAdapter;
import ca.legacy.firebasedemo.models.User;


public class ChatRoomUsersFragment extends ListFragment {
    private Callbacks mListener;

    public static ChatRoomUsersFragment newInstance(String room, String username) {
        ChatRoomUsersFragment fragment = new ChatRoomUsersFragment();
        Bundle args = new Bundle();
        args.putString("room", room);
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatRoomUsersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String room;
            String username;
            room = getArguments().getString("room");
            username = getArguments().getString("username");
            setListAdapter(new NewChatRoomUsersAdapter(AppController.getFirebaseRef().child("members/" + room), username, getActivity(), android.R.layout.simple_list_item_1));
        }
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mListener != null) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            User user = (User) l.getItemAtPosition(position);
            mListener.onUserSelected(user.getUsername());
//            mListener.onUserSelected(((TextView) v.findViewById(android.R.id.text1)).getText().toString());
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface Callbacks {
        public void onUserSelected(String username);
    }

}
