package com.example.shoppingapp

//import android.telecom.Call
import retrofit2.http.GET
import retrofit2.Call

interface  apiinterface {
    @GET("products")

    abstract fun getProductData(): Call<MyData>
}