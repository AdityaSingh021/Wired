package com.example.gossip;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

public class LoadingAnimation {
    Context context;
    Dialog dialog;
    public void Load() {
        // Create a Dialog instance
        // Set the layout for the Dialog
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_start_screen_child2);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.CENTER);
        ProgressBar progressBar=dialog.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        // Show the Dialog
//        dialog.show();
    }
    public void show(){
        if(dialog!=null && !dialog.isShowing()) dialog.show();

    }
    public void dismiss(){
        if(dialog!=null && dialog.isShowing()) dialog.dismiss();

    }
    public LoadingAnimation(Context context){
        this.context=context;
    }
}
