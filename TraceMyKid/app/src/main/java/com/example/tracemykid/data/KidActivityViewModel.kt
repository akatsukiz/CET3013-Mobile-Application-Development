package com.example.tracemykid.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class KidActivityViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repository: KidActivityRepository
    private val allActivities: LiveData<List<KidActivity>>

    init {
        repository = KidActivityRepository(application)
        allActivities = repository.allActivities!!
    }
    fun getAllActivities():LiveData<List<KidActivity>>?{
        return allActivities
    }

    fun insert(activity: KidActivity) {
        repository.insert(activity)
    }

    fun delete(activity: KidActivity) {
        repository.delete(activity)
    }
    fun deleteAll(){
        repository.deleteAll()
    }

    fun deleteItem(query:String){
        repository.deleteItem(query)
    }
    fun update(activity: KidActivity){
        repository.update(activity)
    }
    fun searchActivity(query: String): LiveData<List<KidActivity>>{
        return repository.searchActivity(query)
    }
}

