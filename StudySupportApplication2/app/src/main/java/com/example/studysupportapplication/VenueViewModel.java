package com.example.studysupportapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VenueViewModel extends AndroidViewModel{

    private VenueRepository vRespository;
    private LiveData<List<Venue>> vAllVenue;
    public VenueViewModel(Application application) {
        super(application);
        vRespository = new VenueRepository(application);
        vAllVenue = vRespository.getAllVenue();
    }
    LiveData<List<Venue>> getAllVenue(){return vAllVenue;}

    public void insert(Venue venue){vRespository.insert(venue);}
}
