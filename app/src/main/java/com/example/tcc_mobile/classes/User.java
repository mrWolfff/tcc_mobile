package com.example.tcc_mobile.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String token;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String cpf_cnpj;
    private String sexo;
    private String telefone;
    private String celular;
    private String endereço;
    private String cidade;
    private String estado;
    private int categoria;
    private String categoria_user;

    public User(){


    }

    public User(int id, String first_name, String last_name, String username, String email, int categoria, String categoria_user){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.categoria = categoria;
        this.categoria_user = categoria_user;
    }
    public User(int id, String first_name, String last_name, String email, String cpf_cnpj, String sexo, String telefone, String celular, String endereco, String cidade, String estado, int categoria, String categoria_user) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.cpf_cnpj = cpf_cnpj;
        this.sexo = sexo;
        this.telefone = telefone;
        this.celular = celular;
        this.endereço = endereço;
        this.cidade = cidade;
        this.estado = estado;
        this.categoria = categoria;
        this.categoria_user = categoria_user;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setID(int id){
        this.id = id;
    }
    public void setUsername(String username){
        this.username = username;
    }


    public String get_full_name() {
        return first_name +" "+last_name;
    }

    public String getUsername(){
        return username;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getCategoria_user() {
        return categoria_user;
    }

    public void setCategoria_user(String categoria_user) {
        this.categoria_user = categoria_user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public User(Parcel in){
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.token);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.categoria_user);
    }

    public void readFromParcel(Parcel parcel){
        this.id = parcel.readInt();
        this.token = parcel.readString();
        this.first_name = parcel.readString();
        this.last_name = parcel.readString();
        this.username = parcel.readString();
        this.email = parcel.readString();
        this.categoria_user = parcel.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){
        @Override
        public User createFromParcel (Parcel p){
            return new User(p);
        }
        @Override
        public User[] newArray(int size){
            return new User[size];
        }
    };

    public String getToken() {
        return token;
    }

    public int getID() {
        return id;
    }
}
