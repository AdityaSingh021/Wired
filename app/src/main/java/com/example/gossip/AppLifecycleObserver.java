package com.example.gossip;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class AppLifecycleObserver implements Application.ActivityLifecycleCallbacks {

    DatabaseReference databaseReference=MainActivity.databaseReference;
    private int activityReferences = 0;
    private boolean isAppInForeground = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (++activityReferences == 1) {
            // App enters foreground
            isAppInForeground = true;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        databaseReference.child("Users").child(Container.getMobile()).child("State").setValue(1);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (--activityReferences == 0) {
//            Toast.makeText(activity.getApplicationContext(), "background",Toast.LENGTH_SHORT).show();
            // App enters background
//            databaseReference.child("Users").child(Container.getMobile()).child("State").setValue(0);
            isAppInForeground = false;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    public boolean isAppInForeground() {
        return isAppInForeground;
    }
}
