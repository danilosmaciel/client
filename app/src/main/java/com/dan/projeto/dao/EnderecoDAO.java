package com.dan.projeto.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dan.projeto.model.Endereco;
import com.dan.projeto.model.Pedido;

import java.util.List;

@Dao
public abstract class EnderecoDAO {

    @Insert
    public abstract long gravarEndereco(Endereco endereco);

    @Query("SELECT * FROM endereco WHERE id = :id")
    public abstract Endereco getEndereco(int id);


    @Query("SELECT * FROM endereco")
    public abstract List<Endereco> getEnderecos();

    @Update()
    public abstract void atualizaEndereco(Pedido pedido);


}