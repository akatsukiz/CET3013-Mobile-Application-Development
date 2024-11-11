package com.example.studysupportapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class FragmentNoteContent extends Fragment{

    private NoteViewModel noteViewModel;

    private int location=0;

    static public String titleKey="NOTE_TITLE", contentKey="NOTE_CONTENT";
    private String content="", title="";
    private int chapter=0;

    private TextView textView, titleTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notecontent, container,false);
        noteViewModel=new ViewModelProvider(this).get(NoteViewModel.class);
        textView=view.findViewById(R.id.contentTextView);
        titleTextView=view.findViewById(R.id.imageContentTitleTextView);






        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if(args!=null){
            content= args.getString(contentKey);
            title=args.getString(titleKey);
        }
        titleTextView.setText(title);
        textView.setText(content);
    }
}
