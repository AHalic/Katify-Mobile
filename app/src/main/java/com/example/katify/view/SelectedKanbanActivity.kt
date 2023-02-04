package com.example.katify.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.katify.R
import com.example.katify.data.model.Kanban
import com.example.katify.databinding.ActivitySelectedKanbanBinding
import com.example.katify.viewModel.KanbanViewModel

class SelectedKanbanActivity : AppCompatActivity(), View.OnClickListener {
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

        val bundle = bundleOf("kanban_id" to kanban)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<SelectedKanbanFragment>(R.id.fragment_container_view, args=bundle)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController


        binding.backArrow.setOnClickListener(this)
    }

    override fun onClick (view: View) {
//        val lastAddedFragmentId = navController.currentDestination?.id ?: return

        if (view.id == R.id.back_arrow) {
            finish()
        }
    }

    private fun getKanbanFromIntent(): Int {
        return intent.getSerializableExtra("kanban") as Int
    }
}