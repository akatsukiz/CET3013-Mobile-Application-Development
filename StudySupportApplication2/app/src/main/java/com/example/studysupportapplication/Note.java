package com.example.studysupportapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="course_name")
    private String cName;

    @NonNull
    @ColumnInfo(name="subject_title")
    private String sTitle;

    @NonNull
    @ColumnInfo(name="chapter_number")
    private int cNum;

    @ColumnInfo(name="note_title")
    private String nTitle;

    @ColumnInfo(name="is_image")
    private boolean isImage;

    @ColumnInfo(name="note_content")
    private String nContent;

    @ColumnInfo(name="note_image")
    private String nImage;

    public Note(@NonNull String cName, String sTitle, int cNum, String nTitle, boolean isImage, String nContent, String nImage){
        this.cName=cName;
        this.sTitle=sTitle;
        this.cNum=cNum;
        this.nTitle=nTitle;
        this.nContent=nContent;
        this.isImage=isImage;
        this.nImage=nImage;}
    public String getCName(){ return this.cName; }
    public String getSTitle(){ return this.sTitle; }
    public int getCNum(){ return this.cNum; }
    public String getNTitle(){ return this.nTitle; }
    public String getNContent(){ return this.nContent; }
    public boolean getIsImage(){return this.isImage;}
    public String getNImage(){return this.nImage;}
}
