package com.shrikant.mytwitter.adapters;

import com.shrikant.mytwitter.fragments.HomeTweetsFragment;
import com.shrikant.mytwitter.fragments.MentionsTweetsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by spandhare on 2/23/16.
 */
public class TweetsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> tabsTitles = new ArrayList<String>(){{
        add("Home");
        add("Mentions");
    }};

    public TweetsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new HomeTweetsFragment();
        } else if (position == 1) {
            return new MentionsTweetsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabsTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitles.get(position);
    }
}
