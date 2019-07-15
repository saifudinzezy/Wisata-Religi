package com.example.wisatareligi.network;

import com.example.wisatareligi.model.acara.ResponseAcara;
import com.example.wisatareligi.model.galeri.ResponseGaleri;
import com.example.wisatareligi.model.wisata.ResponseWisata;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    //wisata
    @GET("read/wisata.php")
    Call<ResponseWisata> getWisata();

    @FormUrlEncoded
    @POST("read/wisata.php")
    Call<ResponseWisata> getWisataId(@Field("id") String id);

    //galeri
    @GET("read/galeri.php")
    Call<ResponseGaleri> getGaleri();

    @FormUrlEncoded
    @POST("read/galeri.php")
    Call<ResponseGaleri> getGaleriId(@Field("id") String id);

    //acara
    @GET("read/acara.php")
    Call<ResponseAcara> getAcara();

    @FormUrlEncoded
    @POST("read/acara.php")
    Call<ResponseAcara> getAcaraId(@Field("id") String id);
}