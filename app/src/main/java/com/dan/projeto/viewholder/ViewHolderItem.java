package com.dan.projeto.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dan.projeto.R;

public class ViewHolderItem {
    public final TextView _nome;
    public final TextView _descricao;
    public final TextView _valor;
    public final ImageView _imagem;

    public ViewHolderItem(View view) {
        _nome       = view.findViewById(R.id.adapter_item_nome);
        _descricao  = view.findViewById(R.id.adapter_item_descricao);
        _imagem     = view.findViewById(R.id.adapter_item_imagem);
        _valor       = view.findViewById(R.id.adapter_item_valor);
    }

}
