package com.a4bit.meilani.jamury4.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 3/9/18.
 */

public class BentukModel implements Parcelable {
    private int id;
    private String eks_bentuk;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEks_bentuk() {
        return eks_bentuk;
    }

    public void setEks_bentuk(String eks_bentuk) {
        this.eks_bentuk = eks_bentuk;
    }

    public BentukModel() {
    }

    public BentukModel(String eks_bentuk){
        this.eks_bentuk = eks_bentuk;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.eks_bentuk);
    }

    public BentukModel(Parcel in) {
        this.id = in.readInt();
        this.eks_bentuk = in.readString();
    }

    public static final Creator<BentukModel> CREATOR = new Creator<BentukModel>() {
        @Override
        public BentukModel createFromParcel(Parcel source) {
            return new BentukModel(source);
        }

        @Override
        public BentukModel[] newArray(int size) {
            return new BentukModel[size];
        }
    };

    public BentukModel(int id, String eks_bentuk) {
        this.id = id;
        this.eks_bentuk = eks_bentuk;
    }
}
