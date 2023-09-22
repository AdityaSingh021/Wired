package com.example.gossip.Status;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gossip.BlankFragment;
import com.example.gossip.Container;
import com.example.gossip.MainActivity;
import com.example.gossip.R;
import com.example.gossip.ReportDialog;
import com.example.gossip.chat.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatusGallery extends AppCompatActivity {

    String Name;
    String mobile;
    String chatId;
    private ProgressBar progressBar;
    RelativeLayout bottomBar;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_gallery);
//        Animatoo.animateZoom(getApplicationContext());
        ImageView status = findViewById(R.id.Status);
        RelativeLayout left = findViewById(R.id.left);
        RelativeLayout right = findViewById(R.id.right);
        progressBar = findViewById(R.id.progressBar);
        bottomBar=findViewById(R.id.bottomBar);
        int index = getIntent().getIntExtra("Index", 0);
        if(index==-1){
            mobile= MainActivity.mobile;
            Name=BlankFragment.myName;
            bottomBar.setVisibility(View.GONE);

        }
        else{
            mobile = Container.statusList.get(index).getMobile();
            Name = Container.statusList.get(index).getName();
            chatId = Container.statusList.get(index).getChatId();
        }

        EditText message = findViewById(R.id.message);
        RelativeLayout sendButton = findViewById(R.id.sendBtn);
        ImageView profilePic = findViewById(R.id.profilePic);
        TextView profileName = findViewById(R.id.profileName);
        profileName.setText(Name);
        ImageView menu=findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDialog reportDialog=new ReportDialog();
                reportDialog.openCenteredDialog(StatusGallery.this,"The story has been reported.","Report this story",false,"");
            }
        });
//        String val=databaseReference.child()
//        Toast.makeText(getApplicationContext(),mobile+"  "+Container.getMobile(),Toast.LENGTH_SHORT).show();

        List<String> images = new ArrayList<>();
        final int[] check = {0};
        databaseReference.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long currTime = System.currentTimeMillis();
                images.clear();
//                if (timeDifferenceMillis <= TimeUnit.DAYS.toMillis(1)) {
                    for (DataSnapshot dataSnapshot : snapshot.child("Status").child("MyStatus").getChildren()) {
                        long time = currTime;
                        try {
                            time = dataSnapshot.child("statusTime").getValue(Long.class);
                        } catch (Exception e) {
                        }
                        ;
                        long timeDifferenceMillis = currTime - time;
                        if (timeDifferenceMillis <= TimeUnit.DAYS.toMillis(1)) {
                        String url = dataSnapshot.child("statusImage").getValue(String.class);
//                        Log.i("abcd1234", url);
                        if(url!=null) images.add(url);
                        if (check[0] == 0) {
                            Glide.with(getApplicationContext())
                                    .load(snapshot.child("profilePic").getValue(String.class))
                                    .error(R.drawable.cant_load_image)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            profilePic.setImageResource(R.drawable.default_user);
                                            return true;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            RoundedBitmapDrawable circularDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), ((BitmapDrawable) resource).getBitmap());
                                            circularDrawable.setCircular(true);
                                            profilePic.setImageDrawable(circularDrawable);

                                            return true;
                                        }
                                    })
                                    .into(profilePic);
//                        Picasso.wi
                            Glide.with(getApplicationContext())
                                    .load(url)
                                    .placeholder(R.drawable.loading_image)
                                    .into(status);
                            check[0] = 1;
                        }
                    }
                    }
                    progressBar.setVisibility(View.GONE);

                }
//            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final int[] i = {0};
        if(i[0]==images.size()-1){
            if(!mobile.equals(MainActivity.mobile))databaseReference.child(MainActivity.mobile).child("Status").child("Others").child(mobile).child("viewed").setValue("Yes");
            else databaseReference.child(MainActivity.mobile).child("Status").child("MyStatus").child("viewed").setValue("Yes");
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i[0] >= 1) {
                    progressBar.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load(images.get(--i[0]))
                            .placeholder(R.drawable.loading_image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(status);
                    progressBar.setVisibility(View.GONE);
//                    Picasso.get().load().into(status);
                } else {
//                    databaseReference.child(mobile).child("Status").child("Others").child("viewed").setValue("Yes");
                    if (index > 0) {
                        Intent i = new Intent(getApplicationContext(), StatusGallery.class);
                        i.putExtra("Index", index - 1);
                        startActivity(i);
                        Animatoo.INSTANCE.animateSlideRight(StatusGallery.this);
//                        onDestroy();
                        finish();
                    }
                    if(index==0 && BlankFragment.Mystatus!=null){
                        Intent i = new Intent(getApplicationContext(), StatusGallery.class);
                        i.putExtra("Index", index - 1);
                        startActivity(i);
                        Animatoo.INSTANCE.animateSlideRight(StatusGallery.this);
//                        onDestroy();
                        finish();
                    }else{
                            onBackPressed();

                    }


                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("imagesList",images.toString());
                if (i[0] < images.size() - 1) {
                    if(images.size()>=2 && i[0]== images.size()-2){
                        if(!mobile.equals(MainActivity.mobile))databaseReference.child(MainActivity.mobile).child("Status").child("Others").child(mobile).child("viewed").setValue("Yes");
                        else databaseReference.child(MainActivity.mobile).child("Status").child("MyStatus").child("viewed").setValue("Yes");
                    }
                    if(images.get(++i[0])!=null) {
                        Glide.with(getApplicationContext())
                                .load(images.get(i[0]))
                                .placeholder(R.drawable.loading_image)
                                .into(status);
                    }
                } else {
//                    Toast.makeText(getApplicationContext(),MainActivity.mobile,Toast.LENGTH_SHORT).show();
                    if(!mobile.equals(MainActivity.mobile))databaseReference.child(MainActivity.mobile).child("Status").child("Others").child(mobile).child("viewed").setValue("Yes");
                    else databaseReference.child(MainActivity.mobile).child("Status").child("MyStatus").child("viewed").setValue("Yes");
                    if (index < Container.statusList.size() - 1) {
                        Intent i = new Intent(getApplicationContext(), StatusGallery.class);
                        i.putExtra("Index", index + 1);
                        startActivity(i);
                        Animatoo.INSTANCE.animateSlideLeft(StatusGallery.this);
//                        onDestroy();
                        finish();
                    }else{
                        onBackPressed();
                    }
                }
            }
        });
//        Picasso.get().load().into(holder.statusImage);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat=new Chat();
                String getTxtMessage=message.getText().toString();
                if(!getTxtMessage.isEmpty()){

                    chat.sendMessage(getTxtMessage,mobile,Container.getMobile(),chatId,message);
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    public void openCenteredDialog() {
//        // Create a Dialog instance
//        Dialog dialog = new Dialog(StatusGallery.this);
//
//        // Set the layout for the Dialog
//        dialog.setContentView(R.layout.report_dialog);
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
//
//        // Optionally set other properties for the Dialog, e.g., size, title, etc.
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        dialog.setTitle("Your Dialog Title");
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//        TextView report=dialog.findViewById(R.id.report);
//        report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"The story has be reported",Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//        // Show the Dialog
//        dialog.show();
//    }
}