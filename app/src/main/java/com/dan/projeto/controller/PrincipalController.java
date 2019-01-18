package com.dan.projeto.controller;

import android.content.Context;

import com.dan.projeto.dao.PedidoDAO;
import com.dan.projeto.database.CarregaBanco;
import com.dan.projeto.model.Pedido;
import com.dan.projeto.model.UsuarioResponse;
import com.dan.projeto.PrincipalActivity;

import java.util.List;

public class PrincipalController {

    private Context _contexto;
    private UsuarioResponse _usuario;

    public PrincipalController(PrincipalActivity contexto, UsuarioResponse usuarioResponse) {
        _contexto = contexto;
        _usuario = usuarioResponse;
    }

    public String getUsuarioNome() {
        return _usuario.getNome();
    }

    public List<Pedido> carregaPedidos(String usuarioId) {

        PedidoDAO pedidoDAO = new CarregaBanco().getDb(_contexto).getPedidoDao();
        if(pedidoDAO.getQtdePedidos() > 0){
            return pedidoDAO.getPedidosCliente(usuarioId);
    }
        return null;
    }

    public String getUsuarioId() {
        return _usuario.getId();
    }

    public UsuarioResponse getUsuario() {
        return _usuario;
    }
}
