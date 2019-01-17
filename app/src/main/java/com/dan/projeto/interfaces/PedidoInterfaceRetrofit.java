package com.dan.projeto.interfaces;

import com.dan.projeto.model.Pedido;
import com.dan.projeto.model.PedidoResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface PedidoInterfaceRetrofit {

    @Headers("Cache-Control: max-age=640000, Content-type: application/json, Accept: application/json")
    @GET("pedidos/json")
    Call<List<Pedido>> buscarPedidos();

    @Headers("Cache-Control: max-age=640000, Content-type: application/json, Accept: application/json")
    @POST("pedidos/novo")
    Call<PedidoResponse> enviarPedido(Pedido pedido);
}
