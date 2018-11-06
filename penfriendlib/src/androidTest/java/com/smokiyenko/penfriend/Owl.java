package com.smokiyenko.penfriend;

import android.os.Parcel;
import android.os.Parcelable;

public class Owl implements Parcelable {

    private String name;
    private int age;

    public Owl(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
    }

    public Owl(String name, int age){
        this.name = name;
        this.age = age;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    public static final Creator CREATOR = new Creator() {
        public Owl createFromParcel(Parcel in) {
            return new Owl(in);
        }

        public Owl[] newArray(int size) {
            return new Owl[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
