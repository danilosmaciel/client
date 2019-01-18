package com.dan.projeto.Cache;

import com.dan.projeto.model.Item;

import java.util.HashMap;
import java.util.Map;

public class CacheItensVendidos {
    HashMap<String,Integer> _listaItensVendidos;
    static CacheItensVendidos INSTANCE = null;
    double _valorTotalVenda;

    private CacheItensVendidos() {
        _listaItensVendidos = new HashMap<String,Integer>();
        _valorTotalVenda    = 0.0;
    }

    public static CacheItensVendidos getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new CacheItensVendidos();
        }
        return INSTANCE;
    }

    public HashMap<String,Integer> getItensVendidos(){
        return _listaItensVendidos;
    }

    public void adicionaItem(String codigoItem, int quantidade){
        if(_listaItensVendidos.containsKey(codigoItem)){
            _listaItensVendidos.remove(codigoItem);
        }
        _listaItensVendidos.put(codigoItem, quantidade);
    }

    public void adicionaValorVenda(double valor){
        _valorTotalVenda += valor;
    }

    public void removeItem(String codigoItem){
        if(_listaItensVendidos.containsKey(codigoItem)){
            _listaItensVendidos.remove(codigoItem);
        }
    }

    public int getQtdeItensVendidos(){
       return  _listaItensVendidos.size();
    }

    public String getMsgItensVendidos(){
        CacheItem cacheItem = CacheItem.getINSTANCE();

        StringBuilder sb = new StringBuilder();
        sb.append("Qtde.\t\t\t\t Produto\t\t    \n");
        for(Map.Entry<String, Integer> item : _listaItensVendidos.entrySet()){
            double auxValor = cacheItem.getValorItem(item.getKey());
            int qtde = item.getValue();
            sb.append(String.format(" %d\t\t = Cód. %s - %s(R$ %.2f )\n", qtde, item.getKey(), cacheItem.getNomeItem(item.getKey()), auxValor));

        }
        sb.append("\n\nValor Total = R$ " + getValorTotalPedido());
        return sb.toString();
    }

    public double getValorTotalPedido(){
        double valorTotal = 0.0;
        CacheItem cacheItem = CacheItem.getINSTANCE();

        for(Map.Entry<String, Integer> item : _listaItensVendidos.entrySet()){
            double auxValor = cacheItem.getValorItem(item.getKey());
            int qtde = item.getValue();
            valorTotal += auxValor * qtde;
        }

        return valorTotal;
    }

    public int getQtdeItemVedida(String id) {
         return _listaItensVendidos.get(id);
    }

    public boolean contem(String id) {
        return _listaItensVendidos.containsKey(id) ;
    }
}
