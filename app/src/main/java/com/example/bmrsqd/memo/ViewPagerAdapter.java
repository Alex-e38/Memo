package com.example.bmrsqd.memo;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter{

    String[] tabtitlearray = {"Memos", "Sprachaufnahmen"};

    public ViewPagerAdapter (FragmentManager manager){

        super(manager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0: return new Fragment1();
            case 1: return new Fragment2();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabtitlearray[position];
    }
}
