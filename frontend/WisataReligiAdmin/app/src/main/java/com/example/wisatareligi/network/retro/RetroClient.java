package com.example.wisatareligi.network.retro;

import com.example.wisatareligi.network.service.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL;

public class RetroClient {
    private static Retrofit retrofit = null;

    private static Retrofit getClient(){
        //cek jika retrofit null
        if (retrofit == null){
            //maka buat object dari retrofit
            retrofit = new Retrofit.Builder()
                    //ubah sesuai urlnya
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //get data
    public static ApiService getApiService(){
        return getClient().create(ApiService.class);
    }
}