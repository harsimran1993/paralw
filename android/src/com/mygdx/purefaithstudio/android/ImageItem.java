package com.mygdx.purefaithstudio.android;


/**
 * Created by harsimran singh on 01-07-2017.
 */

public class ImageItem {
    private int imageID;
    private String title;

    public ImageItem(int imageID, String title) {
        super();
        this.imageID = imageID;
        this.title = title;
    }

    public int getImage() {
        return imageID;
    }

    public void setImage(int imageID) {
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
