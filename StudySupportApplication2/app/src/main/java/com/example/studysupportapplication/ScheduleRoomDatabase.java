package com.example.studysupportapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Schedule.class},version = 1, exportSchema = false)
public abstract class ScheduleRoomDatabase extends RoomDatabase{
    public abstract ScheduleDao scheduleDao();
    private static volatile ScheduleRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS =4;

    static final ExecutorService databaseWriteExecutor =

            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ScheduleRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) { synchronized (ScheduleRoomDatabase.class) {
            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ScheduleRoomDatabase.class, "schedule_database") .fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).build();
            }
        }
        } return INSTANCE; }
    private static Callback sRoomDatabaseCallback = new Callback(){
        @Override public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);





        } };
}
