package ca.legacy.firebasedemo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.widget.ListView;

import ca.legacy.firebasedemo.AppController;
import ca.legacy.firebasedemo.R;
import ca.legacy.firebasedemo.adapters.NewChatRoomMessagesAdapter;
import ca.legacy.firebasedemo.models.Room;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ChatRoomMessageListFragment extends ListFragment {
    private Callbacks mListener;

    public static ChatRoomMessageListFragment newInstance(Room room, String username) {
        ChatRoomMessageListFragment fragment = new ChatRoomMessageListFragment();
        Bundle args = new Bundle();
        args.putString("room", room.getName());
        args.putBoolean("private", room.getIsPrivate());
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatRoomMessageListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String room;
            String username;
            Boolean isPrivate;
            room = getArguments().getString("room");
            username = getArguments().getString("username");
            isPrivate = getArguments().getBoolean("private");
            String roomPath = "messages/" + room;

            if (isPrivate) {
                roomPath += "/" + username;
            }
            setListAdapter(new NewChatRoomMessagesAdapter(AppController.getFirebaseRef()
                    .child(roomPath), username, getActivity(), R.layout.chat_message));
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement ChatRoomMessageListFragment.Callbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView lv = getListView();
        lv.setStackFromBottom(true);
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
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
    }

}
