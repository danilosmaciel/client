package com.dan.projeto.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.dan.projeto.Cache.CacheItensVendidos;
import com.dan.projeto.dao.EnderecoDAO;
import com.dan.projeto.database.CarregaBanco;
import com.dan.projeto.helper.Funcoes;

import java.util.Locale;
import java.util.Map;

@Entity
public class Pedido {
    @PrimaryKey @NonNull
    public String    numeropedido;
    double           valor;
    private String   cliente;;
    private int      enderecoEntrega;
    private String   statusPedido;
    private String dataPedido;

    public Pedido() {}

    public String getNumeroPedido() {
        return numeropedido;
    }

    public void setNumeroPedido(String numeropedido) {
        this.numeropedido = numeropedido;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(int enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public String getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String toJson() {
        CacheItensVendidos cacheItensVendidos = CacheItensVendidos.getINSTANCE();
        StringBuilder sbPedido = new StringBuilder();
        sbPedido.append("{ 'numeropedido' : '"+getNumeroPedido()+"', ");
        sbPedido.append("'valor' : '"+cacheItensVendidos.getValorTotalPedido()+"', ");
        sbPedido.append("'cliente' : '"+getCliente()+"', ");
        sbPedido.append("'itens' : {  ");
        StringBuilder sbItens = new StringBuilder("");
        for(Map.Entry<String, Integer>  item : cacheItensVendidos.getItensVendidos().entrySet()){
            if(sbItens.length()>0){
                sbItens.append(",");
            }
            sbItens.append(String.format(new Locale ("pt", "BR"),"'%s':'%d'",item.getKey(),item.getValue()));
        }
        sbPedido.append(sbItens);
        sbPedido.append("   } }");

        return sbPedido.toString().trim();
    }

    @Override
    public String toString() {
        CacheItensVendidos cacheItensVendidos = CacheItensVendidos.getINSTANCE();
        return "Nro do pedido: "+getNumeroPedido() +
                "\nData: " + getDataPedido() +
                "\nValor: " + getValor();
    }
}
