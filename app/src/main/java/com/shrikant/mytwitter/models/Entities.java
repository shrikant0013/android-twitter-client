
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;


public class Entities implements Parcelable {

    @SerializedName("url")
    @Expose
    private Url url;
    @SerializedName("description")
    @Expose
    private Description description;

    /**
     * 
     * @return
     *     The url
     */
    public Url getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(Url url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The description
     */
    public Description getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.url, flags);
        dest.writeParcelable(this.description, flags);
    }

    public Entities() {
    }

    protected Entities(Parcel in) {
        this.url = in.readParcelable(Url.class.getClassLoader());
        this.description = in.readParcelable(Description.class.getClassLoader());
    }

    public static final Parcelable.Creator<Entities> CREATOR = new Parcelable.Creator<Entities>() {
        public Entities createFromParcel(Parcel source) {
            return new Entities(source);
        }

        public Entities[] newArray(int size) {
            return new Entities[size];
        }
    };
}
