package com.example.inventario3.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.inventario3.api.ApiService
import com.example.inventario3.api.AuthResponse
import com.example.inventario3.api.LoginRequest
import com.example.inventario3.api.RegisterRequest
import com.example.inventario3.api.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore para guardar las preferencias de usuario, incluido el token JWT
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

/**
 * Repositorio responsable de operaciones de autenticación y gestión de sesión
 */
class AuthRepository(private val context: Context) {
    
    private val apiService: ApiService = RetrofitClient.apiService
    
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
    }
    
    /**
     * Obtiene el token JWT almacenado
     */
    val authToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }
    
    /**
     * Obtiene el nombre de usuario almacenado
     */
    val username: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME_KEY]
        }
    
    /**
     * Obtiene el email del usuario almacenado
     */
    val email: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[EMAIL_KEY]
        }
    
    /**
     * Intenta iniciar sesión con las credenciales proporcionadas
     * @return Resultado que contiene los datos del usuario en caso de éxito
     */
    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            
            if (response.isSuccessful && response.body() != null) {
                // Guardar datos de autenticación
                val authResponse = response.body()!!
                saveAuthData(authResponse)
                Result.success(authResponse)
            } else {
                // Error en la respuesta
                val errorMsg = response.errorBody()?.string() ?: "Error de autenticación"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     * @return Resultado que contiene los datos del usuario en caso de éxito
     */
    suspend fun register(username: String, email: String, password: String, fullName: String?): Result<AuthResponse> {
        return try {
            val response = apiService.register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password,
                    fullName = fullName
                )
            )
            
            if (response.isSuccessful && response.body() != null) {
                // Guardar datos de autenticación
                val authResponse = response.body()!!
                saveAuthData(authResponse)
                Result.success(authResponse)
            } else {
                // Error en la respuesta
                val errorMsg = response.errorBody()?.string() ?: "Error en el registro"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Cierra la sesión del usuario, eliminando todos los datos guardados
     */
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(USERNAME_KEY)
            preferences.remove(EMAIL_KEY)
        }
    }
    
    /**
     * Guarda los datos de autenticación en DataStore
     */
    private suspend fun saveAuthData(authResponse: AuthResponse) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = authResponse.token
            preferences[USERNAME_KEY] = authResponse.username
            preferences[EMAIL_KEY] = authResponse.email
        }
    }
}