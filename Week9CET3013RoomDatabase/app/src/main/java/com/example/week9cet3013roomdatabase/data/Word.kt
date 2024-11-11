package com.example.week9cet3013roomdatabase.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
public class Word(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word") var mWord:String) {

}
