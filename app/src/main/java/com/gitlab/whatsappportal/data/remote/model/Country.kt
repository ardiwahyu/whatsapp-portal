package com.gitlab.whatsappportal.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class Country (
    @SerializedName("flags") val flags: Flag,
    @SerializedName("name") val name: Name,
    @SerializedName("idd") val callingCodes: CallingCode
): Parcelable {

    @Parcelize
    data class Name(
        @SerializedName("common") val common: String
    ): Parcelable

    @Parcelize
    data class CallingCode(
        @SerializedName("root") val root: String,
        @SerializedName("suffixes") val suffixes: ArrayList<String>
    ): Parcelable

    @Parcelize
    data class Flag(
        @SerializedName("png") val png: String
    ): Parcelable
}