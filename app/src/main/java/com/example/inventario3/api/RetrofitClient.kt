package com.example.inventario3.api

import com.example.inventario3.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para comunicarse con el backend en Railway
 */
object RetrofitClient {
    private const val BASE_URL = BuildConfig.API_BASE_URL
    
    /**
     * Moshi es una biblioteca para convertir entre JSON y objetos Kotlin
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    /**
     * Interceptor que añade el token JWT a las cabeceras de las peticiones
     */
    fun createAuthInterceptor(token: String): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
    }
    
    /**
     * Cliente HTTP para peticiones sin autenticación
     */
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * Crea un cliente HTTP con autenticación mediante JWT
     */
    fun createAuthHttpClient(token: String): OkHttpClient {
        return httpClient.newBuilder()
            .addInterceptor(createAuthInterceptor(token))
            .build()
    }
    
    /**
     * Instancia de Retrofit para peticiones sin autenticación
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    
    /**
     * Crea una instancia de Retrofit con autenticación mediante JWT
     */
    fun createAuthRetrofit(token: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createAuthHttpClient(token))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    
    /**
     * API para peticiones sin autenticación
     */
    val apiService: ApiService = retrofit.create(ApiService::class.java)
    
    /**
     * Crea una instancia de la API con autenticación mediante JWT
     */
    fun createAuthApiService(token: String): ApiService {
        return createAuthRetrofit(token).create(ApiService::class.java)
    }
}