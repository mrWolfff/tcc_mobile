package com.example.tcc_mobile.classes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Categoria implements Parcelable{
    private int id;
    private String categoria;
    private Bitmap imagem;

    public Categoria(){

    }

    public int getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Categoria(Parcel in){
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.categoria);
        //dest.writeValue(this.imagem);
    }

    public void readFromParcel(Parcel parcel){
        this.id = parcel.readInt();
        this.categoria = parcel.readString();
        //this.imagem = (Bitmap) parcel.readValue(Bitmap.class.getClassLoader());
    }

    public static final Parcelable.Creator<Categoria> CREATOR = new Parcelable.Creator<Categoria>(){
        @Override
        public Categoria createFromParcel (Parcel p){
            return new Categoria(p);
        }
        @Override
        public Categoria[] newArray(int size){
            return new Categoria[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }
}
