
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Url implements Parcelable {

    @SerializedName("urls")
    @Expose
    private List<Url_> urls = new ArrayList<Url_>();

    /**
     * 
     * @return
     *     The urls
     */
    public List<Url_> getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(List<Url_> urls) {
        this.urls = urls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.urls);
    }

    public Url() {
    }

    protected Url(Parcel in) {
        this.urls = new ArrayList<Url_>();
        in.readList(this.urls, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<Url> CREATOR = new Parcelable.Creator<Url>() {
        public Url createFromParcel(Parcel source) {
            return new Url(source);
        }

        public Url[] newArray(int size) {
            return new Url[size];
        }
    };
}
