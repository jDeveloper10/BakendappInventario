package com.example.inventario3.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario3.api.AuthResponse
import com.example.inventario3.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Inicia sesión utilizando el backend desplegado en Railway
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Idle

            try {
                val result = authRepository.login(username, password)
                result.fold(
                    onSuccess = { authResponse ->
                        _loginState.value = LoginState.Success(authResponse)
                    },
                    onFailure = { exception ->
                        _loginState.value = LoginState.Error(exception.message ?: "Error desconocido")
                    }
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error de conexión: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Registra un nuevo usuario en el backend de Railway
     */
    fun register(username: String, email: String, password: String, fullName: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Idle

            try {
                val result = authRepository.register(username, email, password, fullName)
                result.fold(
                    onSuccess = { authResponse ->
                        _loginState.value = LoginState.Success(authResponse)
                    },
                    onFailure = { exception ->
                        _loginState.value = LoginState.Error(exception.message ?: "Error desconocido")
                    }
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error de registro: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Cierra la sesión eliminando los datos locales
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    sealed class LoginState {
        object Idle : LoginState()
        data class Success(val authResponse: AuthResponse) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}