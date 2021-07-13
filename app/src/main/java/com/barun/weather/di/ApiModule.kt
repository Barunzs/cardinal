package com.barun.weather.di

import com.barun.weather.BuildConfig
import com.barun.weather.data.repository.OpenAlbumDataApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private val mediaType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }


    @Provides
    fun provideApiService(retrofit: Retrofit): OpenAlbumDataApi {
        return retrofit.create(OpenAlbumDataApi::class.java)
    }

    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(mediaType))
            .build()
    }

    /***
     * Create OkHttpClient Instance
     */
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(loggerInterceptor)
        }.build()
    }

    /***
     * Create HttpLoggingInterceptor Instance with Level.BODY
     */
    private val loggerInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

}