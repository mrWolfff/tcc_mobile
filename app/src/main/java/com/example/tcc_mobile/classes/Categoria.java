package com.example.tcc_mobile.classes;

public class Categoria {
    private int id;
    private String categoria;
    private String imagem;

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
}
