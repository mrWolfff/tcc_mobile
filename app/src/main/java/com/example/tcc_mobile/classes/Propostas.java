package com.example.tcc_mobile.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Propostas implements Parcelable {
    int id;
    private String proposta;
    private int valor;
    private String data;
    private String data_inicio;
    private String data_fim;
    private int user_proposta;
    private int to_user_proposta;
    private int demanda;
    private boolean ativo;
    private boolean aceito;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String user_proposta_string;

    public String getUser_proposta_string() {
        return user_proposta_string;
    }

    public void setUser_proposta_string(String user_proposta_string) {
        this.user_proposta_string = user_proposta_string;
    }

    public Propostas(){

    }

    public String getProposta() {
        return proposta;
    }

    public void setProposta(String proposta) {
        this.proposta = proposta;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getData_fim() {
        return data_fim;
    }

    public void setData_fim(String data_fim) {
        this.data_fim = data_fim;
    }

    public int getUser_proposta() {
        return user_proposta;
    }

    public void setUser_proposta(int user_proposta) {
        this.user_proposta = user_proposta;
    }

    public int getTo_user_proposta() {
        return to_user_proposta;
    }

    public void setTo_user_proposta(int to_user_proposta) {
        this.to_user_proposta = to_user_proposta;
    }

    public int getDemanda() {
        return demanda;
    }

    public void setDemanda(int demanda) {
        this.demanda = demanda;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAceito() {
        return aceito;
    }

    public void setAceito(boolean aceito) {
        this.aceito = aceito;
    }

    protected Propostas(Parcel in) {
        id = in.readInt();
        proposta = in.readString();
        valor = in.readInt();
        data = in.readString();
        data_inicio = in.readString();
        data_fim = in.readString();
        user_proposta = in.readInt();
        to_user_proposta = in.readInt();
        demanda = in.readInt();
        ativo = in.readByte() != 0;
        aceito = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(proposta);
        dest.writeInt(valor);
        dest.writeString(data);
        dest.writeString(data_inicio);
        dest.writeString(data_fim);
        dest.writeInt(user_proposta);
        dest.writeInt(to_user_proposta);
        dest.writeInt(demanda);
        dest.writeByte((byte) (ativo ? 1 : 0));
        dest.writeByte((byte) (aceito ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Propostas> CREATOR = new Creator<Propostas>() {
        @Override
        public Propostas createFromParcel(Parcel in) {
            return new Propostas(in);
        }

        @Override
        public Propostas[] newArray(int size) {
            return new Propostas[size];
        }
    };
}
