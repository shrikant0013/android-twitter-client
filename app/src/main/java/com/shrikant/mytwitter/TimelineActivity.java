package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.adapters.TweetsPagerAdapter;
import com.shrikant.mytwitter.fragments.HomeTweetsFragment;
import com.shrikant.mytwitter.tweetmodels.Tweet;
import com.shrikant.mytwitter.tweetmodels.User;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity
            implements ComposeFragment.OnTweetComposedListener {
    private final int REQUEST_CODE = 200;

    User me;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.sliding_tabs) TabLayout mTabLayout;
    //@Bind(R.id.pager_header) PagerTabStrip mPagerTabStrip;
    @Bind(R.id.viewpager) ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_birdy);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(vpPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                editTweet();
            }
        });
        storeMyInfo();
    }

    //Send an API request to get the timeline json
    // Fill the view by creating the tweet objects from the json

    //[] == JsonArray


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            //case R.id.miCompose:
                //composeTweet();
                //getMyInfo();
                //launchComposeDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    public void composeTweet() {

        //Toast.makeText(this, "Composed clicked", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("TimelineActivity", "Back to onactivity result");
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet = (Tweet) data.getExtras().get("tweet");
            int code = data.getExtras().getInt("code", 0);
            // Toast the name to display temporarily on screen
            Log.i("TimelineActivity", tweet.getText().toString());
            Toast.makeText(this, tweet.getText().toString(), Toast.LENGTH_SHORT).show();

//            mTweets.addFirst(tweet);
//            //int curSize = mComplexRecyclerViewArticleAdapter.getItemCount();
//            mComplexRecyclerViewArticleAdapter.notifyItemRangeInserted(0,
//                    1);
//            layoutManager.scrollToPosition(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TimelineActivity", "Saving to DB");
        //clearDB();
        //saveToDB();
    }

    public void launchComposeDialog(String userProfileUrl) {
        ComposeFragment composeFragment = ComposeFragment.newInstance(userProfileUrl);
        FragmentManager fm = getSupportFragmentManager();
        composeFragment.show(fm, "compose");
    }

    public void onProfileClick(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void onSearchClick(MenuItem item) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
    }

    public void showProfile(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        Log.i("TimelineActivity", "Clicked user profile is: " + (String)view.getTag());
        i.putExtra("screen_name", (String)view.getTag());
        startActivity(i);
    }

    void editTweet() {
        if (me != null && !TextUtils.isEmpty(me.getProfileImageUrl())) {
            launchComposeDialog(me.getProfileImageUrl());
        } else {
            TwitterClient mTwitterClient = TwitterApplication.getRestClient();
            mTwitterClient.getMyInfo(new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonUserObject = gson.fromJson(responseString, JsonObject.class);

                        if (jsonUserObject != null) {
                            me = User.fromJsonObjectToUser(jsonUserObject);
                            if (me != null && !TextUtils.isEmpty(me.getProfileImageUrl())) {
                                launchComposeDialog(me.getProfileImageUrl());
                            }
                        }
                    } catch (JsonParseException e) {
                        Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                            throwable.getMessage());
                }

            });
        }
    }

    void storeMyInfo() {
        TwitterClient mTwitterClient = TwitterApplication.getRestClient();
        mTwitterClient.getMyInfo(new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Gson gson = new GsonBuilder().create();
                    JsonObject jsonUserObject = gson.fromJson(responseString, JsonObject.class);

                    if (jsonUserObject != null) {
                        me = User.fromJsonObjectToUser(jsonUserObject);
                    }
                } catch (JsonParseException e) {
                    Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
            }

        });

    }

    @Override
    public void updateStatus(Tweet composeTweet) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        HomeTweetsFragment homeTweetsFragment = null;
        for (Fragment f : fragmentList) {
            if (f instanceof HomeTweetsFragment) {
                homeTweetsFragment = (HomeTweetsFragment)f;
                break;
            }
        }
        if (homeTweetsFragment != null) {
            homeTweetsFragment.mTweets.addFirst(composeTweet);
            //int curSize = mComplexRecyclerViewArticleAdapter.getItemCount();
            homeTweetsFragment.mComplexRecyclerViewHomeTweetsAdapter.notifyItemRangeInserted(0,
                    1);
            homeTweetsFragment.layoutManager.scrollToPosition(0);

        }
    }

    public void onMessageClick(MenuItem item) {
        Intent i = new Intent(this, MessagesActivity.class);
        startActivity(i);
    }

    public void replyToTweet(View view) {
        //Toast.makeText(this,  "Replying to tweet: " + (String) view.getTag(), Toast.LENGTH_SHORT).show();
        Log.i("TimelineActivity", "Clicked user profile is: " + (String) view.getTag());
        String arg =  (String) view.getTag();
        String[] args = arg.split("@");
        ComposeFragment composeFragment = null;

        String profileImageUrl ="";
        if (me != null && !TextUtils.isEmpty(me.getProfileImageUrl())) {
            profileImageUrl = me.getProfileImageUrl();
        }

        if (args.length > 1) {

            composeFragment = ComposeFragment.newInstance(true, args[0], "@" + args[1],
                    profileImageUrl);
        } else  {
            composeFragment = ComposeFragment.newInstance(false, "", "", profileImageUrl);
        }
        FragmentManager fm = getSupportFragmentManager();
        composeFragment.show(fm, "compose");
    }
}
