package ca.legacy.firebasedemo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.legacy.firebasedemo.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserRegisterFragment.Callbacks} interface
 * to handle interaction events.
 * Use the {@link UserRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class UserRegisterFragment extends Fragment {
    private Callbacks mListener;

    public static UserRegisterFragment newInstance() {
        UserRegisterFragment fragment = new UserRegisterFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }
    public UserRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_register, container, false);
        final EditText usernameEdit = (EditText) v.findViewById(R.id.edit_username);
        final Button regBtn = (Button) v.findViewById(R.id.btn_user_register);
        final Button loginBtn = (Button) v.findViewById(R.id.btn_user_login);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    String username = usernameEdit.getText().toString();
                    if (username.length() > 0) {
                        mListener.registerUser(username);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Username invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    String username = usernameEdit.getText().toString();
                    if (username.length() > 0) {
                        mListener.loginUser(username);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Username invalid", Toast.LENGTH_SHORT).show();
                    }
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
                    + " must implement OnFragmentInteractionListener");
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
        public void registerUser(String username);
        public void loginUser(String username);
    }

}
