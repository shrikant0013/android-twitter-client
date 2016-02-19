
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ExtendedEntities {

    @SerializedName("media")
    @Expose
    private List<Medium__> media = new ArrayList<Medium__>();

    /**
     * 
     * @return
     *     The media
     */
    public List<Medium__> getMedia() {
        return media;
    }

    /**
     * 
     * @param media
     *     The media
     */
    public void setMedia(List<Medium__> media) {
        this.media = media;
    }

}
