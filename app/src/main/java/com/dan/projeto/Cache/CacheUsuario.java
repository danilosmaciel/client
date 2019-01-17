package com.dan.projeto.Cache;

import com.dan.projeto.model.Usuario;

public class CacheUsuario {
    Usuario _usuario;

    public CacheUsuario(Usuario usuario) {
        _usuario = usuario;
    }

    public Usuario getUsuario(){
        return _usuario;
    }
}
