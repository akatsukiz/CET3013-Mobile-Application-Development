package com.example.studysupportapplication;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "schedule_table")
public class Schedule {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="schedule_id")
    private int scheduleId;

    @NonNull
    @ColumnInfo(name="event_title")
    private String eTitle;

    @NonNull
    @ColumnInfo(name="venue")
    private String venue;

    @NonNull
    @ColumnInfo(name="startDateTime")
    @TypeConverters(DateTypeConvertor.class)
    private Date startDateTime;

    @ColumnInfo(name="endDateTime")
    @TypeConverters(DateTypeConvertor.class)
    private Date endDateTime;

    @NonNull
    @ColumnInfo(name="isRepeat")
    private boolean isRepeat;

    public Schedule(@NonNull int scheduleId, String eTitle, String venue, Date startDateTime, Date endDateTime, boolean isRepeat){
        this.scheduleId=scheduleId;
        this.eTitle=eTitle;
        this.venue=venue;
        this.startDateTime=startDateTime;
        this.endDateTime=endDateTime;
        this.isRepeat=isRepeat;}



    public int getScheduleId(){return this.scheduleId;}
    public String getETitle(){
        return this.eTitle;
    }
    public String getVenue(){
        return this.venue;
    }
    public Date getStartDateTime(){ return this.startDateTime; }
    public Date getEndDateTime(){return this.endDateTime;}
    public boolean getIsRepeat(){return this.isRepeat;}
}
