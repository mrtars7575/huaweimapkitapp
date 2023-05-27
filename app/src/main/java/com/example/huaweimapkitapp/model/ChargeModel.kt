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
    val operatorInfo: OperatorInfo?,
    @SerializedName("Connections")
    val connections: List<Connection>?,
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
    val country: Country,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double
    ) : Serializable

data class OperatorInfo(
    @SerializedName("WebsiteURL")
    val webSiteURL : String?
    ) : Serializable

data class Connection(
    @SerializedName("ConnectionType")
    val connectionType: ConnectionType,
    val powerKW: Double?,
    val quantity: Int?,
    val amps: Int?,
    val voltage: Int?,
    val currentTypeName: String?,
    val level: Int?,
    val levelName: String?,
    val isFastChargeCapable: Boolean?,
    val maxSocketPower: Int?,
    val comments: String?,

):Serializable

data class ConnectionType(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Title")
    val title: String,
    @SerializedName("FormalName")
    val formalName: String?,
    @SerializedName("IsDiscontinued")
    val isDiscontinued: Boolean?,
    // Diğer özellikler
):Serializable

data class Country(
    @SerializedName("Title")
    val title: String?,
    @SerializedName("ISOCode")
    val isoCode: String?
    ) : Serializable
