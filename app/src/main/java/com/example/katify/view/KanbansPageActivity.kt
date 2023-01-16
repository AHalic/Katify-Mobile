package com.example.katify.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.katify.databinding.ActivityKanbansPageBinding

class KanbansPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityKanbansPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKanbansPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}