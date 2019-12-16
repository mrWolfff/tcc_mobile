package com.example.tcc_mobile.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Servicos implements Parcelable {
    private String data_servico;
    private int id;
    private String status;
    private int proposta;
    private int user;
    private int user_proposta;
    private String justificativa;
    private boolean cancel_confirm;
    private String avaliacao;
    private String sugestao_critica;
    private int demanda;

    private String proposta_string;
    private String user_string;
    private String user_proposta_string;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProposta_string() {
        return proposta_string;
    }

    public void setProposta_string(String proposta_string) {
        this.proposta_string = proposta_string;
    }

    public String getUser_string() {
        return user_string;
    }

    public void setUser_string(String user_string) {
        this.user_string = user_string;
    }

    public String getUser_proposta_string() {
        return user_proposta_string;
    }

    public void setUser_proposta_string(String user_proposta_string) {
        this.user_proposta_string = user_proposta_string;
    }

    public Servicos(){

    }

    public Servicos(Parcel in) {
        id = in.readInt();
        data_servico = in.readString();
        status = in.readString();
        proposta = in.readInt();
        user = in.readInt();
        user_proposta = in.readInt();
        justificativa = in.readString();
        cancel_confirm = in.readByte() != 0;
        avaliacao = in.readString();
        sugestao_critica = in.readString();
        demanda = in.readInt();
        proposta_string = in.readString();
        user_string = in.readString();
        user_proposta_string = in.readString();

    }

    public static final Creator<Servicos> CREATOR = new Creator<Servicos>() {
        @Override
        public Servicos createFromParcel(Parcel in) {
            return new Servicos(in);
        }

        @Override
        public Servicos[] newArray(int size) {
            return new Servicos[size];
        }
    };

    public int getDemanda() {
        return demanda;
    }

    public void setDemanda(int demanda) {
        this.demanda = demanda;
    }

    public String getData_servico() {
        return data_servico;
    }

    public void setData_servico(String data_servico) {
        this.data_servico = data_servico;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProposta() {
        return proposta;
    }

    public void setProposta(int proposta) {
        this.proposta = proposta;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getUser_proposta() {
        return user_proposta;
    }

    public void setUser_proposta(int user_proposta) {
        this.user_proposta = user_proposta;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public boolean isCancel_confirm() {
        return cancel_confirm;
    }

    public void setCancel_confirm(boolean cancel_confirm) {
        this.cancel_confirm = cancel_confirm;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getSugestao_critica() {
        return sugestao_critica;
    }

    public void setSugestao_critica(String sugestao_critica) {
        this.sugestao_critica = sugestao_critica;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(data_servico);
        dest.writeString(status);
        dest.writeInt(proposta);
        dest.writeInt(user);
        dest.writeInt(user_proposta);
        dest.writeString(justificativa);
        dest.writeByte((byte) (cancel_confirm ? 1 : 0));
        dest.writeString(avaliacao);
        dest.writeString(sugestao_critica);
        dest.writeInt(demanda);
        dest.writeString(proposta_string);
        dest.writeString(user_string);
        dest.writeString(user_proposta_string);
    }
}
