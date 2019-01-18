package com.dan.projeto.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dan.projeto.model.Pedido;

import java.util.List;

@Dao
public abstract class PedidoDAO {

    @Insert
    public abstract Long gravarPedido(Pedido pedido);

    @Query("UPDATE PEDIDO SET statusPedido = :status WHERE numeropedido = :numero ")
    public abstract void atualizaStatusPedido(String numero, String status);

    @Query("SELECT * FROM pedido WHERE numeropedido = :numero")
    public abstract Pedido getPedido(String numero);

    @Query("SELECT * FROM pedido")
    public abstract List<Pedido> getPedidos();

    @Query("SELECT COUNT(1) FROM pedido")
    public abstract int getQtdePedidos();

    @Query("SELECT *  FROM pedido WHERE cliente = :clienteId")
    public abstract List<Pedido> getPedidosCliente(String clienteId);
}
