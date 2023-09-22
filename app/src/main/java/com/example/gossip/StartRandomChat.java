package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gossip.Status.StatusGallery;
import com.example.gossip.chat.Chat;
import com.example.gossip.chat.chatAdapter;
import com.example.gossip.chat.chatList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class StartRandomChat extends AppCompatActivity {

    private long lastBackPressTime = 0;

    private TextView text;
    private Dialog dialog;
    private String chatRoomName;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    //    public boolean check;
    private int check1;
    private String mobile;
    private boolean condition=true;
    String getName="";

    private ValueEventListener exitOrNot;
    private final List<chatList> chatLists=new ArrayList<>();
    String getUserMobile="";
    String chatKey;
    private int generatedChatKey;
    private String myUserName;
    Handler handler;
    private RecyclerView chattingRecyclerView;
    private TextView skip;
    private TextView stop;
    private boolean canStart;
    private com.example.gossip.chat.chatAdapter chatAdapter;
    private ImageView options;
    private boolean check=true;
    private ValueEventListener mValueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_with_random);

//        mobile= BlankFragment.mobile;
        int[] check1 = {0};
        final TextView name=findViewById(R.id.name_chat);
        final EditText message=findViewById(R.id.message);
        final ImageView sendButton=findViewById(R.id.sendBtn);
        canStart=true;
        options=findViewById(R.id.Options);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDialog reportDialog=new ReportDialog();
                reportDialog.openCenteredDialog(StartRandomChat.this,"User reported.","Report user",true,getName);
            }
        });
//        condition=true;
        TextView Status=findViewById(R.id.Status);
        chattingRecyclerView=findViewById(R.id.chattingRecyclerView);
//        databaseReference.child(mobile).child("Status").setValue(1);
        // data from message adapter
        getName=getIntent().getStringExtra("name");
        name.setText(getName);
        final long[] LastMsgTs = {0};
        TextView operations=findViewById(R.id.operations);
        operations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCenteredDialog();
            }
        });
//        skip=findViewById(R.id.skip);
//        stop=findViewById(R.id.stop);
//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                skip();
//            }
//        });
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stop();
//            }
//        });
//        chatKey=getIntent().getStringExtra("chat_key");
//        final String getMobile=getIntent().getStringExtra("mobile");
        //get user mobile from memory
//        getUserMobile= MemoryData.getData(getApplicationContext());

        myUserName=getIntent().getStringExtra("myUserName");
        chatRoomName=getIntent().getStringExtra("chatRoomName");
        ImageView options=findViewById(R.id.options);
//        ImageView camera=findViewById(R.id.camera);
        chatAdapter=new chatAdapter(chatLists,getApplicationContext(),myUserName);
//        chatAdapter.userMobile=
        chattingRecyclerView.setAdapter(chatAdapter);
        RelativeLayout topBar=findViewById(R.id.topBar);

        final float originalTranslationY = topBar.getTranslationY();
        final int scrollThreshold = 200; // Adjust this threshold as needed

        final AtomicInteger scrollOffset = new AtomicInteger(0);
        final AtomicBoolean isControlsVisible = new AtomicBoolean(true);
        databaseReference.child("RandomChat").child("ChatRoom").child(chatRoomName).setValue("");
        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(2);

//        boolean condition=false;
        final boolean[] work = {true};
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chattingRecyclerView.hasFixedSize();
//        exitOrNot=databaseReference.child("RandomChat").child("ChatRoom").child(chatRoomName).child("Leave").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                Toast.makeText(getApplicationContext(),"Yess",Toast.LENGTH_SHORT).show();
//               if(snapshot.getValue()==Long.valueOf(0) ){
//                   stop();
////                   finish();
//               }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        databaseReference.child("RandomChat").child("ChatRoom").child(myUserName+" "+getName).child("chat").setValue("");
//        databaseReference.child("RandomChat").child("ChatRoom").child(myUserName+" "+getName).child("chat").setValue("");
        final boolean[] stop = {true};
        mValueEventListener=databaseReference.child("RandomChat").child("ChatRoom").child(chatRoomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
//                    Log.i("trreasure","1");
//                    skip();
//                }
                    if (snapshot.hasChild("chat")) {
                        Log.i("trreasure", "2");
                        chatLists.clear();
                        for (DataSnapshot messagesSnapshot : snapshot.child("chat").getChildren()) {
                            if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("userName")) {
                                final String messageTimeStamp = messagesSnapshot.child("timestamp").getValue(String.class);
                                final String userN = messagesSnapshot.child("userName").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);


                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimeStamp));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                chatAdapter.updateChatList(chatLists);

//                                if ( Long.parseLong(messageTimeStamp) > LastMsgTs[0]) {
                                chatList chatList = new chatList(userN, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date),"","",false);
                                chatLists.add(chatList);
                                check = false;
//                                    MemoryData.saveLastMsgTs(messageTimeStamp,chatKey,getApplicationContext());
                                chatAdapter.updateChatList(chatLists);
                                chattingRecyclerView.scrollToPosition(chatLists.size() - 1);
//                                    LastMsgTs[0] = Long.parseLong(messageTimeStamp);

//                                }
//                            }

                            }
                        }
                    }
                }else{
////                    databaseReference.child("RandomChat").child("ChatRoom").child(chatRoomName).removeValue();
//                    if(mValueEventListener!=null) databaseReference.removeEventListener(mValueEventListener);
//                    mValueEventListener=null;
////                    work[0] =false;
//                    if(!condition){
//                        Log.i("Checking","2");
//                    databaseReference.removeValue()
//                    skip();
//                    stop();
//                    if(stop[0]){
//                        if(condition){
//                            stop();
//                        }else skip();
//                        stop[0] =false;
//                    }
                    if(condition) skip();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                skip();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getTxtMessage = message.getText().toString();
                // Get current Timestamp
                final String currentTimeStamp = String.valueOf(System.currentTimeMillis());

                // Use push() to generate a unique key for the new message
                DatabaseReference newMessageRef = databaseReference.child("RandomChat").child("ChatRoom").child(chatRoomName).child("chat").push();
                String messageId = newMessageRef.getKey();

                // Set the message data under the unique key
                newMessageRef.child("timestamp").setValue(currentTimeStamp);
                newMessageRef.child("msg").setValue(getTxtMessage);
                newMessageRef.child("userName").setValue(myUserName);


                message.setText("");
            }
        });
    }


    public void skip(){
        Container.ChatRoomName=chatRoomName;
        Container.lastUser=getName;
            Intent i=new Intent(getApplicationContext(),MatchingAnimation.class);
            i.putExtra("name",myUserName);
            startActivity(i);
            finish();
//        }
    }
    public void stop(){
//        condition=true;
//        databaseReference.removeEventListener(mValueEventListener);
        Container.lastUser=getName;

        databaseReference.removeEventListener(mValueEventListener);
        Container.ChatRoomName=chatRoomName;
        mValueEventListener=null;
//        databaseReference.removeEventListener(exitOrNot);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        }, 10000);






//        databaseReference.child("RandomChat").child("Status").child(myUserName).removeValue();
//        Container.setStop(true);

//        mValueEventListener=null;
        Log.i("Checking","1");
//        MatchingAnimation matchingAnimation=new MatchingAnimation();
//        matchingAnimation.finish();
//        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue("1");
//        RandomChat.dialog.dismiss();
        finish();

//        onDestroy();
    }
    public void openCenteredDialog() {
        // Create a Dialog instance
        Dialog dialog = new Dialog(StartRandomChat.this);

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.random_chat_operations);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView skip=dialog.findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseReference.child("RandomChat").child("ChatRoom").child(chatRoomName).removeValue();
                skip();
            }
        });
        TextView stop=dialog.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition=false;

//                mValueEventListener=null;
                databaseReference.child("RandomChat").child("ChatRoom").child(chatRoomName).removeValue();
//                stop();
                finish();
            }
        });
        // Show the Dialog
        dialog.show();

    }

    @Override
    public void finish() {
        super.finish();
    }




//    public void startMatching(Context context, String myUserName, List<String> myTags){
//        Log.i("Steps", "1");
//        openCenteredDialog(myUserName);
////                Log.i("HashTagsList", String.valueOf(myTags));
//        if (!myTags.isEmpty()) {
//            databaseReference.child("RandomChat").child("HashTags").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    Toast.makeText(context,"Inside",Toast.LENGTH_SHORT).show();
//                    int flag = 0;
//                    int condition = 0;
//                    for (String s : myTags) {
////                                if(condition==0) {
//                        if (snapshot.hasChild(s) && snapshot.child(s).getChildrenCount() > 0) {
//                            flag = 1;
//                            int users_count = (int) snapshot.child(s).getChildrenCount();
////                                        Random random = new Random();
////                                        int randomUser = random.nextInt(users_count - 1 + 1);
////                                    Toast.makeText(getContext(),String.valueOf(randomUser),Toast.LENGTH_SHORT).show();
//                            for (DataSnapshot snapshot1 : snapshot.child(s).getChildren()) {
//                                String oppoUserName = snapshot1.getKey();
//                                if(!oppoUserName.equals(myUserName)) {
//                                    databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("State").addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
//                                            if (snapshot2.getValue() == Long.valueOf(0)) {
//                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("CRN").setValue(myUserName + " " + oppoUserName);
//                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("CRN").setValue(myUserName + " " + oppoUserName);
//                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("OppoName").setValue(myUserName);
//                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("OppoName").setValue(oppoUserName);
//                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(1);
//                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("State").setValue(1);
////                                                condition = 1;
////                                                        Intent intent = new Intent(getContext(), StartRandomChat.class);
////                                                        intent.putExtra("name", oppoUserName);
////                                                        intent.putExtra("myUserName", myUserName);
////                                                        startActivity(intent);
//
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
//                                }
////                                            randomUser--;
//                            }
//
//
//                            // Now match with the Random User
//                        }
//                        databaseReference.child("RandomChat").child("HashTags").child(s).child(myUserName).setValue("");
////                                Toast.makeText(getContext(), "Working", Toast.LENGTH_SHORT).show();
////                                        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(0);
////                                        databaseReference.child("RandomChat").child("Status").child(myUserName).child("CRN").setValue("");
////                                    }
////                                }else break;
//
//                    }
//                    if (flag == 0)
//                        text.setText("No users Online with your hashtags");
////                                Toast.makeText(getContext(), "No users Online with your hashtags", Toast.LENGTH_SHORT).show();
////                    myTags.clear();
////                    flexboxLayout.removeAllViews();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
////                    for(String s:myTags){
////
////                    }
//        }
//    }


//    public void openCenteredDialog(String myUserName) {
//        Log.i("Steps", "2");
//        // Create a Dialog instance
//        dialog = new Dialog(StartRandomChat.this);
//
//        // Set the layout for the Dialog (your custom layout)
//        dialog.setContentView(R.layout.matching_anim_window);
//
//        // Set the corner radius for the Dialog
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
//
//        // Optionally set other properties for the Dialog, e.g., size, title, etc.
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.setTitle("Your Dialog Title");
//        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(0);
//        text=dialog.findViewById(R.id.textView);
//        TextView stop=dialog.findViewById(R.id.Stop);
//
//
////        databaseReference.child("RandomChat").child("Status").child(myUserName).addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////
//////                Toast.makeText(getContext(),"bhar",Toast.LENGTH_SHORT).show();
////                if (snapshot.child("State").getValue() == Long.valueOf(1) && condition) {
////                    condition=false;
//////                    Toast.makeText(getContext(),"andar",Toast.LENGTH_SHORT).show();
////                    Intent intent = new Intent(StartRandomChat.this, StartRandomChat.class);
////                    intent.putExtra("name", snapshot.child("OppoName").getValue(String.class));
////                    intent.putExtra("myUserName", myUserName);
////                    intent.putExtra("chatRoomName", snapshot.child("CRN").getValue(String.class));
//////                    condition=1;
////                    StartRandomChat.this.startActivity(intent);
////                    dialog.dismiss();
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
//
//
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(2);
//            }
//        });
//
//        // Show the Dialog
//        dialog.show();
////        finish();
//    }

    @Override
    protected void onPause() {
        super.onPause();
//        databaseReference.removeEventListener(mValueEventListener);
//        mValueEventListener=null;
    }
}