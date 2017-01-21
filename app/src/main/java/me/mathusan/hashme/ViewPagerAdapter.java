package me.mathusan.hashme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles1[];
    int Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);
        this.Titles1 = mTitles;
        this.NumbOfTabs = mNumbOfTabs;
    }

    @Override
    public Fragment getItem(int arg0) {

        if(arg0 == 0){
            RecentFragment tab1 = new RecentFragment();
            return tab1;
        }else if(arg0 == 1){
            UploadFragment tab2 = new UploadFragment();
            return tab2;
        }else{
            AccountFragment tab3 = new AccountFragment();
            return tab3;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return Titles1[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}