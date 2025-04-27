package com.example.inventario3.backend

data class User(
    val id: String,
    val username: String,
    val email: String,
    val password: String,  // In a real app, this would be hashed
    val fullName: String = ""
)