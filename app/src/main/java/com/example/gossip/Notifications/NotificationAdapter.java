package com.example.gossip.Notifications;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gossip.BlankFragment;
import com.example.gossip.Container;
import com.example.gossip.MainActivity;
import com.example.gossip.MemoryData;
import com.example.gossip.R;
import com.example.gossip.chat.Chat;
import com.example.gossip.messages.MessagesAdapter;
import com.example.gossip.messages.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<FriendRequestList> messagesLists;
    private final Context context;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");
    public NotificationAdapter(List<FriendRequestList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request,null));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        FriendRequestList list2=messagesLists.get(position);

//        Bitmap bitmap= MemoryData.getProfilePicture(list2.getMobile(), context.getApplicationContext());
////        Toast.makeText(context.getApplicationContext(),list2.getMobile()+" "+list2.getLastMessage(),Toast.LENGTH_SHORT).show();
//        Log.i("myList",list2.getMobile()+" "+position+"   "+messagesLists.size());
//        if(bitmap!=null){
//            Log.i("myList","andar hu");
//            holder.profilePic.setImageBitmap(bitmap);
////            Toast.makeText(context.getApplicationContext(),list2.getMobile()+" "+list2.getLastMessage(),Toast.LENGTH_SHORT).show();
//        }
//        bitmap=null;
//        for(int i=0;i<messagesLists.size();i++){
//            Log.i("hdmi345",messagesLists.get(i).getMobile()+" "+messagesLists.get(i).getProfilePic()+" "+i);
//        }

        if(list2.isCondition()){
            holder.Confirm.setText("Send");

        } else{
            holder.Confirm.setText("Confirm");
            holder.lastMessage.setVisibility(View.GONE);


        }
        String mobile=MemoryData.getData(context);
        if(list2.getProfilePic()!=null){
//            Log.i("hdmi345",position+"  "+list2.getMobile()+"  "+list2.getLastNode());
            Picasso.get()
                    .load(list2.getProfilePic())
                    .placeholder(R.drawable.default_user) // Placeholder image while loading
                    .error(R.drawable.default_user) // Error image if the URL is invalid
                    .into(holder.profilePic);
        }else{
            holder.profilePic.setImageResource(R.drawable.default_user);
        }
        holder.name.setText(list2.getName());



        holder.Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list2.isCondition()) {
                    if(databaseReference.child(mobile).child("contacts").child(list2.getMobile()).getKey()==null) {
                        databaseReference.child(mobile).child("contacts").child(list2.getMobile()).child("Name").setValue(list2.getName());
//                databaseReference.child(mobile).child("contacts").child(newUser_mobile.getText().toString()).child("profilePic").setValue();
                        databaseReference.child(list2.getMobile()).child("contacts").child(mobile).child("Name").setValue(MainActivity.myName);
                        databaseReference1 = databaseReference;
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        int getChatCounts= (int) snapshot.getChildrenCount()+1;
//                        String chatCount=String.valueOf(getChatCounts);
//                        Log.i("hhhhhh",chatCount+"  "+getChatCounts);

                                DatabaseReference ChatKey = databaseReference.child(mobile).child("chat").push();
                                String chatId = ChatKey.getKey();
                                String oppoMobile = list2.getMobile();
                                databaseReference.child(mobile).child("FrndReq").child(oppoMobile).removeValue();
                                databaseReference1.child(mobile).child("chat").child(chatId).child("messages").setValue("");
                                databaseReference1.child(mobile).child("chat").child(chatId).child("user_1").setValue(mobile);
                                databaseReference1.child(mobile).child("chat").child(chatId).child("user_2").setValue(list2.getMobile());
                                databaseReference1.child(mobile).child("contacts").child(list2.getMobile()).child("profilePic").setValue(snapshot.child(list2.getMobile()).child("profilePic").getValue(String.class));

                                databaseReference1.child(list2.getMobile()).child("contacts").child(mobile).child("Name").setValue(MainActivity.myName);
                                databaseReference1.child(oppoMobile).child("chat").child(chatId).child("messages").setValue("");
                                databaseReference1.child(oppoMobile).child("chat").child(chatId).child("user_1").setValue(oppoMobile);
                                databaseReference1.child(oppoMobile).child("chat").child(chatId).child("user_2").setValue(mobile);
                                databaseReference1.child(oppoMobile).child("contacts").child(mobile).child("profilePic").setValue(snapshot.child(mobile).child("profilePic").getValue(String.class));
                                databaseReference.child(mobile).child("Status").child("Others").child(oppoMobile).child("viewed").setValue("No");
                                databaseReference.child(oppoMobile).child("Status").child("Others").child(mobile).child("viewed").setValue("No");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else{
                        Toast.makeText(context.getApplicationContext(), "Already a friend",Toast.LENGTH_SHORT).show();
                    }

                    databaseReference.child(mobile).child("FrndReq").child(list2.getMobile()).removeValue();
                }else{
                    sendReq(list2.getName());
                    messagesLists.remove(position);
                    updateData(messagesLists);
                }
            }
        });
        holder.Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(mobile).child("FrndReq").child(list2.getMobile()).removeValue();
            }
        });

        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(list2.getProfilePic());
            }
        });

//        long currentTimeStamp=System.currentTimeMillis();
//        long lastNodeTime= list2.getLastNode();
//        long timeDifferenceMillis = currentTimeStamp - lastNodeTime;


//        if (timeDifferenceMillis < TimeUnit.DAYS.toMillis(1)) {
//            // Format the timestamp of the last message
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            String lastMessageTime = sdf.format(new Date(list2.getLastNode()));
////            holder.time.setText(lastMessageTime);
////            System.out.println("Last message was sent/received at " + lastMessageTime);
//        }else {
//            // Convert milliseconds to days
//            long days = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis);
//
//            // Display the time difference in days
//            String timeDifference = days + " day" + (days > 1 ? "s" : "") + " ago";
////            holder.time.setText(timeDifference);
//        }
//        if(list2.getUnseenMessages()==0){
////            holder.unseenMessages.setVisibility(View.GONE);
////            Toast.makeText(context.getApplicationContext(),String.valueOf(list2.getUnseenMessages()),Toast.LENGTH_SHORT).show();
//
//            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
//        }
//        else{
////            holder.unseenMessages.setVisibility(View.VISIBLE);
////            holder.unseenMessages.setText(list2.getUnseenMessages()+"");
//
//            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
//        }
    }
    public void updateData(List<FriendRequestList> messagesLists){
        this.messagesLists=messagesLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profilePic;
        private TextView name,lastMessage;
        private RelativeLayout rootLayout;
        private TextView Confirm;
        private TextView Cancel;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic =itemView.findViewById(R.id.profilePic);
            name =itemView.findViewById(R.id.name);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            Confirm=itemView.findViewById(R.id.Confirm);
            rootLayout=itemView.findViewById(R.id.rootLayout);
            Cancel=itemView.findViewById(R.id.Cancel);

        }
    }
    public void sendReq(String userName){
        String mobile=MainActivity.mobile;
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int[] check = {0};
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.child("Name").getValue().equals(userName)){
                        databaseReference.child(mobile).child("contacts").child(snapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    databaseReference.child(snapshot1.getKey()).child("FrndReq").child(mobile).child("Name").setValue(MainActivity.myName)
                                    ;

                                    databaseReference.child(mobile).child("ProfilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child(snapshot1.getKey()).child("FrndReq").child(mobile).child("profilePic").setValue(snapshot.getValue(String.class));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



                                    check[0] =1;
//                                    Toast.makeText(context,"Request sent",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showImage(String imageUrl) {
        // Create a Dialog instance
        Dialog dialog = new Dialog(context);

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.profile_image_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageView myImage=dialog.findViewById(R.id.MyImage);
        Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.default_user)
                .into(myImage);
        // Show the Dialog
        dialog.show();
    }
}
