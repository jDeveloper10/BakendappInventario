package com.example.inventario3.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario3.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona el estado global de autenticación
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)
    
    /**
     * Estado que indica si el usuario está autenticado
     */
    val isAuthenticated: Flow<Boolean> = authRepository.authToken.map { token ->
        !token.isNullOrEmpty()
    }
    
    /**
     * Nombre de usuario actual
     */
    val username: Flow<String?> = authRepository.username
    
    /**
     * Email del usuario actual
     */
    val email: Flow<String?> = authRepository.email
    
    /**
     * Cierra la sesión del usuario
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}