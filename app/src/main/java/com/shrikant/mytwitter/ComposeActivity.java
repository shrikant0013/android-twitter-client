package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.models.Tweet;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient mTwitterClient;

    @Bind(R.id.btnTweetSent) Button mButtonTweetSend;
    @Bind(R.id.tvCharacterCount) TextView  mTextViewCharCount;
    @Bind(R.id.etComposeBody) EditText mEditTextComposeBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mEditTextComposeBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextViewCharCount.setText("" + s.length());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTwitterClient = TwitterApplication.getRestClient();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    @OnClick(R.id.btnTweetSent)
    public void sendTweet(View view) {
        String tweetText = mEditTextComposeBody.getText().toString();

        mTwitterClient.sendTweet(tweetText, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet composeTweet = new Tweet();
                if (responseString != null) {
                    Log.i("ComposeActivity", responseString);
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                        if (jsonObject != null) {
                            Type collectionType = new TypeToken<Tweet>() {
                            }.getType();
                            composeTweet = gson.fromJson(jsonObject, collectionType);
                            Log.i("ComposeActivity", composeTweet.getText());
                        }
                    } catch (JsonParseException e) {
                        Log.d("Compose tweet onSuccess", "Json parsing error:" + e.getMessage(), e);
                    }
                }

                // Prepare data intent
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("tweet", composeTweet);
                data.putExtra("code", 200); // ints work too
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                Log.w("ComposeActivity", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
        }
    });
//        Toast.makeText(getApplicationContext(), "Sending tweet.." + tweetText,
//                Toast.LENGTH_SHORT).show();
    }
}
