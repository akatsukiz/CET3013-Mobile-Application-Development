package com.example.studysupportapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>{
    private final LayoutInflater inflater;
    private List<Schedule> schedule;
    public int location=0;
    private String event="Event: ", venue="Venue: ", date="Date: ";
    //private ArrayList<Note> noteArrayList = new ArrayList<>();

    ScheduleListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item_schedule, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        if (schedule != null) {
            Log.e("Check","Check");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Schedule current = schedule.get(position);
            holder.eventTextView.setText(event+current.getETitle());
            holder.venueTextView.setText(venue+current.getVenue());
            holder.dateTextView.setText(date+dateFormat.format(current.getStartDateTime()));


            //holder.subjectTitleItemView.setText(current.getSTitle());
            //holder.chapterNumItemView.setText(current.getCNum());
            //holder.noteTitleItemView.setText(current.getNTitle());
        } else { // Covers the case of data not being ready yet.
            Log.e("Check","Null!");
            holder.eventTextView.setText("");
            holder.venueTextView.setText("");
            holder.dateTextView.setText("");
        }
    }

    void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (schedule != null) return schedule.size();
        else return 0;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        private final TextView eventTextView;
        private final TextView venueTextView;
        private final TextView dateTextView;


        private ScheduleViewHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.eventTextView);
            venueTextView = itemView.findViewById(R.id.venueTextView);
            dateTextView=itemView.findViewById(R.id.listDateTextView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Test","From the adapter");

                }
            });

        }


    }



}
