package com.example.tcc_mobile.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Demandas implements Parcelable {
    private int id;
    private String imagem;
    private String titulo;
    private String descricao;
    private String data;
    private int user_demanda;
    private int categoria;

    private String categoria_string;
    private String user_demanda_string;

    public String getUser_demanda_string() {
        return user_demanda_string;
    }

    public void setUser_demanda_string(String user_demanda_string) {
        this.user_demanda_string = user_demanda_string;
    }

    public String getCategoria_string() {
        return categoria_string;
    }

    public void setCategoria_string(String categoria_string) {
        this.categoria_string = categoria_string;
    }

    @SerializedName("body")
    private String text;

    public Demandas(){

    }

    public Demandas(int id, String titulo, String descricao, String data, int user_demanda, int categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.user_demanda = user_demanda;
        this.categoria = categoria;
    }


    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getId(){
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getUser_demanda() {
        return user_demanda;
    }

    public void setUser_demanda(int user_demanda) {
        this.user_demanda = user_demanda;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public Demandas(Parcel in){
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.titulo);
        dest.writeInt(this.categoria);
        dest.writeString(this.descricao);
        dest.writeInt(this.user_demanda);
        dest.writeString(this.data);
    }

    public void readFromParcel(Parcel parcel){
        this.id = parcel.readInt();
        this.titulo = parcel.readString();
        this.categoria = parcel.readInt();
        this.descricao = parcel.readString();
        this.user_demanda = parcel.readInt();
        this.data = parcel.readString();
    }

    public static final Parcelable.Creator<Demandas> CREATOR = new Parcelable.Creator<Demandas>(){
        @Override
        public Demandas createFromParcel (Parcel p){
            return new Demandas(p);
        }
        @Override
        public Demandas[] newArray(int size){
            return new Demandas[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }
}
