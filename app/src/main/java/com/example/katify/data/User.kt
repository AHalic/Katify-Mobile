package com.example.katify.data

import java.io.Serializable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
        val userId: String,
        val name: String,
        val urlImage: String,
        val userEmail: String,
        var isAuthenticated : Boolean = false,
        var isNew : Boolean = false,
        var isCreated : Boolean = false
) : Serializable
