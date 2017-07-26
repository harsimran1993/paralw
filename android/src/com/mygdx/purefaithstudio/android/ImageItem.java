package com.mygdx.purefaithstudio.android;


/**
 * Created by harsimran singh on 01-07-2017.
 */

public class ImageItem {
    private int imageID,downloads;
    private String title,madeBy;

    public ImageItem(int imageID, String title,String madeBy,int downloads) {
        super();
        this.imageID = imageID;
        this.title = title;
        this.madeBy=madeBy;
        this.downloads=downloads;
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

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }
}
