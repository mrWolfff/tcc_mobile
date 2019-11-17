package com.example.tcc_mobile.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_Config {

        private static Retrofit retrofit = null;

        public static Retrofit getClient(String baseUrl) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
}
