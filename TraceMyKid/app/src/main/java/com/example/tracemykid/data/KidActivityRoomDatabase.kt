package com.example.tracemykid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [KidActivity::class], version = 1, exportSchema = false)
abstract class KidActivityRoomDatabase : RoomDatabase() {

    abstract fun kidActivityDao(): KidActivityDao

    companion object {
        @Volatile
        private var INSTANCE: KidActivityRoomDatabase? = null

        val coroutineScope = CoroutineScope(Dispatchers.Main)

        // Callback method here
        val loadSampleData: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                // Comment out the following block
                coroutineScope.launch(Dispatchers.IO) {

                    // Populate the database in the background.
                    val dao = INSTANCE!!.kidActivityDao()


                }
            }
        }

        fun getDatabase(context: Context): KidActivityRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(KidActivityRoomDatabase::class.java) {

                    // Create database here
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        KidActivityRoomDatabase::class.java, "kid_activity_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(loadSampleData)
                        .build()
                }
            }
            return INSTANCE
        }
    }
}

