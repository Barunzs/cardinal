package com.barun.weather.data.repository


import com.barun.weather.data.response.album.Album
import retrofit2.http.GET


interface OpenAlbumDataApi {

    @GET("albums/")
    suspend fun getPost(): List<Album>
}