package com.example.katify.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.katify.databinding.ActivitySelectedKanbanBinding

class SelectedKanbanActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySelectedKanbanBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedKanbanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}