package com.example.weatherappmvi.di

import android.content.Context
import androidx.room.Room
import com.example.weatherappmvi.BuildConfig
import com.example.weatherappmvi.data.api.ApiService
import com.example.weatherappmvi.data.local.CitiesDao
import com.example.weatherappmvi.data.local.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface AppModule {


    companion object {

        @Singleton
        @Provides
        fun provideApiService(): ApiService {
            val client = OkHttpClient.Builder()
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


        private const val BASE_URL = "https://api.weatherapi.com/v1/"

    }
}