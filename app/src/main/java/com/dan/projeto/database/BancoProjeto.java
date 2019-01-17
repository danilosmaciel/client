package com.dan.projeto.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dan.projeto.dao.EnderecoDAO;
import com.dan.projeto.dao.PedidoDAO;
import com.dan.projeto.dao.UsuarioDAO;
import com.dan.projeto.model.Endereco;
import com.dan.projeto.model.Pedido;
import com.dan.projeto.model.Usuario;

@Database(entities = {Pedido.class,Usuario.class,Endereco.class},version = 1, exportSchema = false)
public  abstract class BancoProjeto extends RoomDatabase {

    public abstract PedidoDAO getPedidoDao();

    public abstract UsuarioDAO getUsuarioDao();

    public abstract EnderecoDAO getEndereooDao();

}
