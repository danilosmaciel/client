package com.dan.projeto.interfaces;

import com.dan.projeto.model.CepRetrofit;
import com.dan.projeto.model.ClienteResponse;
import com.dan.projeto.model.PedidoResponse;
import com.dan.projeto.model.Usuario;
import com.dan.projeto.model.UsuarioResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioInterfaceRetrofit {

    //@Headers("Cache-Control: max-age=640000, Content-type: application/json, Accept: application/json")
    @GET("/cliente/{login}/{senha}")
    Call<ClienteResponse> buscarUsuario(@Path("login") String login, @Path("senha")String senha);

   // @Headers("Cache-Control: max-age=640000, Content-type: application/json")
    @POST("/cliente/cadastro")
    Call<UsuarioResponse> transmitirUsuario(@Body Usuario usuario);

}
