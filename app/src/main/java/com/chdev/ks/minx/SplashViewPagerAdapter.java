package com.chdev.ks.minx;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.chdev.ks.minx.SplashFragments.SplashIntroFragment;
import com.chdev.ks.minx.SplashFragments.SplashPinFragment;
import com.chdev.ks.minx.SplashFragments.SplashSettingsFragment;
import com.chdev.ks.minx.SplashFragments.SplashTutorialFragment;

/**
 * Created by Kartik_ch on 9/1/2015.
 */
public class SplashViewPagerAdapter extends FragmentPagerAdapter {

    public SplashViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new SplashIntroFragment();
            case 1:return new SplashTutorialFragment();
            case 2:return new SplashSettingsFragment();
            case 3:return new SplashPinFragment();
            default:return new SplashIntroFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
