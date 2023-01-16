package com.example.katify.data

import android.provider.ContactsContract.CommonDataKinds.Email
import java.net.URL

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
        val userId: String,
        val displayName: String,
        val urlImage: URL,
        val userEmail: Email
)
