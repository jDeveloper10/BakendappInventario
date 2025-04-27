package com.example.inventario3.config

import com.example.inventario3.model.Role
import com.example.inventario3.model.User
import com.example.inventario3.repository.RoleRepository
import com.example.inventario3.repository.UserRepository
import com.example.inventario3.security.SecurityConstants
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer {

    /**
     * Inicializa datos básicos para la aplicación
     * Crea los roles y un usuario administrador para entornos que no sean de producción
     */
    @Bean
    @Profile("!prod") // No ejecutar en producción
    fun initData(
        roleRepository: RoleRepository, 
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner {
        return CommandLineRunner {
            // Crear roles si no existen
            if (roleRepository.count() == 0L) {
                val userRole = Role(name = SecurityConstants.ROLE_USER, description = "Usuario estándar")
                val adminRole = Role(name = SecurityConstants.ROLE_ADMIN, description = "Administrador con todos los privilegios")
                
                roleRepository.save(userRole)
                roleRepository.save(adminRole)
                
                println("Roles inicializados correctamente")
                
                // Crear usuario administrador para pruebas
                if (userRepository.count() == 0L) {
                    val adminUser = User(
                        username = "admin",
                        email = "admin@inventario.com",
                        password = passwordEncoder.encode("admin123"),
                        fullName = "Administrador",
                        roles = mutableSetOf(adminRole)
                    )
                    
                    val testUser = User(
                        username = "usuario",
                        email = "usuario@inventario.com",
                        password = passwordEncoder.encode("usuario123"),
                        fullName = "Usuario de Prueba",
                        roles = mutableSetOf(userRole)
                    )
                    
                    userRepository.save(adminUser)
                    userRepository.save(testUser)
                    
                    println("Usuarios de prueba creados correctamente")
                }
            }
        }
    }
}