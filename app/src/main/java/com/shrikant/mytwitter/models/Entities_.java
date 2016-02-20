
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class Entities_ implements Parcelable {

    @SerializedName("hashtags")
    @Expose
    private List<Hashtag> hashtags = new ArrayList<Hashtag>();
    @SerializedName("symbols")
    @Expose
    private List<Object> symbols = new ArrayList<Object>();
    @SerializedName("user_mentions")
    @Expose
    private List<Object> userMentions = new ArrayList<Object>();
    @SerializedName("urls")
    @Expose
    private List<Url__> urls = new ArrayList<Url__>();
    @SerializedName("media")
    @Expose
    private List<Medium> media = new ArrayList<Medium>();

    /**
     * 
     * @return
     *     The hashtags
     */
    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    /**
     * 
     * @param hashtags
     *     The hashtags
     */
    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    /**
     * 
     * @return
     *     The symbols
     */
    public List<Object> getSymbols() {
        return symbols;
    }

    /**
     * 
     * @param symbols
     *     The symbols
     */
    public void setSymbols(List<Object> symbols) {
        this.symbols = symbols;
    }

    /**
     * 
     * @return
     *     The userMentions
     */
    public List<Object> getUserMentions() {
        return userMentions;
    }

    /**
     * 
     * @param userMentions
     *     The user_mentions
     */
    public void setUserMentions(List<Object> userMentions) {
        this.userMentions = userMentions;
    }

    /**
     * 
     * @return
     *     The urls
     */
    public List<Url__> getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(List<Url__> urls) {
        this.urls = urls;
    }

    /**
     * 
     * @return
     *     The media
     */
    public List<Medium> getMedia() {
        return media;
    }

    /**
     * 
     * @param media
     *     The media
     */
    public void setMedia(List<Medium> media) {
        this.media = media;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.hashtags);
        dest.writeList(this.symbols);
        dest.writeList(this.userMentions);
        dest.writeList(this.urls);
        dest.writeList(this.media);
    }

    public Entities_() {
    }

    protected Entities_(Parcel in) {
        this.hashtags = new ArrayList<Hashtag>();
        in.readList(this.hashtags, List.class.getClassLoader());
        this.symbols = new ArrayList<Object>();
        in.readList(this.symbols, List.class.getClassLoader());
        this.userMentions = new ArrayList<Object>();
        in.readList(this.userMentions, List.class.getClassLoader());
        this.urls = new ArrayList<Url__>();
        in.readList(this.urls, List.class.getClassLoader());
        this.media = new ArrayList<Medium>();
        in.readList(this.media, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<Entities_> CREATOR = new Parcelable.Creator<Entities_>() {
        public Entities_ createFromParcel(Parcel source) {
            return new Entities_(source);
        }

        public Entities_[] newArray(int size) {
            return new Entities_[size];
        }
    };
}
