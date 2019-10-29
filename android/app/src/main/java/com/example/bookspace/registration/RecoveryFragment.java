package com.example.bookspace.registration;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookspace.R;

public class RecoveryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RecoveryFragment() {
        // Required empty public constructor
    }

    public static RecoveryFragment newInstance() {
        RecoveryFragment fragment = new RecoveryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);
        Button fragmentButton = view.findViewById(R.id.buttonRecFragGotIt);
        TextView fragmentTitle = view.findViewById(R.id.textViewFragTitle);

        fragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        return view;
    }

    //открываем страницу логина при нажатии на кнопку "GOT IT"
    public void openLoginActivity(){
        Intent intent = new Intent(RecoveryFragment.this.getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}