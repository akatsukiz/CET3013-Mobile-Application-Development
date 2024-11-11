package com.example.tracemykid.data

import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kid_activity_table")
data class KidActivity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="activity_id")val id: Int,
    @ColumnInfo(name = "activity_name") val activityName: String,
    @ColumnInfo(name = "activity_category") val activityCategory: String,
    @ColumnInfo(name = "activity_date") val activityDate: String,
    @ColumnInfo(name = "activity_location") val activityLocation: String?,
    @ColumnInfo(name = "activity_photo") val activityPhoto: String?,
    @ColumnInfo(name = "activity_notes") val activityNotes: String?,
    @ColumnInfo(name = "reporter_name") val reporterName: String,
    @ColumnInfo(name = "gotImage") val gotImage: Boolean

)
{




}

