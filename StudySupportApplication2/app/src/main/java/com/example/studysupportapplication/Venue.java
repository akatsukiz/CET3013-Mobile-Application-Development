package com.example.studysupportapplication;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "venue_table")
public class Venue {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="venue_id")
    private int venueId;

    @NonNull
    @ColumnInfo(name="venue_name")
    private String venueName;

    Venue(int venueId, String venueName){
        this.venueId=venueId;
        this.venueName=venueName;
    }
    public int getVenueId(){return this.venueId;}
    public String getVenueName(){return this.venueName;}
}
