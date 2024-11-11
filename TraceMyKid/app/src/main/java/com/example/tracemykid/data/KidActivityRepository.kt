package com.example.tracemykid.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KidActivityRepository(application: Application) {

    private val kidActivityDao: KidActivityDao
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // This method will be called in ViewModel class
    var allActivities: LiveData<List<KidActivity>>? = null

    init {
        val db = KidActivityRoomDatabase.getDatabase(application)
        kidActivityDao = db!!.kidActivityDao()
        allActivities = kidActivityDao.getAllActivities()
    }

    suspend fun asyncInsert(activity: KidActivity) {
        kidActivityDao.insert(activity)
    }

    suspend fun asyncDelete(activity: KidActivity) {
        kidActivityDao.delete(activity)
    }

    fun insert(activity: KidActivity) {
        coroutineScope.launch(Dispatchers.IO) {
            asyncInsert(activity)
        }
    }

    fun delete(activity: KidActivity) {
        coroutineScope.launch(Dispatchers.IO) {
            asyncDelete(activity)
        }
    }
    fun deleteAll(){
       coroutineScope.launch (Dispatchers.IO){


            kidActivityDao.deleteAll()
        }
    }
    fun deleteItem(query:String){
        coroutineScope.launch (Dispatchers.IO) {
            kidActivityDao.deleteItem(query)
        }
    }

    fun update(activity: KidActivity){
        coroutineScope.launch (Dispatchers.IO) {
            kidActivityDao.update(activity)
        }
    }
    fun searchActivity(query:String):LiveData<List<KidActivity>>{
        return kidActivityDao.searchActivity(query)

    }
}
