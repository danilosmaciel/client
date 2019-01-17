package com.dan.projeto.model;

import java.io.Serializable;

public class UsuarioResponse implements Serializable {
    private String id;
    private String  email;
    private String  nome;
    private String status;
    private String tolkienAcesso;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTolkienAcesso() {
        return tolkienAcesso.length() > 0 ? tolkienAcesso : "SEm";
    }
    public void setTolkienAcesso(String tolkienAcesso) {
        this.tolkienAcesso = tolkienAcesso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

