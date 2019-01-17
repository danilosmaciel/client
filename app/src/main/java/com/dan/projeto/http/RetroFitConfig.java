package com.dan.projeto.http;

import com.dan.projeto.interfaces.ItemInterfaceRetrofit;
import com.dan.projeto.interfaces.PedidoInterfaceRetrofit;
import com.dan.projeto.interfaces.UsuarioInterfaceRetrofit;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetroFitConfig {

    private final Retrofit retrofit;

    public RetroFitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://www.projetox.kinghost.net:21015/") // sem o recurso, será colocado na interface
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
    public ItemInterfaceRetrofit httpItem(){
        return this.retrofit.create(ItemInterfaceRetrofit.class);
    }

    public UsuarioInterfaceRetrofit httpUsuario(){
        return this.retrofit.create(UsuarioInterfaceRetrofit.class);
    }

    public PedidoInterfaceRetrofit httpPedido(){
        return this.retrofit.create(PedidoInterfaceRetrofit.class);
    }
}