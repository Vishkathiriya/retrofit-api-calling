package com.example.ibl_post_call

import com.example.ibl_post_call.repsonse.MyResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MyInterface {

    @POST("api/v1/create")
    fun createUser(@Body data: JsonObject): Call<MyResponse>
}