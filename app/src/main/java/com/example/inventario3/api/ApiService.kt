package com.example.inventario3.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de Retrofit para comunicarse con el backend en Railway
 */
interface ApiService {
    /**
     * Endpoint para iniciar sesión
     */
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>
    
    /**
     * Endpoint para registrarse
     */
    @POST("api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>
}