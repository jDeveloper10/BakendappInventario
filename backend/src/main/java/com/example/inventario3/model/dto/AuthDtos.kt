package com.example.inventario3.model.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * DTO para la solicitud de login
 */
data class LoginRequest(
    @field:NotBlank(message = "El nombre de usuario es requerido")
    val username: String,
    
    @field:NotBlank(message = "La contraseña es requerida")
    val password: String
)

/**
 * DTO para la solicitud de registro
 */
data class RegisterRequest(
    @field:NotBlank(message = "El nombre de usuario es requerido")
    @field:Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    val username: String,
    
    @field:NotBlank(message = "El correo electrónico es requerido")
    @field:Email(message = "Debe proporcionar un correo electrónico válido")
    val email: String,
    
    @field:NotBlank(message = "La contraseña es requerida")
    @field:Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    val password: String,
    
    val fullName: String? = null
)

/**
 * DTO para la respuesta de autenticación
 */
data class AuthResponse(
    val token: String,
    val username: String,
    val email: String,
    val roles: List<String>
)