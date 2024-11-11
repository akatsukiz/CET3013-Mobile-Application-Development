package com.example.studysupportapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class, Venue.class},version = 1, exportSchema = false)
public abstract class NoteRoomDatabase extends RoomDatabase{
    public abstract NoteDao noteDao();
    public abstract VenueDao venueDao();
    private static volatile NoteRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS =4;

    static final ExecutorService databaseWriteExecutor =

            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NoteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) { synchronized (NoteRoomDatabase.class) {
            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NoteRoomDatabase.class, "note_database") .fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).build();
            }
        }
        } return INSTANCE; }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);






        } };
}
