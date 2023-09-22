package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.siddydevelops.customlottiedialogbox.CustomLottieDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {

    private String mobile;
    private Bitmap store;
    private Bitmap bitmap;
    private SharedPreferences sharedPreferences;
    private EditText Name;
    private TextView save;
    private EditText Email;
    private CircleImageView profilePic;
    private Uri filepath;
    private String currUserName;
    private RelativeLayout rl;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");;
    TextView desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Name=findViewById(R.id.Name);
        Email=findViewById(R.id.Email);
        mobile=MemoryData.getData(getApplicationContext());
        profilePic=findViewById(R.id.profilePic);
        desc=findViewById(R.id.textView1);
        save=findViewById(R.id.save);
        ImageView editName=findViewById(R.id.EditName);
        ImageView editEmail=findViewById(R.id.EditEmail);
        ImageView back=findViewById(R.id.back);
        TextView phone=findViewById(R.id.Phone);
        phone.setText("+91 "+mobile);
        rl=findViewById(R.id.rl1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Name.setEnabled(false);
        Email.setEnabled(false);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
        String email=sharedPreferences.getString("Email","");
        if(!email.isEmpty()) Email.setText(email);
        loadUserInfo(sharedPreferences);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Name.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
                Name.setEnabled(true);
                Name.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(Name, InputMethodManager.SHOW_IMPLICIT);

            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Email.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
                Email.setEnabled(true);
                Email.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(Email, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType("image/*");
                        startActivityForResult(Intent.createChooser(i, "Please select Image"), 1);
//                    openGallery();
                    } else {
//                        Toast.makeText(getContext(),"13asdad",Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(AccountSettings.this,
                                new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                                123);
                    }
                }else{

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType("image/*");
                        startActivityForResult(Intent.createChooser(i, "Please select Image"), 1);
//                    openGallery();
                    } else {
                        ActivityCompat.requestPermissions(AccountSettings.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                123);
                    }

                }
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
                final String[] newUserName = {Name.getText().toString()};
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                if(!newUserName[0].isEmpty()){
                    if(!currUserName.equals(newUserName[0])){
                        final int[] check = {0};
                        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    if(Objects.requireNonNull(dataSnapshot.child("Name").getValue(String.class)).equals(newUserName[0])){
                                        Toast.makeText(getApplicationContext(),"User already Exists",Toast.LENGTH_SHORT).show();
                                        check[0] =1;
                                        break;
                                    }
                                }
                                if(check[0]==0) databaseReference.child("Users").child(mobile).child("Name").setValue(newUserName[0]);

                                myEdit.putString("name", newUserName[0]);
                                myEdit.apply();
                                Name.setText(newUserName[0]);
                                Name.setFocusable(false);
                                MainActivity.myName= newUserName[0];
                                Container.setMyName(newUserName[0]);
                                newUserName[0] ="";
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                String temp=Email.getText().toString();
                if(!email.equals(temp)){
                    if(!temp.isEmpty()) myEdit.putString("Email",temp);
                    myEdit.apply();
                    Email.setFocusable(false);
                    temp="";
                }
            }
        });
    }

    private void loadUserInfo(SharedPreferences sharedPreferences) {
//        String filePath = sharedPreferences.getString("profile_picture_path", null);
        Bitmap bitmap =MemoryData.getProfilePicture(mobile,getApplicationContext());
        if(bitmap!=null) {
            profilePic.setImageBitmap(bitmap);
            store=bitmap;
            Container.setMyImage(bitmap);
        }
//        if (filePath != null) {
//            Bitmap bitmap1 = BitmapFactory.decodeFile(filePath);
//            store=bitmap1;
//            if(bitmap1!=null) profilePic.setImageBitmap(bitmap1);
//            Log.i("pathImage",filePath);
////            currUserName=sharedPreferences.getString("name","User@1a*e");
////            Name.setText(currUserName);
//        }
        currUserName=sharedPreferences.getString("name","User@1a*e");
        Name.setText(currUserName);
    }

    public void showImage() {
        // Create a Dialog instance
        Dialog dialog = new Dialog(AccountSettings.this);

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.profile_image_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageView myImage=dialog.findViewById(R.id.MyImage);
        if(store!=null) myImage.setImageBitmap(store);
        // Show the Dialog
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data!=null && data.getData()!=null && requestCode==1 && resultCode==RESULT_OK){
            filepath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                MemoryData.saveProfilePicture(bitmap,mobile,getApplicationContext());
                Container.setMyImage(bitmap);
//                String fileName = "profile_picture.jpg";
//                File file = new File(getApplicationContext().getFilesDir(), fileName);
//                FileOutputStream fos = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                fos.close();
                profilePic.setImageBitmap(bitmap);
//                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("profile_picture_path", getApplicationContext().getFilesDir() + "/" + fileName);
//                editor.apply();
                uploadtofirebase();
//                Mydp=findViewById(R.id.Mydp);
//                Mydp.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
//                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void uploadtofirebase(){
//        CustomLottieDialog customLottieDialog = new CustomLottieDialog(AccountSettings.this, R.raw.animation_loading);
//        customLottieDialog.setLottieBackgroundColor("#000000");
//        customLottieDialog.setDialogLayoutDimensions(100, 100);
//        customLottieDialog.setLoadingText("");
//        customLottieDialog.show();
        LoadingAnimation obj=new LoadingAnimation(AccountSettings.this);
        obj.Load();
        obj.dialog.show();
//        ProgressDialog dialog=new ProgressDialog(this);
//        dialog.setTitle("File Uploader");
//        dialog.show();

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference uploader=storage.getReference().child("ProfilePictures").child(mobile);
        if(filepath!=null){
            uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                if (!Login.this.isFinishing() && dialog != null) {
//                dialog.dismiss();
//                }
                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl=uri.toString();
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    databaseReference.child(mobile).child("profilePic").setValue(imageUrl);
//                                    Toast.makeText(getApplicationContext(),"Updating Profile picture.",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    Toast.makeText(getApplicationContext(),"Image uploaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    obj.dismiss();
                }
            });
        }
    }

}