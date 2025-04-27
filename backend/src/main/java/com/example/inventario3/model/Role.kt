package com.example.inventario3.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(unique = true, nullable = false, length = 50)
    val name: String,
    
    @Column(length = 255)
    val description: String? = null
)