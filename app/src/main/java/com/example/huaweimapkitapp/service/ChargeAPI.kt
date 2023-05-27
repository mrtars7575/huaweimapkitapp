package com.example.huaweimapkitapp.service

import com.example.huaweimapkitapp.ChargeModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChargeAPI {
    @GET("poi")
    fun getData(@Query("longitude") longitude: Double?,
                @Query("latitude") latitude: Double?,
                @Query("distance") distance: Int = 10,
                @Query("distanceunit") distanceUnit: String = "km",
                @Query("countrycode") countryCode : String?,
                @Query("output") output: String = "json",
                @Query("verbose") verbose: Boolean = true,
                @Query("key") key : String): Call<List<ChargeModel>>

    @GET("poi")
    fun getDataToLocationInfo(
        @Query("longitude") longitude: Double?,
        @Query("latitude") latitude: Double?,
        @Query("distance") distance: Int = 10,
        @Query("distanceunit") distanceUnit: String = "km",

        @Query("output") output: String = "json",
        @Query("verbose") verbose: Boolean = true,
        @Query("key") key : String):Call<List<ChargeModel>>


    @GET("poi")
    fun getDataToCountrCode(
        @Query("distance") distance: Int = 10,
        @Query("distanceunit") distanceUnit: String = "km",
        @Query("countrycode") countryCode : String?,
        @Query("output") output: String = "json",
        @Query("verbose") verbose: Boolean = true,
        @Query("key") key : String):Call<List<ChargeModel>>

}