package com.example.tcc_mobile.classes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Message_Session implements Parcelable {
    private int id;
    private int from_user;
    private int to_user;
    private Bitmap imagem;
    private String from_user_string;
    private String to_user_string;
    public Message_Session(){

    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    public String getFrom_user_string() {
        return from_user_string;
    }

    public void setFrom_user_string(String from_user_string) {
        this.from_user_string = from_user_string;
    }

    public String getTo_user_string() {
        return to_user_string;
    }

    public void setTo_user_string(String to_user_string) {
        this.to_user_string = to_user_string;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_user() {
        return from_user;
    }

    public void setFrom_user(int from_user) {
        this.from_user = from_user;
    }

    public int getTo_user() {
        return to_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public Message_Session(Parcel in){
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.from_user);
        dest.writeInt(this.to_user);
    }

    public void readFromParcel(Parcel parcel){
        this.id = parcel.readInt();
        this.from_user = parcel.readInt();
        this.to_user = parcel.readInt();
    }

    public static final Parcelable.Creator<Message_Session> CREATOR = new Parcelable.Creator<Message_Session>(){
        @Override
        public Message_Session createFromParcel (Parcel p){
            return new Message_Session(p);
        }
        @Override
        public Message_Session[] newArray(int size){
            return new Message_Session[size];
        }
    };
}
