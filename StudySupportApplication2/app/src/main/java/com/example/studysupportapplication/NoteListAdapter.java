package com.example.studysupportapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>{
    private final LayoutInflater inflater;
    private List<Note> note;
    private CourseListener courseListener;
    public int location=0;
    private String courseName="", subjectTitle="", noteTitle="";
    private String noteImage="";
    private boolean isImage=false;
    private int chapter=0;
    //private ArrayList<Note> noteArrayList = new ArrayList<>();

    NoteListAdapter(Context context, CourseListener listener) {
        inflater = LayoutInflater.from(context);
        this.courseListener=listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item_course, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        if (note != null) {
            Log.e("Check","Check");
            Note current = note.get(position);
            switch (location) {
                case 0:
                    holder.courseNameItemView.setText(current.getCName());
                    break;
                case 1:
                    holder.courseNameItemView.setText(current.getSTitle());
                    break;
                case 2:
                    holder.courseNameItemView.setText(Integer.toString(current.getCNum()));
                    break;
                case 3:
                    holder.courseNameItemView.setText(current.getNTitle());
                    break;
                case 4:


            }
            holder.courseNameItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Test","From the adapter");
                    switch(location) {
                        case 0:
                        courseName = current.getCName();
                        break;
                        case 1:
                            subjectTitle=current.getSTitle();
                            break;
                        case 2:
                        chapter = current.getCNum();
                        Log.e("Test","CNum: "+Integer.toString(chapter));
                        break;

                        case 3:
                            if(courseName==""){
                                courseName=current.getCName();
                                subjectTitle=current.getSTitle();
                                chapter=current.getCNum();
                            }
                            isImage=current.getIsImage();
                            noteImage=current.getNImage();
                            noteTitle=current.getNTitle();
                            break;


                    }
                    location+=1;
                    courseListener.onCourseClick(courseName,subjectTitle,chapter,location,isImage,noteTitle,noteImage);
                    notifyDataSetChanged();
                }
            });

            //holder.subjectTitleItemView.setText(current.getSTitle());
            //holder.chapterNumItemView.setText(current.getCNum());
            //holder.noteTitleItemView.setText(current.getNTitle());
        } else { // Covers the case of data not being ready yet.
            Log.e("Check","Null!");
            holder.courseNameItemView.setText("");
            holder.subjectTitleItemView.setText("");
            holder.chapterNumItemView.setText("");
            holder.noteTitleItemView.setText("");
        }
    }

    void setNote(List<Note> note) {
        this.note = note;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (note != null) return note.size();
        else return 0;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private final Button courseNameItemView;
        private final Button subjectTitleItemView;
        private final Button chapterNumItemView;
        private final Button noteTitleItemView;


        private NoteViewHolder(View itemView) {
            super(itemView);
            courseNameItemView = itemView.findViewById(R.id.courseListButton);
            subjectTitleItemView = itemView.findViewById(R.id.subjectListButton);
            chapterNumItemView=itemView.findViewById(R.id.chapterListButton);
            noteTitleItemView=itemView.findViewById(R.id.scheduleListButton);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Test","From the adapter");

                }
            });

        }


    }



}
