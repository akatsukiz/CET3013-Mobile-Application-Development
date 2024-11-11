package com.example.studysupportapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert

    void insert(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Query("SELECT * from note_table ORDER BY course_name ASC")
    LiveData<List<Note>> getAllCourse();

    @Query("SELECT * from note_table WHERE course_name = :query ORDER BY course_name ASC")
    LiveData<List<Note>> getSubject(String query);
    @Query("SELECT * from note_table WHERE course_name = :query AND subject_title = :query2 ORDER BY course_name ASC")
    LiveData<List<Note>> getChapter(String query, String query2);
    @Query("SELECT * from note_table WHERE course_name = :query AND subject_title = :query2 AND chapter_number = :query3 ORDER BY course_name ASC")
    LiveData<List<Note>> getNTitle(String query , String query2, int query3);
    @Query("SELECT * from note_table WHERE course_name = :query AND subject_title = :query2 AND chapter_number = :query3 AND note_title = :query5 ORDER BY course_name ASC")
    LiveData<List<Note>> getNContent(String query, String query2, int query3, String query5);

    @Query("SELECT * from note_table WHERE note_title LIKE '%' ||:query|| '%'")
    LiveData<List<Note>> searchNote(String query);

}
