package com.example.inventario3.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Solicitud de inicio de sesión
 */
@JsonClass(generateAdapter = true)
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * Solicitud de registro de usuario
 */
@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String? = null
)

/**
 * Respuesta de autenticación del servidor
 */
@JsonClass(generateAdapter = true)
data class AuthResponse(
    val token: String,
    val username: String,
    val email: String,
    val roles: List<String>
)

/**
 * Modelo de error para manejar errores del API
 */
@JsonClass(generateAdapter = true)
data class ApiError(
    val timestamp: String? = null,
    val status: Int? = null,
    @Json(name = "error")
    val errorType: String? = null,
    val message: String? = null,
    val path: String? = null
)