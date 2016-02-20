package com.shrikant.mytwitter;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY =
			"d6pvxCCA04mHEGMZMGq4wi8WU";       // Change this
	public static final String REST_CONSUMER_SECRET =
			"Bg7ZJ2kbBSbg6ekcZRTUdS9ksEAIyjSdg0PEdr1XnCLo81ieir"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://chika.space"; // Change this (here and in manifest)
	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	//METHOD=ENDPOINT
    //HomeTimeLine - Get us the home timeline data
	// GET statuses/home_timeline.json
    //  count=25
    //  since_id = 1
    public void getHomeTimeline(long maxId,  long sinceId, boolean isScrolled, boolean isRefreshed,
                                AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();

        if (isScrolled) {
            params.put("max_id", maxId);
            params.put("count", 25);
        } else if(isRefreshed) {
            params.put("since_id", sinceId);
        } else {
            params.put("count", 25);
            params.put("since_id", 1); //get latest tweets
        }
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void sendTweet(String text, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("status", text);

        //Execute the request
        getClient().post(apiUrl, params, handler);
    }


    //COMPOSE TWEET

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}