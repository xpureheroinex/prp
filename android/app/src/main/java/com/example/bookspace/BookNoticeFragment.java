package com.example.bookspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookspace.adapters.NoteAdapter;
import com.example.bookspace.model.RetrofitClient;
import com.example.bookspace.model.notes.GetNotesResponse;
import com.example.bookspace.model.notes.Note;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class BookNoticeFragment extends Fragment {

    ListView listView;
    NoteAdapter noteAdapter;
    List<NoteClass> noteClassList;
    public int[] noteId;
    public Note[] notes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book_notice, container, false);
        final String token = getActivity().getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", "token is null");
        final int bookId = getArguments().getInt("bookId");


        Call<GetNotesResponse> call = RetrofitClient
                .getInstance()
                .getBookSpaceAPI()
                .getNotes("Bearer " + token, bookId);

        call.enqueue(new Callback<GetNotesResponse>() {
            @Override
            public void onResponse(Call<GetNotesResponse> call, Response<GetNotesResponse> response) {
                notes = response.body().getNotes();
                if (notes.length == 0) {
                    TextView txtView = view.findViewById(R.id.textViewNoNotes);
                    //?????txtView.setText("No notes on this book");
                    txtView.setVisibility(View.VISIBLE);
                } else {
                    noteId = new int[notes.length];
                    noteClassList = new ArrayList<>();
                    for (int i = 0; i < notes.length; i++) {
                        noteClassList.add(new NoteClass(i, notes[i].getTitle(),
                                "\n" + notes[i].getText(), getText(R.string.noteDate) + notes[i].getCreated()));
                        noteId[i] = notes[i].getId();
                    }
                    listView = view.findViewById(R.id.ListNotes);
                    noteAdapter = new NoteAdapter(getContext(), noteClassList);
                    listView.setAdapter(noteAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent inten = new Intent(getContext(), EditDeleteNote.class);
                            inten.putExtra("noteId", noteId[position]);
                            inten.putExtra("bookId", bookId);
                            inten.putExtra("Title", notes[position].getTitle());
                            inten.putExtra("Text", notes[position].getText());
                            startActivity(inten);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetNotesResponse> call, Throwable t) {
                Toast.makeText(getContext(), getText(R.string.wrongRes), Toast.LENGTH_LONG).show();
            }
        });

        Button addNoticePage = view.findViewById(R.id.buttonOpenAddNoticePage);
        addNoticePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoticeFragment addNoticeFragment = new AddNoticeFragment();
                addNoticeFragment.setArguments(getArguments());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.bookPageFragmentContainer, addNoticeFragment).commit();
            }
        });

        return view;
    }
}
