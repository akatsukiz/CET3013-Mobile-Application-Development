package com.example.studysupportapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScheduleViewModel extends AndroidViewModel{

    private ScheduleRepository sRespository;
    private LiveData<List<Schedule>> sAllDetail;
    public ScheduleViewModel(Application application) {
        super(application);
        sRespository = new ScheduleRepository(application);
        sAllDetail = sRespository.getAllDetail();
    }
    LiveData<List<Schedule>> getAllDetail(){
        return sAllDetail;
    }
    LiveData<Integer> getId(){return sRespository.getId();}
    /*LiveData<List<Note>> getSubject(String query) {return nRespository.getSubject(query);}
    LiveData<List<Note>> getChapter(String query, String query2) {return nRespository.getChapter(query,query2);}
    LiveData<List<Note>> getNTitle(String query, String query2, int query3) {return nRespository.getNTitle(query,query2,query3);}
    LiveData<List<Note>> getNContent(String query, String query2, int query3, String query5){return nRespository.getNContent(query,query2,query3,query5);}*/
    public void insert(Schedule schedule){
        sRespository.insert(schedule);
    }
    public void delete(Schedule schedule) {sRespository.delete(schedule);}
}
