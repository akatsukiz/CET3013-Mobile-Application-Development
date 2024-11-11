package com.example.studysupportapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao nDao;
    private LiveData<List<Note>> nAllCourse;

    NoteRepository(Application application){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        nDao = db.noteDao();
        nAllCourse = nDao.getAllCourse();
    }
    LiveData<List<Note>> getAllCourse(){
        return nAllCourse;
    }
    LiveData<List<Note>> getSubject(String query){return nDao.getSubject(query);}
    LiveData<List<Note>> getSubject(String query){return nDao.getSubject(query);}
    LiveData<List<Note>> getChapter(String query, String query2){return nDao.getChapter(query,query2);}
    LiveData<List<Note>> getNTitle(String query, String query2, int query3){return nDao.getNTitle(query, query2, query3);}
    LiveData<List<Note>> getNContent(String query, String query2, int query3, String query5){return nDao.getNContent(query,query2,query3,query5);}
    LiveData<List<Note>> searchNote(String query){return nDao.searchNote(query);}

    public void insert(Note note){
        NoteRoomDatabase.databaseWriteExecutor.execute(()->{
            nDao.insert(note);
        });
    }
}
