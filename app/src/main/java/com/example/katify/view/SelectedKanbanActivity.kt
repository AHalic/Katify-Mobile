package com.example.katify.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.katify.R
import com.example.katify.databinding.ActivitySelectedKanbanBinding

/**
 *
 * Class that inflates the [R.layout.activity_selected_kanban] layout
 *
 * This is the activity on which the fragments [NoteFragment]
 * and [SelectedKanbanFragment] are attached
 *
 * Inherits [AppCompatActivity] and implements [View.OnClickListener]
 *
 */
class SelectedKanbanActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivitySelectedKanbanBinding
    private var kanban : Int = -1
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedKanbanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kanban = getKanbanFromIntent()
        supportActionBar?.hide()

        val bundle = bundleOf("kanban_id" to kanban)

        // Navigates to selected fragment
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<SelectedKanbanFragment>(R.id.fragment_container_view, args=bundle)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.backArrow.setOnClickListener(this)
    }

    override fun onClick (view: View) {
        if (view.id == R.id.back_arrow) {
            finish()
        }
    }

    /**
     * Gets id of the kanban selected from the intent that started this activity
     *
     * @return kanban id
     */
    private fun getKanbanFromIntent(): Int {
        return intent.getSerializableExtra("kanban") as Int
    }
}