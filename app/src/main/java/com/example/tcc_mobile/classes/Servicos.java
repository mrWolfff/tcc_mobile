package com.example.tcc_mobile.classes;

public class Servicos {
    private String data_servico;
    private String status;
    private int proposta;
    private int user;
    private int user_proposta;
    private String justificativa;
    private boolean cancel_confirm;
    private String avaliacao;
    private String sugestao_critica;


    public Servicos(){

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
}
