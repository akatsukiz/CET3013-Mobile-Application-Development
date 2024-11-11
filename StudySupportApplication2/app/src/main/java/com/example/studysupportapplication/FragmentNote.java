package com.example.studysupportapplication;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentNote extends Fragment implements CourseListener {
    private NoteViewModel noteViewModel;
    private RecyclerView recylerView;
    private int location=0;
    private String courseName="", subjectTitle="", noteTitle="";
    private String noteImage="";
    boolean isImage=false;
    private int chapter=0;
    private NoteListAdapter adapter=null;
    private NoteDataListener listener;
    private TextView textView;
    SearchView searchView;
    List<Note> note01;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NoteDataListener)
        listener=(NoteDataListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_note,container,false);
        FloatingActionButton fab;
        fab= (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        noteViewModel=new ViewModelProvider(this).get(NoteViewModel.class);
        adapter= new NoteListAdapter(getContext(),this);
        recylerView=view.findViewById(R.id.courseRecyclerView);
        recylerView.setAdapter(adapter);
        recylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textView= view.findViewById(R.id.titleTextView);
        searchView=view.findViewById(R.id.searchViewNote);
        searchView.setQueryHint(getContext().getString(R.string.searchText));
        switch(location) {

            case 0:
                noteViewModel.getAllCourse().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        adapter.setNote(notes);
                        textView.setText(R.string.courseText);
                        Log.e("Test","Got into switch 0");
                    }
                });
                break;
            case 1:
                noteViewModel.getSubject(courseName).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        adapter.setNote(notes);
                        textView.setText(R.string.subjectText);
                        Log.e("Test","Got into switch 1");
                    }
                });
                break;
            case 2:
                noteViewModel.getChapter(courseName,subjectTitle).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        adapter.setNote(notes);
                        textView.setText(R.string.chapterText);
                        Log.e("Test","Got into switch 2");
                    }
                });
                break;

            case 3:
                noteViewModel.getNTitle(courseName,subjectTitle,chapter).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        adapter.setNote(notes);
                        textView.setText(R.string.noteTitleText);
                        Log.e("Test","Got into switch 3");
                    }
                });
                break;


        }




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAddNote fragmentAddNote = new FragmentAddNote();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentAddNote).addToBackStack(null).commit();

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery(newText);
                return false;
            }
        });

        return view;

    }


    public void searchQuery(String query){
        View view = getView();
        adapter = new NoteListAdapter(getContext(),this);
        recylerView = view.findViewById(R.id.courseRecyclerView);
        recylerView.setAdapter(adapter);
        recylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noteViewModel.searchNote(query).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.location=3;
                adapter.setNote(notes);
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onCourseClick(String courseName, String subjectTitle, int number, int location, boolean isImage, String noteTitle, String noteImage) {
        this.courseName=courseName;
        this.subjectTitle=subjectTitle;
        this.noteTitle=noteTitle;
        this.location=location;
        this.chapter=number;
        this.isImage=isImage;
        this.noteImage=noteImage;


        switch(location) {

            case 0:
                noteViewModel.getAllCourse().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        Log.e("Notes 0", Integer.toString(notes.size()));
                        textView.setText(R.string.courseText);
                        adapter.setNote(notes);
                        Log.e("Test","Got into switch 0");
                    }
                });
                break;
            case 1:
                noteViewModel.getSubject(courseName).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        Log.e("Notes 1", Integer.toString(notes.size()));
                        textView.setText(R.string.subjectText);
                        adapter.setNote(notes);
                        Log.e("Test","Got into switch 1");
                    }
                });
                break;
            case 2:
                noteViewModel.getChapter(courseName,subjectTitle).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        Log.e("Note 02",Integer.toString(notes.size()));
                        textView.setText(R.string.chapterText);
                        adapter.setNote(notes);
                        Log.e("Test","Got into switch 2");
                    }
                });
                break;
            case 3:
                noteViewModel.getNTitle(courseName,subjectTitle,chapter).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        adapter.setNote(notes);
                        textView.setText(R.string.noteTitleText);
                        Log.e("Test","Got into switch 2");
                        Log.e("Notes 3", Integer.toString(notes.size()));
                    }
                });
                break;
            case 4:
                Log.e("Before note 4", "case 04");
                noteViewModel.getNContent(courseName,subjectTitle,chapter, noteTitle).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {

                    @Override
                    public void onChanged(List<Note> notes) {
                        if(notes!=null) {
                            boolean bool=notes.get(0).getIsImage();
                            if (!bool) {
                                note01 = notes;
                                Log.e("Notes 4", notes.get(0).getNContent());
                                //adapter.setNote(notes);
                                listener.onSendData(notes.get(0).getNTitle(), notes.get(0).getNContent(),bool);
                            }
                            else{
                                listener.onSendData(notes.get(0).getNTitle(),notes.get(0).getNImage(),bool);
                            }
                        }
                        else{
                            Log.e("Notes 4","Note is null");
                        }
                    }
                });


                break;
        }
    }
}

