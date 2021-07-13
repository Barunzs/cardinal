package com.barun.weather.data.response.album

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class Alumb(
    val albums: List<Album>,
    val comments: List<Comment>
)