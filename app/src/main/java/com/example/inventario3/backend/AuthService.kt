package com.example.inventario3.backend

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Service to handle authentication operations like login and registration
 */
class AuthService {
    // In a real app, this would be replaced with a database or API call
    private val mockUsers = mutableMapOf(
        "admin" to User(
            id = UUID.randomUUID().toString(),
            username = "admin",
            email = "admin@example.com",
            password = "admin123",
            fullName = "Administrator"
        ),
        "user" to User(
            id = UUID.randomUUID().toString(),
            username = "user",
            email = "user@example.com",
            password = "user123",
            fullName = "Regular User"
        )
    )

    /**
     * Attempts to login a user with the provided credentials
     * @return Result containing User on success or error message on failure
     */
    suspend fun login(username: String, password: String): Result<User> {
        // Simulate network delay
        return withContext(Dispatchers.IO) {
            Thread.sleep(1000) // Simulate network delay
            
            val user = mockUsers[username]
            when {
                user == null -> Result.failure(Exception("User not found"))
                user.password != password -> Result.failure(Exception("Invalid password"))
                else -> Result.success(user)
            }
        }
    }

    /**
     * Register a new user
     * @return Result containing the new User on success or error message on failure
     */
    suspend fun register(username: String, email: String, password: String, fullName: String): Result<User> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(1000) // Simulate network delay
            
            if (mockUsers.containsKey(username)) {
                Result.failure(Exception("Username already exists"))
            } else {
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    username = username,
                    email = email,
                    password = password,
                    fullName = fullName
                )
                mockUsers[username] = newUser
                Result.success(newUser)
            }
        }
    }
}