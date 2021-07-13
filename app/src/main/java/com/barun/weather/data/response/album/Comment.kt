package com.barun.weather.data.response.album

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val body: String,
    val id: Int,
    val postId: Int
)