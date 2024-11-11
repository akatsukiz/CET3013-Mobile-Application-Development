package com.example.studysupportapplication;

import androidx.room.TypeConverter;
import java.util.Date;
public class DateTypeConvertor {
    @TypeConverter
    public static Date toDate(Long dateLong){
        if (dateLong==null){
            return null;
        }
        else{
            return new Date(dateLong);
        }
    }

    @TypeConverter
    public static Long fromDate(Date date){
        if (date==null){
            return null;
        }
        else{
            return date.getTime();
        }
    }
}
