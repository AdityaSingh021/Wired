package com.example.gossip;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://goss-p-dc95b-default-rtdb.firebaseio.com/");


    private String currUserName;
    private TextView Name;
    private RelativeLayout LogOut;
    SharedPreferences sharedPreferences;
    RelativeLayout deleteAccount;
    CircleImageView profilePic;
    RelativeLayout AccountSettings;
    RelativeLayout privacyPolicy;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        AccountSettings=view.findViewById(R.id.AccountSetting);
        privacyPolicy=view.findViewById(R.id.PrivacyPolicyLayout);
        profilePic=view.findViewById(R.id.profile_image);
        Name=view.findViewById(R.id.Name);
        LogOut=view.findViewById(R.id.LogOutLayout);
        deleteAccount=view.findViewById(R.id.DeleteAccountLayout);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccountDialog();
            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog();
            }
        });
        sharedPreferences = getContext().getSharedPreferences("MyAuthenticationId",MODE_PRIVATE);


        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),PrivacyPolicy.class);
                startActivity(i);
            }
        });
        AccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), com.example.gossip.AccountSettings.class);
                startActivity(i);
            }
        });
        Switch switchButton = view.findViewById(R.id.switchButton);
        SharedPreferences sharedPreferences1= getContext().getSharedPreferences("Notifications",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences1.edit();
        boolean areNotificationsEnabled = areNotificationsEnabled(getContext());
        boolean Status=sharedPreferences1.getBoolean("Status",areNotificationsEnabled);
        if(!areNotificationsEnabled) switchButton.setChecked(false);
        else switchButton.setChecked(Status);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean areNotificationsEnabled = areNotificationsEnabled(getContext());
                    if (areNotificationsEnabled) {
                        // Notifications are enabled
                    } else {
                        // Notifications are not enabled
                        openNotificationSettings();
                    }
                    // Switch is in the ON state
                    if(areNotificationsEnabled) {
                        edit.putBoolean("Status", true);
                        Toast.makeText(getContext(), "You will be Notified", Toast.LENGTH_SHORT).show();
                        databaseReference.child(BlankFragment.mobile).child("Notif").setValue("1");
                    }else{
                        switchButton.setChecked(false);
                    }


                } else {
                    // Switch is in the OFF state
                    edit.putBoolean("Status",false);
                    databaseReference.child(BlankFragment.mobile).child("Notif").setValue("0");
                    Toast.makeText(getContext(), "You will not get any Notifications", Toast.LENGTH_SHORT).show();
                }
//                edit.apply();
            }
        });
        return view;
    }

    public void AlertDialog() {
        // Create a Dialog instance
        Dialog dialog = new Dialog(getContext());

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.logout_alert_window);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.CENTER);
        TextView logOut=dialog.findViewById(R.id.logOut);
        TextView cancel=dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.getInstance().signOut();

                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.clear();
                edit.apply();
                String fileName = "datata.txt";
                File file = new File(getContext().getFilesDir(), fileName);

                if (file.exists()) {
                    boolean deleted = file.delete();
                } else {
                    // File does not exist
                }
                Intent i=new Intent(getContext(),Login.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        // Show the Dialog
        dialog.show();
    }

    public void DeleteAccountDialog() {
        // Create a Dialog instance
        Dialog dialog = new Dialog(getContext());

        // Set the layout for the Dialog
        dialog.setContentView(R.layout.activity_delete_account);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Optionally set other properties for the Dialog, e.g., size, title, etc.
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setTitle("Your Dialog Title");
        dialog.getWindow().setGravity(Gravity.CENTER);
        TextView delete=dialog.findViewById(R.id.Delete);
        TextView cancel=dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Users").child(MainActivity.mobile).removeValue();
                databaseReference.child(MainActivity.mobile).removeValue();
                databaseReference.child("RandomChat").child("Status").child(MainActivity.myName).removeValue();
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();
                StorageReference imgRef = storageReference.child("ProfilePictures").child(MainActivity.mobile);
                imgRef.delete();
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.getInstance().signOut();

                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.clear();
                edit.apply();
                String fileName = "datata.txt";
                File file = new File(getContext().getFilesDir(), fileName);

                if (file.exists()) {
                    boolean deleted = file.delete();
                } else {
                    // File does not exist
                }
                Intent i=new Intent(getContext(),Login.class);
                startActivity(i);
                getActivity().finish();
            }
        });
//        logOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//            }
//        });
        // Show the Dialog
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Bitmap bitmap;
        if(Container.getMyImage()!=null) bitmap=Container.getMyImage();
        else bitmap =MemoryData.getProfilePicture(MainActivity.mobile,getContext());
        if(bitmap!=null) {
            profilePic.setImageBitmap(bitmap);
        }
        currUserName=Container.myName;
        Name.setText(currUserName);
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
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}