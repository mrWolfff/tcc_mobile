package com.example.tcc_mobile.classes;

public class Messages {
        private String message;
        private String data;
        private int from_user;
        private int to_user;
        private int session;
        private boolean is_proposta;
        private int proposta;
        private int request_user;

        public Messages(){

        }

        public int getRequest_user(){
            return request_user;
        }
        public void setRequest_user(int request_user){
            this.request_user = request_user;
        }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public boolean isIs_proposta() {
        return is_proposta;
    }

    public void setIs_proposta(boolean is_proposta) {
        this.is_proposta = is_proposta;
    }

    public int getProposta() {
        return proposta;
    }

    public void setProposta(int proposta) {
        this.proposta = proposta;
    }
}
