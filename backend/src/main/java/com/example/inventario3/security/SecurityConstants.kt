package com.example.inventario3.security

/**
 * Constantes utilizadas en el sistema de seguridad
 */
object SecurityConstants {
    // Roles disponibles en el sistema
    const val ROLE_USER = "ROLE_USER"
    const val ROLE_ADMIN = "ROLE_ADMIN"
    
    // JWT Headers
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "Authorization"
}