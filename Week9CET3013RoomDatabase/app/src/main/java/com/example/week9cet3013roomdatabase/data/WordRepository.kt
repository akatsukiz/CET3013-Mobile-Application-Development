package com.example.week9cet3013roomdatabase.data

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordRepository(application: Application?) {

    private val mWordDao: WordDao
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //This method will be called in ViewModel class
    var allWords: LiveData<List<Word>>? = null

    init {
        val db = WordRoomDatabase.getDatabase(application!!)
        mWordDao = db!!.wordDao()
        allWords = mWordDao.getAllWords()
    }

    suspend fun asyncInsert(word:Word?) {
        mWordDao.insert(word)
    }

    suspend  fun asyncDelete(word:Word?) {
        mWordDao.delete(word)
    }

    fun insert(word: Word?) {
        coroutineScope.launch(Dispatchers.IO) {
            asyncInsert(word)
        }
    }

    fun delete(word: Word?) {
        coroutineScope.launch(Dispatchers.IO) {
            asyncDelete(word)
        }
    }

}