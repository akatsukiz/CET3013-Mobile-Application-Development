package com.example.week9cet3013roomdatabase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        //Callback method here
        val loadSampleData: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                // comment out the following block
                coroutineScope.launch(Dispatchers.IO) {

                    // Populate the database in the background.
                    val dao = INSTANCE!!.wordDao()
                    dao.deleteAll()
                    var word = Word("Android")
                    dao.insert(word)
                    word = Word("IOS")
                    dao.insert(word)
                    word = Word("Symbian")
                    dao.insert(word)
                }
            }
        }

        fun getDatabase(context: Context): WordRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(WordRoomDatabase::class.java) {

                    // Create database here
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java, "word_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(loadSampleData)
                        .build()
                }
            }
            return INSTANCE
        }
    }


}
