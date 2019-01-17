package com.dan.projeto.interfaces;

import com.dan.projeto.model.CepRetrofit;
import com.dan.projeto.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ItemInterfaceRetrofit {

    @Headers("Cache-Control: max-age=640000, Content-type: application/json, Accept: application/json")
    @GET("produtos/json")
    Call<List<Item>> buscarItens();
}
