package com.example.inventario3.controller

import com.example.inventario3.model.User
import com.example.inventario3.model.dto.AuthResponse
import com.example.inventario3.model.dto.LoginRequest
import com.example.inventario3.model.dto.RegisterRequest
import com.example.inventario3.security.JwtService
import com.example.inventario3.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        // Autenticar las credenciales del usuario
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        
        SecurityContextHolder.getContext().authentication = authentication
        
        // Generar token JWT
        val userDetails = authentication.principal as UserDetails
        val jwt = jwtService.generateToken(userDetails)
        
        // Obtener detalles del usuario para la respuesta
        val user = userService.findByUsername(loginRequest.username).orElseThrow()
        
        val response = AuthResponse(
            token = jwt,
            username = user.username,
            email = user.email,
            roles = user.roles.map { it.name }
        )
        
        return ResponseEntity.ok(response)
    }
    
    @PostMapping("/register")
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<AuthResponse> {
        // Crear nuevo usuario
        val newUser = User(
            username = registerRequest.username,
            email = registerRequest.email,
            password = registerRequest.password,
            fullName = registerRequest.fullName
        )
        
        // Registrar el usuario en la base de datos
        val registeredUser = userService.registerUser(newUser)
        
        // Autenticar automáticamente al usuario
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(registerRequest.username, registerRequest.password)
        )
        
        SecurityContextHolder.getContext().authentication = authentication
        
        // Generar token JWT
        val userDetails = authentication.principal as UserDetails
        val jwt = jwtService.generateToken(userDetails)
        
        val response = AuthResponse(
            token = jwt,
            username = registeredUser.username,
            email = registeredUser.email,
            roles = registeredUser.roles.map { it.name }
        )
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}