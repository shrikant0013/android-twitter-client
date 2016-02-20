
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Description implements Parcelable {

    @SerializedName("urls")
    @Expose
    private List<Object> urls = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The urls
     */
    public List<Object> getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(List<Object> urls) {
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

    public Description() {
    }

    protected Description(Parcel in) {
        this.urls = new ArrayList<Object>();
        in.readList(this.urls, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<Description> CREATOR = new Parcelable.Creator<Description>() {
        public Description createFromParcel(Parcel source) {
            return new Description(source);
        }

        public Description[] newArray(int size) {
            return new Description[size];
        }
    };
}
