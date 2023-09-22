package com.example.gossip;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentAdapter extends FragmentStateAdapter {

//    public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
//        super(fragmentActivity);
//    }
    public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

//    public MyFragmentAdapter(FragmentManager supportFragmentManager, Lifecycle lifecycle) {
//        super();
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return your fragment instances here based on the position
        // Example: return MyFragment.newInstance();
        if(position==1){
            BlankFragment blankFragment=new BlankFragment();
            return new RandomChat();
        }
        else if(position==0) return new BlankFragment();
        else if(position==2) return new NotificationFragment();
        return new ProfileFragment();
    }

    @Override
    public int getItemCount() {
        // Return the total number of fragments
        return 4;
    }
}
