package com.example.week9cet3013roomdatabase.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

public class WordViewModel(application: Application) :
    AndroidViewModel(application) {
    private val mRepository: WordRepository
    private var allWords: LiveData<List<Word>>?
    init {
        mRepository = WordRepository(application)
        allWords = mRepository.allWords
    }

    fun getAllWords(): LiveData<List<Word>>? {
        return allWords
    }

    fun insert(word: Word?) {
        mRepository.insert(word)
    }

    fun delete(word: Word?) {
        mRepository.delete(word)
    }


}
