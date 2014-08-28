package ca.legacy.firebasedemo.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.joda.time.DateTime;

import ca.legacy.firebasedemo.R;
import ca.legacy.firebasedemo.adapters.UsersAutoCompleteAdapter;
import ca.legacy.firebasedemo.models.ChatMessage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatRoomMessagesFragment.Callbacks} interface
 * to handle interaction events.
 * Use the {@link ChatRoomMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ChatRoomMessagesFragment extends Fragment {
    private String room;
    private String username;
    private Callbacks mListener;

    public static ChatRoomMessagesFragment newInstance(String room, String username) {
        ChatRoomMessagesFragment fragment = new ChatRoomMessagesFragment();
        Bundle args = new Bundle();
        args.putString("room", room);
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }
    public ChatRoomMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            room = getArguments().getString("room");
            username = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_room_messages, container, false);
        final AutoCompleteTextView message = (AutoCompleteTextView) v.findViewById(R.id.txt_room_new_message);

        // Load online users in Chat Room for the AutoCompleteTextView {@link message}
        message.setAdapter(new UsersAutoCompleteAdapter(getActivity(),
                android.R.layout.simple_list_item_1, room));
        message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Set the background color and text color for the selected user in the autocomplete list
                Spannable str = message.getText();
                str.setSpan(new BackgroundColorSpan(getActivity().getResources()
                        .getColor(R.color.blue_light_dark)), 0, str.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, str.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        });

        Button btn = (Button) v.findViewById(R.id.btn_room_send_message);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().trim().length() > 0) {
                    mListener.onSendMessage(new ChatMessage(message.getText().toString(),
                            username, new DateTime().toDateTimeISO().toString()), room);
                    message.setText("");
                }
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ChatRoomMessagesFragment.Callbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        public void onSendMessage(ChatMessage chatMessage, String room);
    }
}
