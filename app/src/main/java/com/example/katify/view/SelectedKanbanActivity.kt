package com.example.katify.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.katify.R
import com.example.katify.data.model.Kanban
import com.example.katify.databinding.ActivitySelectedKanbanBinding

class SelectedKanbanActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySelectedKanbanBinding;
    private var kanban : Int = -1
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedKanbanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kanban = getKanbanFromIntent()
        Toast.makeText(this, "$kanban", Toast.LENGTH_SHORT).show()
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun getKanbanFromIntent(): Int {
        return intent.getSerializableExtra("kanban") as Int
    }
}