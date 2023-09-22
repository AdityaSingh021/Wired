package com.example.gossip;

// ImportContactsTask.java (Create a new Java file for the AsyncTask)
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.siddydevelops.customlottiedialogbox.CustomLottieDialog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ImportContactsTask extends AsyncTask<Void, Void, Void> {
    private CustomLottieDialog customLottieDialog;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");;

    private HashMap<String,String> map=new HashMap<>();
    private DatabaseReference databaseReference1;
    String mobile=MainActivity.mobile;
    private Context context;

    public ImportContactsTask(Context context, CustomLottieDialog customLottieDialog) {
        this.context = context;
        this.customLottieDialog = customLottieDialog;
    }

    @Override
    protected void onPreExecute() {
        // Show the custom dialog before starting the background task
        customLottieDialog.show();
    }

    @SuppressLint("Range")
    @Override
    protected Void doInBackground(Void... voids) {
        Cursor cursor = null;
        try {
            // Query the contacts database
            cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Get contact name and phone number
                    @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    final String finalDisplayName=displayName.replaceAll("\\.","");

//                    Toast.makeText(context.getApplicationContext(), displayName, Toast.LENGTH_SHORT).show();
                    // Check if the contact has a phone number
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor phoneCursor = context.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{contactId},
                                null
                        );

                        if (phoneCursor != null) {
                            while (phoneCursor.moveToNext()) {
                                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                // Do something with the contact details (e.g., display in a RecyclerView)
//                                displayName=displayName.replaceAll(".","");

//                                Log.i("Namexx",displayName+"  2  ");
                                if(phoneNumber.length()>=10){
                                    phoneNumber=phoneNumber.replaceAll(" ","");
                                    if(phoneNumber.length()>10){
                                        phoneNumber=phoneNumber.replace("+91","");
                                        if(phoneNumber.charAt(0)=='0') phoneNumber=phoneNumber.substring(1);
                                    }
                                    String finalPhoneNumber = phoneNumber;
                                    if(!finalPhoneNumber.equals(MainActivity.mobile)){
//                                        Log.i("Namexx",displayName);
//                                        if(finalDisplayName.equals("Sss") || displayName.equals("Maa") ){
//                                            Log.i("Namexx",displayName);
//                                        }
                                            map.put(finalPhoneNumber,finalDisplayName);
                                    }

//                                    databaseReference1=databaseReference;
//
//                                    databaseReference1.child(mobile).child("contacts").child(finalPhoneNumber).child("Name").setValue(displayName);
//                                    databaseReference1.child(phoneNumber).child("contacts").child(mobile).child("Name").setValue(MainActivity.myName);
//                                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            int getChatCounts= (int) snapshot.getChildrenCount()+1;
//                                            String chatCount=String.valueOf(getChatCounts);
//                                            Log.i("hhhhhh",chatCount+"  "+getChatCounts);
//
//                                            databaseReference1.child(mobile).child("chat").child(chatCount).child("messages").setValue("");
//                                            databaseReference1.child(mobile).child("chat").child(chatCount).child("user_1").setValue(mobile);
//                                            databaseReference1.child(mobile).child("chat").child(chatCount).child("user_2").setValue(finalPhoneNumber);
//                                            databaseReference1.child(mobile).child("contacts").child(finalPhoneNumber).child("profilePic").setValue(snapshot.child(finalPhoneNumber).child("profilePic").getValue(String.class));
//
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
                                }
                                // ... Your other logic here
                            }
                            phoneCursor.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        upload();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // Dismiss the custom dialog after the contacts import is completed
        customLottieDialog.dismiss();
    }

    public void upload(){
        for(Map.Entry<String,String> entry:map.entrySet()){
            String finalPhoneNumber=entry.getKey();
            HashSet<String> set=new HashSet<>();
            set.add("8853636539");
            set.add("8210506397");
            set.add("9818514406");
            set.add("9540809479");
            set.add("7895714652");
            String finalDisplayName=entry.getValue();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.i("finalmobilenumber",finalPhoneNumber);
                    if(!finalPhoneNumber.contains(",") && !finalPhoneNumber.contains("#") && !finalPhoneNumber.contains("$") && !finalPhoneNumber.contains("[") && !finalPhoneNumber.contains("]")) {
                        if (snapshot.child("Users").hasChild(finalPhoneNumber) && !snapshot.child(mobile).child("contacts").child(finalPhoneNumber).exists()) {
//                        Log.i("Namexx",displayName+"   aaa");
//                                                Log.i("Namexx",finalDisplayName+"   3");
                            databaseReference.child(mobile).child("contacts").child(finalPhoneNumber).child("Name").setValue(finalDisplayName);
                            databaseReference.child(finalPhoneNumber).child("contacts").child(mobile).child("Name").setValue(MainActivity.myName);

                            boolean condition = true;
                            for (DataSnapshot snapshot1 : snapshot.child(finalPhoneNumber).child("chat").getChildren()) {
                                if (snapshot1.hasChild("messages") && (snapshot1.child("user_1").getValue().equals(mobile) || snapshot1.child("user_2").getValue().equals(mobile))) {
                                    Log.i("adityaaa", "2");
                                    String ChatId = snapshot1.getKey();
//                                Log.i("ooppoo",ChatId);
                                    databaseReference.child(mobile).child("chat").child(ChatId).child("messages").setValue("");
                                    databaseReference.child(mobile).child("chat").child(ChatId).child("user_1").setValue(mobile);
                                    databaseReference.child(mobile).child("chat").child(ChatId).child("user_2").setValue(finalPhoneNumber);
                                    databaseReference.child(mobile).child("contacts").child(finalPhoneNumber).child("profilePic").setValue(snapshot.child("Users").child(finalPhoneNumber).child("profilePic").getValue(String.class));
//                                databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);;
//                                databaseReference.child(getMobile).child("chat").child(snapshot1.getKey()).child("messages").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                                    condition = false;
                                    break;
                                }
                            }
//                                                    int getChatCounts= (int) snapshot.child(mobile).child("Chats").getChildrenCount()+1;
//                                                    String chatCount=String.valueOf(getChatCounts);
//                                                Log.i("hhhhhh",mobile);
                            if (!snapshot.child(mobile).child("contacts").child(finalPhoneNumber).exists() && condition) {
                                DatabaseReference ref = databaseReference.child("chat").push();
                                String chatCount = ref.getKey();
                                databaseReference.child(mobile).child("chat").child(chatCount).child("messages").setValue("");
                                databaseReference.child(mobile).child("chat").child(chatCount).child("user_1").setValue(mobile);
                                databaseReference.child(mobile).child("chat").child(chatCount).child("user_2").setValue(finalPhoneNumber);
                                databaseReference.child(mobile).child("contacts").child(finalPhoneNumber).child("profilePic").setValue(snapshot.child("Users").child(finalPhoneNumber).child("profilePic").getValue(String.class));
                            }

                        }

                        else if(snapshot.child(mobile).child("contacts").child(finalPhoneNumber).exists()){
                            databaseReference.child(mobile).child("contacts").child(finalPhoneNumber).child("Name").setValue(finalDisplayName);
                        }

//                        else if(snapshot.child(mobile).child("contacts").child(finalPhoneNumber).exists() && !set.contains(finalPhoneNumber)){
//                            databaseReference.child(mobile).child("contacts").child(finalPhoneNumber).removeValue();
//                            databaseReference.child(finalPhoneNumber).removeValue();
//                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}
