package com.dan.projeto.Cache;

import com.dan.projeto.model.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CacheItem {
    List<Item> _listaItens;
    List<Item> _listaItensDestaque;
    static CacheItem INSTANCE;

    private CacheItem() {  inicializarLista();}

    public static CacheItem getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new CacheItem();
        }
        return INSTANCE;
    }

    public boolean insereItem(Item item){
       return  _listaItens.add(item);
    }
    public boolean insereItemDestaque(Item item){
        return  _listaItensDestaque.add(item);
    }

    public Item getItem(int codigo){
        return _listaItens.get(_listaItens.indexOf(codigo));
    }

    public List<Item> getItens(){
        return _listaItens;
    }

    public List<Item> getItensDestaques(){
        return _listaItensDestaque;
    }

    public boolean removeItem(int codigo){
        return _listaItens.remove(_listaItens.indexOf(codigo)) != null;
    }
    public void inicializarLista(){
        _listaItens = new ArrayList<Item>();
        _listaItensDestaque = new ArrayList<Item>();
    }

    public void insereTodos(List<Item> lista){
        inicializarLista();
        this._listaItens.addAll(lista);
    }

    public String getNomeItem(String codigoItem){
        for(Item item : _listaItens){
            if(item.getId().equals(codigoItem)){
                return item.getNome();
            }
        }
        return "";
    }

    public double getValorItem(String codigoItem) {
        double valor = 0.0;
        for(Item item : _listaItens){
            if(item.getId().equals(codigoItem)){
                valor += item.getValor();
            }
        }
        return valor;
    }

}
