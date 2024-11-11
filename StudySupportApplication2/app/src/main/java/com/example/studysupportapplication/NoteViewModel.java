package com.example.studysupportapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel{

    private NoteRepository nRespository;
    private LiveData<List<Note>> nAllCourses;
    public NoteViewModel(Application application) {
        super(application);
        nRespository = new NoteRepository(application);
        nAllCourses = nRespository.getAllCourse();
    }
    LiveData<List<Note>> getAllCourse(){
        return nAllCourses;
    }
    LiveData<List<Note>> getSubject(String query) {return nRespository.getSubject(query);}
    LiveData<List<Note>> getChapter(String query, String query2) {return nRespository.getChapter(query,query2);}
    LiveData<List<Note>> getNTitle(String query, String query2, int query3) {return nRespository.getNTitle(query,query2,query3);}
    LiveData<List<Note>> getNContent(String query, String query2, int query3, String query5){return nRespository.getNContent(query,query2,query3,query5);}
    LiveData<List<Note>> searchNote(String query){return nRespository.searchNote(query);}
    public void insert(Note note){
        nRespository.insert(note);
    }
}
