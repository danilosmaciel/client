package com.dan.projeto.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices={@Index(value="email", unique=true)})
public class Usuario {

    String id;
    @PrimaryKey @NonNull
    String email;
    String nome;
    String telefoneCelular;
    String telefoneFixo;
    String senha;


    public Usuario(){};

    @Ignore
    public Usuario(String id, String email, String nome, String telefoneCelular, String telefoneFixo, String senha) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.telefoneCelular = telefoneCelular;
        this.telefoneFixo = telefoneFixo;
        this.senha = senha;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefoneCelular() {
        return telefoneCelular.length() > 0 ? telefoneCelular : "0000-0000";
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public String getTelefoneFixo() {
        return telefoneFixo.length() > 0 ? telefoneFixo : "0000-0000";
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getId() {
        return id.length() > 0 ? id : "0";
    }

    public void setId(String id) {
        this.id = id;
    }
/*
    @Override
    public String toString() {
        return  " { id: " + getId().length()  +
                ", email: " + getEmail()  +
                ", nome: " + getNome() +
                ", telefoneCelular: " + getTelefoneCelular() +
                ", telefoneFixo: " + getTelefoneFixo()+
                ", senha: " + getSenha()+
                ", tolkienAcesso: " + getTolkienAcesso() + "}";
    }
    */
}
