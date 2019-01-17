package com.dan.projeto.model;

import android.arch.persistence.room.Ignore;

public class Item {

    String id;
    String nome;
    String descricao;
    double valor;
    public String status;

    public Item(){};
    @Ignore
    public Item(String id, String nome, String descricao, double valor, String status) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return  "id: " + getId() + ",\n" +
                "nome: " + getNome() + ",\n" +
                "descricao: " + getDescricao() + ",\n" +
                "valor: " + getValor() + ",\n" +
                "status:" + getStatus();
    }

}
