package com.dan.projeto.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dan.projeto.model.Usuario;

@Dao
public abstract class UsuarioDAO {

    @Insert
    public abstract long gravarUsuario(Usuario usuario);

    @Query("SELECT * FROM usuario WHERE email = :email")
    public abstract Usuario getUsuario(String email);

    @Query("SELECT * FROM usuario WHERE email = :email")
    public abstract Usuario usuarioExiste(String email);

    @Query("DELETE FROM usuario")
    public abstract int limpaTabelaUsuario();

    @Query("SELECT 1 FROM usuario WHERE email = :login AND senha = :senha")
    public abstract boolean getValidaLogin(String login, String senha);
}
