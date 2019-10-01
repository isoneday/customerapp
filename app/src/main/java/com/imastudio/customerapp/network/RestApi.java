package com.imastudio.customerapp.network;


import com.imastudio.customerapp.model.modelmap.ResponseMap;
import com.imastudio.customerapp.model.modelojekonline.ResponseLogin;
import com.imastudio.customerapp.model.modelojekonline.ResponseRegister;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {
    //    //todo 2 set endpoint di api.php
//
//    //    //endpoint untuk register
    @FormUrlEncoded
    @POST("daftar")
    Call<ResponseRegister> register(
            @Field("nama") String strnama,
            @Field("password") String strpass,
            @Field("email") String stremail,
            @Field("phone") String strphone
    );

    //    //endpoint untuk login
    @FormUrlEncoded
    @POST("login")
    Call<ResponseLogin> login(
            @Field("device") String strdevice,
            @Field("f_password") String strpass,
            @Field("f_email") String stremail

    );

    @GET("json")
    Call<ResponseMap> getdataMap(
         @Query("origin") String strorigin,
         @Query("destination") String strdestination,
         @Query("key") String strkey
    );
}