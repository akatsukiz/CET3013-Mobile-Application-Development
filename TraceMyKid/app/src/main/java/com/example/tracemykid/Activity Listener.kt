package com.example.tracemykid

interface ActivityListener {
    fun onActivityClick(
        id: Int,
        activityName: String,
        activityCategory: String,
        activityDate: String,
        photo: String,
        notes: String,
        reporterName: String,
        gotImage: Boolean,
        location: String
    )
}