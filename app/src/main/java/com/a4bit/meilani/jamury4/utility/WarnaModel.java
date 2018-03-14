package com.a4bit.meilani.jamury4.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 3/9/18.
 */

public class WarnaModel implements Parcelable {
    private int id;
    private double eks_warna;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getEks_warna() {
        return eks_warna;
    }

    public void setEks_warna(double eks_warna) {
        this.eks_warna = eks_warna;
    }

    public WarnaModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.eks_warna);
    }

    protected WarnaModel(Parcel in) {
        this.id = in.readInt();
        this.eks_warna = in.readDouble();
    }

    public static final Parcelable.Creator<WarnaModel> CREATOR = new Creator<WarnaModel>() {
        @Override
        public WarnaModel createFromParcel(Parcel source) {
            return new WarnaModel(source);
        }

        @Override
        public WarnaModel[] newArray(int size) {
            return new WarnaModel[size];
        }
    };

    public WarnaModel(int id, Double eks_warna) {
        this.id = id;
        this.eks_warna = eks_warna;
    }
}
