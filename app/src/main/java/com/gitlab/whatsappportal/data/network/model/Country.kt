package com.gitlab.whatsappportal.data.network.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
data class Country (
    @SerializedName("flag") val flag: String,
    @SerializedName("name") val name: String,
    @SerializedName("callingCodes") val callingCodes: ArrayList<String>
): Parcelable