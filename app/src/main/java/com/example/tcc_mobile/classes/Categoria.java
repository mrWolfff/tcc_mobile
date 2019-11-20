package com.example.tcc_mobile.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Categoria implements Parcelable{
    private int id;
    private String categoria;
    private String imagem;

    public Categoria(){

    }

    public int getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getImagem() {
        return imagem;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setImagem(String imagem) {
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
        dest.writeString(this.imagem);
    }

    public void readFromParcel(Parcel parcel){
        this.id = parcel.readInt();
        this.categoria = parcel.readString();
        this.imagem = parcel.readString();
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
