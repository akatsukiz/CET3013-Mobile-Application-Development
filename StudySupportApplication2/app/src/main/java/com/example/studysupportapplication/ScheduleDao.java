package com.example.studysupportapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert

    void insert(Schedule schedule);

    @Query("DELETE FROM schedule_table")
    void deleteAll();

    @Delete
    void delete(Schedule schedule);

    @Query("SELECT * FROM schedule_table ORDER BY startDateTime ASC")
    LiveData<List<Schedule>> getAllDetail();

    @Query("SELECT MAX(schedule_id) FROM schedule_table ")
    LiveData<Integer> getId();

    /*@Query("SELECT * from note_table WHERE course_name = :query ORDER BY course_name ASC")
    LiveData<List<Note>> getSubject(String query);
    @Query("SELECT * from note_table WHERE course_name = :query AND subject_title = :query2 ORDER BY course_name ASC")
    LiveData<List<Note>> getChapter(String query, String query2);
    @Query("SELECT * from note_table WHERE course_name = :query AND subject_title = :query2 AND chapter_number = :query3 ORDER BY course_name ASC")
    LiveData<List<Note>> getNTitle(String query , String query2, int query3);
    @Query("SELECT * from note_table WHERE course_name = :query AND subject_title = :query2 AND chapter_number = :query3 AND note_title = :query5 ORDER BY course_name ASC")
    LiveData<List<Note>> getNContent(String query, String query2, int query3, String query5);*/

}
