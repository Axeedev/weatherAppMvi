package com.example.weatherappmvi.di

import android.content.Context
import androidx.room.Room
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.weatherappmvi.BuildConfig
import com.example.weatherappmvi.data.api.ApiService
import com.example.weatherappmvi.data.local.CitiesDao
import com.example.weatherappmvi.data.local.Database
import com.example.weatherappmvi.data.repository.FavouriteRepositoryImpl
import com.example.weatherappmvi.data.repository.SearchRepositoryImpl
import com.example.weatherappmvi.data.repository.WeatherRepositoryImpl
import com.example.weatherappmvi.domain.repository.FavouriteRepository
import com.example.weatherappmvi.domain.repository.SearchRepository
import com.example.weatherappmvi.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface AppModule {


    @Singleton
    @Binds
    fun bindFavRepository(favouriteRepositoryImpl: FavouriteRepositoryImpl): FavouriteRepository

    @Singleton
    @Binds
    fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    @Singleton
    @Binds
    fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @Singleton
        @Provides
        fun provideApiService(): ApiService {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val newUrl = original
                        .url
                        .newBuilder()
                        .addQueryParameter("key", BuildConfig.API_KEY)
                        .build()
                    val new = original.newBuilder()
                        .url(newUrl)
                        .build()
                    chain.proceed(new)
                }.build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): Database {
            return Room.databaseBuilder(
                name = "cities",
                klass = Database::class.java,
                context = context
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        @Singleton
        @Provides
        fun provideDao(database: Database): CitiesDao = database.citiesDao()


        @Provides
        fun provideStoreFactory() : StoreFactory = DefaultStoreFactory()

        private const val BASE_URL = "https://api.weatherapi.com/v1/"

    }
}