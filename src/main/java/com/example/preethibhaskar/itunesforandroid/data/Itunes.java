package com.example.preethibhaskar.itunesforandroid.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Preethi Bhaskar on 10/28/15.
 * This class has setters and getters for itunes and implements Parcelable so that we can send
 * itunes data through intent .
 */
public class Itunes implements Parcelable {

    private String mTitle;
    private String mImageUrl;
    private String mDescription;
    private String mCollectionName;
    private String mCollectionPrice;
    private String mTrackPrice;



    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getCollectionName() {
        return mCollectionName;
    }

    public void setCollectionName(String collectionName) {
        this.mCollectionName = collectionName;
    }

    public String getCollectionPrice() {
        return mCollectionPrice;
    }

    public void setCollectionPrice(String collectionPrice) {
        this.mCollectionPrice = collectionPrice;
    }

    public String getTrackPrice() {
        return mTrackPrice;
    }

    public void setTrackPrice(String trackPrice) {
        this.mTrackPrice = trackPrice;
    }

    public static final Parcelable.Creator<Itunes> CREATOR = new Creator<Itunes>() {
        public Itunes createFromParcel(Parcel source) {
            Itunes list = new Itunes();
            list.mTitle = source.readString();
            list.mDescription = source.readString();
            list.mImageUrl = source.readString();
            list.mCollectionName = source.readString();
            list.mCollectionPrice = source.readString();
            return list;
        }

        public Itunes[] newArray(int size) {
            return new Itunes[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mImageUrl);
        parcel.writeString(mCollectionName);
        parcel.writeString(mCollectionPrice);

    }

    @Override
    public String toString() {
        return mTitle + " - " + mDescription ;
    }

}
