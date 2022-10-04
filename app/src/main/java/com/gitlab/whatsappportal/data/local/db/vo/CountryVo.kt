package com.gitlab.whatsappportal.data.local.db.vo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "country")
data class CountryVo(
    val flag: String,
    @PrimaryKey val name: String,
    val callingCode: String
): Parcelable