package com.example.inventario3.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
class User(
    @Id
    val id: String = UUID.randomUUID().toString(),
    
    @Column(unique = true, nullable = false)
    var username: String,
    
    @Column(unique = true, nullable = false)
    var email: String,
    
    @Column(nullable = false)
    var password: String,
    
    @Column(name = "full_name")
    var fullName: String? = null,
    
    @Column(name = "is_active")
    var isActive: Boolean = true,
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf(),
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)