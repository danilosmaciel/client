package com.dan.projeto.componentes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dan.projeto.R;
import com.dan.projeto.model.Item;
import com.dan.projeto.viewholder.ViewHolderItem;

import java.util.List;

public class AdapterItem extends BaseAdapter {
    private final List<Item> _listaItens;
    private final Activity   _activity;

    public AdapterItem(Activity activity, List<Item> listaItens) {
        this._listaItens = listaItens;
        this._activity = activity;
    }

    @Override
    public int getCount() {
        return _listaItens.size();
    }

    @Override
    public Item getItem(int position) {
        return _listaItens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;//_listaItens.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View elemento;
        Item item = getItem(position);
        ViewHolderItem holder;


        if( convertView == null) {
            elemento = LayoutInflater.from(_activity).inflate(R.layout.lista_adapter_itens, parent, false);
            holder = new ViewHolderItem(elemento);
            elemento.setTag(holder);
        } else {
            elemento = convertView;
            holder = (ViewHolderItem) elemento.getTag();
        }

        holder._nome.setText(item.getNome());
        holder._descricao.setText(item.getDescricao());
        holder._valor.setText(String.valueOf(item.getValor()));
        holder._imagem.setImageResource(R.drawable.lanche);

        return elemento;
    }
}
