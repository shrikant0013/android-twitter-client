package com.shrikant.mytwitter.tweetmodels;

import com.google.gson.JsonObject;

import android.util.Log;

/**
 * Created by spandhare on 2/21/16.
 */
public class Me {

    public static final String NAME = "name";
    public static final String SCREEN_NAME = "screen_name";
    public static final String PROFILE_IMAGE_URL = "profile_image_url";

    private String myTwitterHandle;
    private String myName;
    private String myProfileImageUrl;

    public String getMyTwitterHandle() {
        return myTwitterHandle;
    }

    public void setMyTwitterHandle(String myTwitterHandle) {
        this.myTwitterHandle = myTwitterHandle;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyProfileImageUrl() {
        return myProfileImageUrl;
    }

    public void setMyProfileImageUrl(String myProfileImageUrl) {
        this.myProfileImageUrl = myProfileImageUrl;
    }

    public static Me fromJsonObjectToMe(JsonObject jsonMeObject) {
        Me me = new Me();
        if (jsonMeObject != null) {
            if (jsonMeObject.has(NAME)) {
                me.setMyName(jsonMeObject.get(NAME).getAsString());
            }
            if (jsonMeObject.has(SCREEN_NAME)) {
                me.setMyTwitterHandle(jsonMeObject.get(SCREEN_NAME).getAsString());
            }
            if (jsonMeObject.has(PROFILE_IMAGE_URL)) {
                me.setMyProfileImageUrl(jsonMeObject.get(PROFILE_IMAGE_URL).getAsString());
                Log.i("Compose fragmet", me.getMyProfileImageUrl());
            }
        }

        return me;
    }
}
