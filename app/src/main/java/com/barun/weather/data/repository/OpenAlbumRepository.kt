package com.barun.weather.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class OpenAlbumRepository @Inject constructor(
    private var apiService: OpenAlbumDataApi,
) : BaseRepository() {

    val TAG = OpenAlbumRepository::class.java.simpleName

    val coroutineErrorHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, exception.printStackTrace().toString())
    }

    suspend fun getAlbum() = safeApiCall {
        apiService.getPost()
    }
}
