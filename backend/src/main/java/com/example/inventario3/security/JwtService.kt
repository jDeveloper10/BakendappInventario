package com.example.inventario3.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService {
    
    @Value("\${jwt.secret}")
    private lateinit var secretString: String
    
    @Value("\${jwt.expiration}")
    private val jwtExpiration: Long = 0
    
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretString.toByteArray())
    }
    
    fun generateToken(userDetails: UserDetails): String {
        val now = System.currentTimeMillis()
        return Jwts.builder()
            .setSubject(userDetails.username)
            .claim("authorities", userDetails.authorities)
            .setIssuedAt(Date(now))
            .setExpiration(Date(now + jwtExpiration))
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }
    
    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }
    
    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }
    
    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }
    
    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }
    
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}