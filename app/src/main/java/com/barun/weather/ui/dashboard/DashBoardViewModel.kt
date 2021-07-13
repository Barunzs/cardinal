package com.barun.weather.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barun.weather.data.repository.OpenAlbumRepository
import com.barun.weather.data.response.Resource
import com.barun.weather.data.response.album.Album
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
@HiltViewModel
class DashBoardViewModel @Inject constructor(val repository: OpenAlbumRepository) : ViewModel() {

    private val TAG = DashBoardViewModel::class.java.simpleName
    private val _albumResponse: MutableLiveData<Resource<List<Album>>> = MutableLiveData()
    private val _sharedData: MutableLiveData<Album> = MutableLiveData()
    val albumResponse: LiveData<Resource<List<Album>>>
        get() = _albumResponse
    val sharedData: LiveData<Album>
        get() = _sharedData


    fun getAlbum() = viewModelScope.launch {
        _albumResponse.value =  repository.getAlbum()
    }


    fun setSharedData(album: Album){
        _sharedData.value = album
    }
}

