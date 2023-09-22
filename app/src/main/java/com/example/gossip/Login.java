package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
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
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    CheckBox checkBox;
    private FirebaseAuth mAuth;
    private String AuthenticationId;
    private ImageView take_image;
    private EditText edtOTP;
    private ImageView verifyOTPBtn;
    private TextView generateOTPBtn;
    private EditText Name;
    private EditText edtPhone;
    private TextView termsAndConditions;
    private String s;
    private String n;
    Uri filepath;
    Bitmap bitmap;
    ImageView Mydp;
    private String profilePicLink;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(data.getData()!=null && requestCode==1 && resultCode==RESULT_OK){
//            filepath=data.getData();
//            try{
//                InputStream inputStream=getContentResolver().openInputStream(filepath);
//                bitmap= BitmapFactory.decodeStream(inputStream);
//                String fileName = "profile_picture.jpg";
//                take_image.setImageBitmap(bitmap);
//
//                SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString("profile_picture_path", getApplicationContext().getFilesDir() + "/" + fileName);
//                editor.apply();
////                Mydp=findViewById(R.id.Mydp);
////                Mydp.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
////                e.printStackTrace();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
//        edtOTP = findViewById(R.id.idEdtOtp);
//        Name=findViewById(R.id.name);
//        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);
//        verifyOTPBtn.setVisibility(View.INVISIBLE);
//        edtOTP.setVisibility(View.INVISIBLE);
        mAuth=FirebaseAuth.getInstance();
        checkBox=findViewById(R.id.checkBox);
        termsAndConditions=findViewById(R.id.TermsAndConditions);
        boolean areNotificationsEnabled = areNotificationsEnabled(Login.this);
        if (!areNotificationsEnabled) openNotificationSettings();
            // Notifications are enabled
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ContactChats.class);
                startActivity(i);
            }
        });
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +getPackageName()));
//        getApplicationContext().startActivity(intent);
//        take_image=findViewById(R.id.profile_image);




        if(!MemoryData.getData(this).isEmpty()){
//            Toast.makeText(getApplicationContext(),MemoryData.getData(this).toString(),Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login.this, ContactChats.class);
            Bundle bundle =new Bundle();
            bundle.putString("mobile",MemoryData.getData(this));
            bundle.putString("name",MemoryData.getData(this));
//            i.putExtra("mobile",MemoryData.getData(this));
//            i.putExtra("name",MemoryData.getName(this));
            BlankFragment frag=new BlankFragment();
            frag.setArguments(bundle);
//            Intent i=new Intent(getApplicationContext(),BottomNavigationPage.class);
//            Fragmentclass frag=new Fragmentclass();
            startActivity(i);
            finish();
        }

//        SharedPreferences sh = getSharedPreferences("MyAuthenticationId", MODE_PRIVATE);
//        String s1 = sh.getString("AuthId", "");
//        if(!s1.isEmpty()){
//            Intent i=new Intent(getApplicationContext(),HomeActivity.class);
//            startActivity(i);
//            finish();
//        }
        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
                    } else {
                        String phone = "+91" + edtPhone.getText().toString();
//                        sendVerificationCode(phone);
                        Intent i = new Intent(getApplicationContext(), OOtpVerification.class);
                        i.putExtra("Mobile", edtPhone.getText().toString());
                        startActivity(i);
                    }
                }else Toast.makeText(getApplicationContext(),"Select CheckBox",Toast.LENGTH_SHORT).show();




            }
        });
//        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                uploadtofirebase();
//                // validating if the OTP text field is empty or not.
//                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
//                    // if the OTP text field is empty display
//                    // a message to user to enter OTP
//                    Toast.makeText(Login.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
//                } else {
////                    Log.i("justCheck1","yess");
////                    Toast.makeText(getApplicationContext(),"1111111",Toast.LENGTH_SHORT).show();
//                    // if OTP field is not empty calling
//                    // method to verify the OTP.
//                    verifyCode(edtOTP.getText().toString());
//                }
//            }
//        });
    }



    // End of On Create Method ...............................................................................................................................................>




    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
//        verifyOTPBtn.setVisibility(View.VISIBLE);
//        edtOTP.setVisibility(View.VISIBLE);
//        generateOTPBtn.setVisibility(View.INVISIBLE);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            AuthenticationId=s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                edtOTP.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    public void verifyCode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(AuthenticationId,code);
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.



                            //shared preference to store authentication id;

                            SharedPreferences sharedPreferences = getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("AuthId", AuthenticationId);
                            myEdit.putString("name","User@"+AuthenticationId.substring(12,14));
                            myEdit.apply();


                            //save mobile to memory

                            MemoryData.saveData(edtPhone.getText().toString(),Login.this);


                            //saving name to Memory
//                            Toast.makeText(getApplicationContext(),Name.getText().toString(),Toast.LENGTH_SHORT).show();
                            MemoryData.saveName(Name.getText().toString(),Login.this);
                            Intent i = new Intent(Login.this, ContactChats.class);
                            i.putExtra("mobile",s);
//                            i.putExtra("name",);
                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private boolean areNotificationsEnabled(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if notifications are enabled for the app
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return notificationManager.areNotificationsEnabled();
            }
        }
        return false;
    }
    private void openNotificationSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getApplicationContext().getPackageName());

        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}