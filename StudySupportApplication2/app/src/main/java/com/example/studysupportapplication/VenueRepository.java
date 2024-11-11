package com.example.studysupportapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class VenueRepository {
    private VenueDao vDao;
    private LiveData<List<Venue>> vAllVenue;

    VenueRepository(Application application){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        vDao = db.venueDao();
        vAllVenue = vDao.getAllVenue();
    }
    LiveData<List<Venue>> getAllVenue(){
        return vAllVenue;
    }

    public void insert(Venue venue){
        NoteRoomDatabase.databaseWriteExecutor.execute(()->{
            vDao.insert(venue);
        });
    }
}
