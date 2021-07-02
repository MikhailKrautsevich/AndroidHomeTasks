package com.example.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class PhotoDialog extends DialogFragment {

    private static final String ARG_FILE  = "ARG_file" ;
    private static final String LOG = "PhotoDialog_log" ;

    private PhotoDialog() {}

    static PhotoDialog newInstance(File file) {
        Bundle bundle = new Bundle() ;
        bundle.putSerializable(ARG_FILE, file);

        Log.d(LOG, "file == null : " + (file == null)) ;

        PhotoDialog fragment = new PhotoDialog() ;
        fragment.setArguments(bundle);
        return fragment ;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_big_photo, null) ;

        ImageView imageView = v.findViewById(R.id.big_photo_image_view) ;
        File file = null ;

        if (getArguments() != null) {
            file = (File) getArguments().getSerializable(ARG_FILE) ;
        }

        if (file != null && file.exists() && imageView != null) {
            imageView.setImageBitmap(PictureUtils.getScaledBitmap(file.getPath(), getActivity()));
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
