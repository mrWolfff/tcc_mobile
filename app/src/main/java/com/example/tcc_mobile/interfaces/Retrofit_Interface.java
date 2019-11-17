package com.example.tcc_mobile.interfaces;

import com.example.tcc_mobile.classes.Demandas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Retrofit_Interface {

    @GET("demandas/")
    Call<List<Demandas>> getDemandas();

    @GET("get_token/")
    Call getToken();

    @POST("/create_demanda")
    @FormUrlEncoded
    Call<Demandas> create_demanda(@Field("titulo") String titulo,
                        @Field("categoria") String categoria,
                        @Field("userId") long userId);

}
