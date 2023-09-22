package com.example.gossip;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class OpenDialogBox {




    public void ImportContactsDialog(Context context, String text, String color, String toast) {
        // Create a Dialog instance
        Dialog dialog = new Dialog(context);

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.report_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView report=dialog.findViewById(R.id.report);
        report.setText(text);
        report.setTextColor(Color.parseColor(color));
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,toast,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        // Show the Dialog
        dialog.show();
    }
}
