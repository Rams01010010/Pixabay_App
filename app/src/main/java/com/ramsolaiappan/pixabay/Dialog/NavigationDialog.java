package com.ramsolaiappan.pixabay.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ramsolaiappan.pixabay.R;


public class NavigationDialog extends DialogFragment {

    private int stepCounter = 0;
    private int[] imageResList = {R.drawable.nav_img_1,R.drawable.nav_img_2,R.drawable.nav_img_3,R.drawable.nav_img_1};
    private String[] messageList = {"Click the Search button to search with added options for your Search","Click the ThumbsUp Button to 'Like' the picture","Click the Image to view Fully and see Image's Details","Click the ThumbsUp Button to view Images you Liked"};

    private NavDialogListener navDialogListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_main_activity,null,false);
        ImageView navIV = view.findViewById(R.id.navIV);
        TextView navTV = view.findViewById(R.id.navTV);
        Button okBtn = view.findViewById(R.id.okBtn);

        navIV.setImageResource(imageResList[stepCounter]);
        navTV.setText(messageList[stepCounter]);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepCounter++;
                if(stepCounter != 4)
                {
                    navIV.setImageResource(imageResList[stepCounter]);
                    navTV.setText(messageList[stepCounter]);
                }
                else
                {
                    if(navDialogListener != null)
                    {
                        navDialogListener.onCancel();
                    }
                }
            }
        });
        builder.setView(view);
        return builder.create();
    }

    public interface NavDialogListener
    {
        void onCancel();
    }
    public void setNavDialogListener(NavDialogListener listener)
    {
        this.navDialogListener = listener;
    }
}
