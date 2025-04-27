package com.example.inventario3.service

import com.example.inventario3.model.User
import com.example.inventario3.repository.RoleRepository
import com.example.inventario3.repository.UserRepository
import com.example.inventario3.security.SecurityConstants
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("Usuario no encontrado: $username") }

        val authorities = user.roles.map { 
            SimpleGrantedAuthority(it.name) 
        }

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            user.isActive,
            true,
            true,
            true,
            authorities
        )
    }

    @Transactional
    fun registerUser(user: User): User {
        if (userRepository.existsByUsername(user.username)) {
            throw IllegalArgumentException("El nombre de usuario ya existe")
        }
        
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalArgumentException("El correo electrónico ya está registrado")
        }

        // Asignar rol de usuario por defecto
        val userRole = roleRepository.findByName(SecurityConstants.ROLE_USER)
            .orElseThrow { RuntimeException("Rol no encontrado") }
        
        user.roles.add(userRole)
        user.password = passwordEncoder.encode(user.password)
        
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun findByUsername(username: String): Optional<User> {
        return userRepository.findByUsername(username)
    }
    
    @Transactional(readOnly = true)
    fun findById(id: String): Optional<User> {
        return userRepository.findById(id)
    }
    
    @Transactional
    fun updateUser(user: User): User {
        return userRepository.save(user)
    }
}