package com.example.studysupportapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VenueDao {
    @Insert

    void insert(Venue venue);

    @Query("DELETE FROM venue_table")
    void deleteAll();

    @Query("SELECT * from venue_table ORDER BY venue_name ASC")
    LiveData<List<Venue>> getAllVenue();



}
