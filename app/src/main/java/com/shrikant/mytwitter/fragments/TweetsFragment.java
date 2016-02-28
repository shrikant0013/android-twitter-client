package com.shrikant.mytwitter.fragments;

import com.shrikant.mytwitter.TwitterApplication;
import com.shrikant.mytwitter.TwitterClient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.IOException;

/**
 * Created by spandhare on 2/22/16.
 */
public abstract class TweetsFragment extends Fragment {

    TwitterClient mTwitterClient;
     abstract void populateTimeLine(boolean b, boolean b1);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwitterClient = TwitterApplication.getRestClient(); //singleton client

    }

//    private Boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
//    }

    protected boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }
//    public void readFromDB() {
//        List<User> existingUsers = new Select().from(User.class).execute();
//        List<Tweet> existingTweets = new Select().from(Tweet.class).execute();
//
//        Map<Long, User> userMap = new HashMap<>();
//
//        if (existingUsers != null) {
//            for (User user : existingUsers) {
//                userMap.put(user.getUser_id(), user);
//            }
//
//        }
//
//        if (existingTweets != null) {
//            for (Tweet tweet : existingTweets) {
//                if (userMap.containsKey(tweet.getUser_id())) {
//                    tweet.setUser(userMap.get(tweet.getUser_id()));
//                }
//                mMentionTweets.add(tweet);
//            }
//            mComplexRecyclerViewArticleAdapter.notifyDataSetChanged();
//        }
//    }

//    public void updateStatus(Tweet tweet) {
//        // Extract name value from result extras
//
//        mMentionTweets.addFirst(tweet);
//        //int curSize = mComplexRecyclerViewArticleAdapter.getItemCount();
//        mComplexRecyclerViewArticleAdapter.notifyItemRangeInserted(0,
//                1);
//        layoutManager.scrollToPosition(0);
//
//    }

//    public void saveToDB() {
//        List<User> existingUsers = new Select().from(User.class).execute();
//        Set<String> userIds = new HashSet<>();
//
//        for (User user: existingUsers) {
//            userIds.add(user.getIdStr());
//        }
//
//        for (Tweet tweet : mMentionTweets) {
//            if (!userIds.contains(tweet.getUser().getIdStr())) {
//                tweet.getUser().save();
//                userIds.add(tweet.getUser().getIdStr());
//            }
//            tweet.save();
//        }
//    }

//    public void clearDB() {
//        new Delete().from(User.class).execute();
//        new Delete().from(Tweet.class).execute();
//    }
}
