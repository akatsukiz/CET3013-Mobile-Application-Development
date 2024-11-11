package com.example.week9cet3013roomdatabase.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word?)

    @Update
    fun update(word: Word?)

    @Delete
    fun delete(word: Word?)

    @Query("DELETE FROM word_table")
    fun deleteAll()

    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAllWords(): LiveData<List<Word>>
}
