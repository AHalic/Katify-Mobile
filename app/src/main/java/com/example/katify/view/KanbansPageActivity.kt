package com.example.katify.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.katify.data.User
import com.example.katify.databinding.ActivityKanbansPageBinding
import com.squareup.picasso.Picasso


class KanbansPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityKanbansPageBinding
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKanbansPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = getUserFromIntent()

        binding.emailProfile.text = user.userEmail
        binding.nameProfile.text = user.name

        Picasso.get().load(Uri.parse(user.urlImage)).into(binding.iconImage)
        Log.w("icon", user.urlImage)

        supportActionBar?.hide()
    }

    private fun getUserFromIntent(): User {
        return intent.getSerializableExtra("user") as User
    }
}