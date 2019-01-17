package com.dan.projeto.controller;

import android.content.Context;

import com.dan.projeto.Cache.CacheItensVendidos;
import com.dan.projeto.dao.PedidoDAO;
import com.dan.projeto.database.CarregaBanco;
import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.http.HttpSendPedido;
import com.dan.projeto.model.Endereco;
import com.dan.projeto.model.Item;
import com.dan.projeto.model.Pedido;
import com.dan.projeto.model.PedidoResponse;
import com.dan.projeto.model.UsuarioResponse;
import com.dan.projeto.view.PedidoItemActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PedidoItemContoller {
    private Context    _ctx;
    private String     _numeroPedido;
    private Pedido     _pedido;
    UsuarioResponse    _usuario;
    CacheItensVendidos _itensVendidos;

    public PedidoItemContoller(PedidoItemActivity ctx, UsuarioResponse usuarioResponse) {
        _ctx           = ctx;
        _numeroPedido  = geraNumeroPedido();
        _usuario       = usuarioResponse;
        _itensVendidos = CacheItensVendidos.getINSTANCE();

        criaPedido();
    }

    private String geraNumeroPedido() {
        Long numero =  Calendar.getInstance().getTimeInMillis();
        return numero.toString();
    }

    public void criaPedido() {
        _pedido = new Pedido();
        _pedido.setNumeroPedido(_numeroPedido);
        _pedido.setStatusPedido("REALIZADO");
        _pedido.setEnderecoEntrega(0);
        _pedido.setValor(0.0);
        _pedido.setCliente(_usuario.getId());
    }


    public void insereItemVendido(Item item, int qtde) {
        _itensVendidos.adicionaItem(item.getId(),qtde);
    }

    public void removeItem(Item item, int qtde) {
        _itensVendidos.adicionaItem(item.getId(),qtde);

    }

    public int getQtdeItensVendidos() {
        return _itensVendidos.getQtdeItensVendidos();
    }

    public String getMsgItensVedidos() {
       return _itensVendidos.getMsgItensVendidos();
    }

    private void atualizarStatusPedido(PedidoResponse pedidoResponse) {
        PedidoDAO pedidoDAO = new CarregaBanco().getDb(_ctx).getPedidoDao();
        pedidoDAO.atualizaStatusPedido(pedidoResponse.getNumeroPedido(),pedidoResponse.getStatus());
    }

    private boolean encerrarPedido(Pedido pedido) {
        PedidoDAO pedidoDAO = new CarregaBanco().getDb(_ctx).getPedidoDao();
        return pedidoDAO.gravarPedido(pedido) != null;
    }

    public Context getContext(){
        return _ctx;
    }

    public boolean enviaPedido() {
        new HttpSendPedido(_ctx, _pedido, Funcoes.ENVIA_PEDIDO).execute();
        return false;
    }

    public void gravarPedidoNoBanco() {
        PedidoDAO pedidoDAO = new CarregaBanco().getDb(_ctx).getPedidoDao();
        _pedido.setStatusPedido("recebido");
        SimpleDateFormat sp = new SimpleDateFormat("dd/mm/yyyy HH:mm");
        Calendar cal = new GregorianCalendar();
        sp.setTimeZone(cal.getTimeZone());
        _pedido.setDataPedido(sp.format(cal.getTime()));
        pedidoDAO.gravarPedido(_pedido);
    }

    public void setEnderecoEntrega(int enderecoEntrega){
        _pedido.setEnderecoEntrega(enderecoEntrega);
    }

    public void gerarNovoNumeroPedido() {
        _pedido.setNumeroPedido(geraNumeroPedido());
    }

    public boolean enderecoEstaVazio() {
        return !(_pedido.getEnderecoEntrega() > 0);
    }
}
