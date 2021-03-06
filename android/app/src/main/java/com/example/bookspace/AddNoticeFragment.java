package com.example.bookspace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookspace.model.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AddNoticeFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_notice, container, false);

        Button addNotice = view.findViewById(R.id.buttonAddNotice);
        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddNote();
                //TODO: make a resource string
                Toast.makeText(getContext(), "Your notice has been added", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                BookNoticeFragment bookNoticeFragment = new BookNoticeFragment();
                bookNoticeFragment.setArguments(getArguments());
                fragmentManager.beginTransaction().replace(R.id.bookPageFragmentContainer, bookNoticeFragment).commit();
            }
        });

        return view;
    }


    public void onClickAddNote(){
        EditText eTitle = getActivity().findViewById(R.id.editTitle);
        EditText eNote = getActivity().findViewById(R.id.editAddNote);
        final String token = getActivity().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "token is null");
        final int bookId = getArguments().getInt("bookId");

        if(eTitle.getText().toString().matches("") || eNote.getText().toString().matches("")){
            Toast.makeText(getContext(), getText(R.string.fillField), Toast.LENGTH_LONG).show();
            return;
        }

        Call<ResponseBody> call7 = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .postNote("Bearer " + token, bookId, eTitle.getText().toString(), eNote.getText().toString());

        call7.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getText(R.string.wrongRes), Toast.LENGTH_LONG).show();
            }
        });
    }
}
