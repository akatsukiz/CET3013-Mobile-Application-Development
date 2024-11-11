package com.example.studysupportapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class FragmentNoteContentImage extends Fragment{

    private NoteViewModel noteViewModel;

    private int location=0;

    static public String titleKey="NOTE_TITLE", contentKey="NOTE_CONTENT";
    private String content="", title="";
    private int chapter=0;

    private TextView textView;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notecontentimage, container,false);
        noteViewModel=new ViewModelProvider(this).get(NoteViewModel.class);
        textView=view.findViewById(R.id.imageContentTitleTextView);
        imageView=view.findViewById(R.id.contentImageView);





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
        Bitmap bitmap = convertBitmap(args.getString(contentKey));
        textView.setText(title);
        imageView.setImageBitmap(bitmap);
    }
    public Bitmap convertBitmap(String encodedString){
        try{
            byte[] encodedByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);
            Log.e("Image error", "Got image");
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            Log.e("Image error", "No image");
            return null;
        }
    }
}
