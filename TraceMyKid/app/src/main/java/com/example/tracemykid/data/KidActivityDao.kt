package com.example.tracemykid.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface KidActivityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(activity: KidActivity)

    @Update
    fun update(activity: KidActivity)

    @Delete
    fun delete(activity: KidActivity)

    @Query("DELETE FROM kid_activity_table")
    fun deleteAll()

    @Query("DELETE FROM kid_activity_table WHERE activity_id=:query")
    fun deleteItem(query:String)

    @Query("SELECT * FROM kid_activity_table ORDER BY activity_name ASC")
    fun getAllActivities(): LiveData<List<KidActivity>>

    @Query("SELECT * FROM kid_activity_table WHERE  activity_name LIKE'%'||:query||'%' OR reporter_name LIKE'%'||:query||'%'")
    fun searchActivity(query:String): LiveData<List<KidActivity>>

}

