package com.example.inventario3.backend

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton to manage the current user session throughout the app
 */
object UserSession {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    val isLoggedIn: Boolean
        get() = _currentUser.value != null

    fun login(user: User) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }
}