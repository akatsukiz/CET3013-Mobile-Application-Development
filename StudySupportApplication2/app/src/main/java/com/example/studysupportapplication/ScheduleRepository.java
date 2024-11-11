package com.example.studysupportapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ScheduleRepository {
    private ScheduleDao sDao;
    private LiveData<List<Schedule>> sAllDetail;
    private LiveData<Integer> sGetId;

    ScheduleRepository(Application application){
        ScheduleRoomDatabase db = ScheduleRoomDatabase.getDatabase(application);
        sDao = db.scheduleDao();
        sAllDetail = sDao.getAllDetail();
        sGetId=sDao.getId();
    }

    public void delete(Schedule schedule){
        ScheduleRoomDatabase.databaseWriteExecutor.execute(()->{
            sDao.delete(schedule);
        });
    }

    LiveData<List<Schedule>> getAllDetail(){
        return sAllDetail;
    }
    LiveData<Integer> getId(){return sGetId;}
    /*LiveData<List<Note>> getSubject(String query){return nDao.getSubject(query);}
    LiveData<List<Note>> getChapter(String query, String query2){return nDao.getChapter(query,query2);}
    LiveData<List<Note>> getNTitle(String query, String query2, int query3){return nDao.getNTitle(query, query2, query3);}
    LiveData<List<Note>> getNContent(String query, String query2, int query3, String query5){return nDao.getNContent(query,query2,query3,query5);}*/

    public void insert(Schedule schedule){
        ScheduleRoomDatabase.databaseWriteExecutor.execute(()->{
            sDao.insert(schedule);
        });
    }
}
