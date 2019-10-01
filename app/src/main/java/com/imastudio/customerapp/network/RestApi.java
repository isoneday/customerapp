package com.imastudio.customerapp.network;


import com.imastudio.customerapp.model.ResponseRegister;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

}