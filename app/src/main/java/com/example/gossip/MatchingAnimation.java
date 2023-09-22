package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nitish.typewriterview.TypeWriterView;

import java.util.ArrayList;
import java.util.List;

public class MatchingAnimation extends AppCompatActivity {

    static ValueEventListener stateChangeListener;
    String myUserName;
    public static ValueEventListener valueEventListener;
    TextView stop;
    TypeWriterView text;
    boolean condition;
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    List<String> myTags=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_anim_window);
        myTags=Container.getMyTags();
        myUserName=getIntent().getStringExtra("name");
        text=findViewById(R.id.textView);
        String[] arr=new String[3];
        String t="Engage in spontaneous conversations with like-minded individuals using our Random Chat feature. Connect with others who share your interests while ensuring your personal information remains confidential – only your username is visible to maintain your privacy.";
        text.animateText(t);
//        arr[0]="Get ready for a thrilling match! Excitement awaits as you connect with someone special. ";
//        arr[1]="Only your username will be visible; your personal information remains confidential.";
//        arr[2]="Engage in spontaneous conversations with like-minded people across the globe!";
//        Handler handler = new Handler();
//        final int[] currentTextIndex = {0};
//        Runnable textRunnable = new Runnable() {
//            @Override
//            public void run() {
//                text.animateText(arr[currentTextIndex[0]]);
//                currentTextIndex[0]++;
//
//                if (currentTextIndex[0] < arr.length) {
//                    // Schedule the next text change after a delay (e.g., 2 seconds)
//                    handler.postDelayed(this, 6000);
//                }
//            }
//        };
//
//        handler.postDelayed(textRunnable, 10);
        stop=findViewById(R.id.Stop);
        condition=true;

//        Log.i("ttry","yes");
        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(0);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(2);
                finish();
            }
        });
        if(Container.ChatRoomName!=null){
            databaseReference.child("RandomChat").child("ChatRoom").child(Container.ChatRoomName).removeValue();
            Container.ChatRoomName=null;
        }
        stateChangeListener=databaseReference.child("RandomChat").child("Status").child(myUserName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(Container.Stop){
//                    finish();
//                }

//                Toast.makeText(getContext(),"bhar",Toast.LENGTH_SHORT).show();
                if (snapshot.child("State").getValue() == Long.valueOf(1) && condition) {
//                    condition=false;
                    Intent intent = new Intent(getApplicationContext(), StartRandomChat.class);
                    intent.putExtra("name", snapshot.child("OppoName").getValue(String.class));
                    intent.putExtra("myUserName", myUserName);
                    intent.putExtra("chatRoomName", snapshot.child("CRN").getValue(String.class));
                    condition=false;
                    startActivity(intent);
                    databaseReference.removeEventListener(stateChangeListener);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        if(Container.Stop && valueEventListener!=null){
//            databaseReference.removeEventListener(valueEventListener);
//            finish();
//        }
//                Log.i("HashTagsList", String.valueOf(myTags));
//            Toast.makeText(getApplicationContext(),"OnResume",Toast.LENGTH_SHORT).show();

        if (!myTags.isEmpty()) {
            databaseReference.child("RandomChat").child("HashTags").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int flag = 0;
                    for (String s : myTags) {
//                                if(condition==0) {
                        if (snapshot.hasChild(s) && snapshot.child(s).getChildrenCount() > 0) {
                            flag = 1;
                            int users_count = (int) snapshot.child(s).getChildrenCount();
                            for (DataSnapshot snapshot1 : snapshot.child(s).getChildren()) {
                                String oppoUserName = snapshot1.getKey();
                                if(!oppoUserName.equals(myUserName) && !oppoUserName.equals(Container.lastUser) ){
                                    databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("State").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                            if (snapshot2.getValue() == Long.valueOf(0) ) {
                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("CRN").setValue(myUserName + " " + oppoUserName);
                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("CRN").setValue(myUserName + " " + oppoUserName);
                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("OppoName").setValue(myUserName);
                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("OppoName").setValue(oppoUserName);
                                                databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(1);
                                                databaseReference.child("RandomChat").child("Status").child(oppoUserName).child("State").setValue(1);
//                                                    condition = 1;
//                                                        Intent intent = new Intent(getContext(), StartRandomChat.class);
//                                                        intent.putExtra("name", oppoUserName);
//                                                        intent.putExtra("myUserName", myUserName);
//                                                        startActivity(intent);


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
//                                            randomUser--;
                            }


                            // Now match with the Random User
                        }
                        databaseReference.child("RandomChat").child("HashTags").child(s).child(myUserName).setValue("");
//                                        databaseReference.child("RandomChat").child("Status").child(myUserName).child("State").setValue(0);
//                                        databaseReference.child("RandomChat").child("Status").child(myUserName).child("CRN").setValue("");
//                                    }
//                                }else break;

                    }
                    if (flag == 0)
                        text.setText("No users Online with your hashtags");
//                        myTags.clear();
//                        flexboxLayout.removeAllViews();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
//                    for(String s:myTags){
//
//                    }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        }

    @Override
    protected void onPause() {
        super.onPause();
//        try{
//            databaseReference.removeEventListener(valueEventListener);
//            databaseReference.removeEventListener(stateChangeListener);
//        }catch (Exception e){}

    }
}