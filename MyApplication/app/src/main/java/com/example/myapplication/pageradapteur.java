package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class pageradapteur extends FragmentPagerAdapter {
int tabcount;
    public pageradapteur(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
     switch (position){
          case 0: return new chat();
           case 1: return new status();
           case 2: return new call();
            default:return null;


        }
//return null;
    }

    @Override
    public int getCount() {
   return tabcount;
    }
}
