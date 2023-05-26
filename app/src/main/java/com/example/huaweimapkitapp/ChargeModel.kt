package com.example.huaweimapkitapp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChargeModel (
    @SerializedName("ID")
    val uuid: String?,
    @SerializedName("AddressInfo")
    val addressInfo: AddressInfo?,
    @SerializedName("OperatorInfo")
    val operatorInfo: OperatorInfo?
    ) : Serializable

data class AddressInfo (
    @SerializedName("Title")
    val title: String?,
    @SerializedName("AddressLine1")
    val addressLine1: String?,
    @SerializedName("AddressLine2")
    val addressLine2: String?,
    @SerializedName("Town")
    val town: String?,
    @SerializedName("StateOrProvince")
    val stateOrProvince: String?,
    @SerializedName("Postcode")
    val postcode: String?,
    @SerializedName("Country")
    val country: Country?,
    @SerializedName("Latitude")
    val latitude: Double?,
    @SerializedName("Longitude")
    val longitude: Double?
    ) : Serializable

data class OperatorInfo(
    @SerializedName("WebsiteURL")
    val webSiteURL : String?
    ) : Serializable



data class Country(
    @SerializedName("Title")
    val title: String?,
    @SerializedName("ISOCode")
    val isoCode: String?
    ) : Serializable
