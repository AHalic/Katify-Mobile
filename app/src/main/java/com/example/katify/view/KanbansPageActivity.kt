package com.example.katify.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.katify.R
import com.example.katify.data.model.Kanban
import com.example.katify.data.model.User
import com.example.katify.databinding.ActivityKanbansPageBinding
import com.example.katify.utils.Constants
import com.example.katify.view.adapter.KanbanAdapter
import com.example.katify.view.listener.OnKanbanListener
import com.example.katify.viewModel.KanbanViewModel
import com.squareup.picasso.Picasso


class KanbansPageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityKanbansPageBinding
    private lateinit var user : User
    lateinit var kanbanVM: KanbanViewModel
    private val adapter = KanbanAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKanbansPageBinding.inflate(layoutInflater)
        kanbanVM = ViewModelProvider(this)[KanbanViewModel::class.java]
        setContentView(binding.root)
        supportActionBar?.hide()

        user = getUserFromIntent()

        // Defines user info
        binding.emailProfile.text = user.userEmail
        binding.nameProfile.text = user.name

        // Gets user's google pic
        Picasso.get().load(Uri.parse(user.urlImage)).into(binding.iconImage)

        // Recycler View config
        binding.kanbansRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.kanbansRecyclerView.adapter = adapter

        val listener = object : OnKanbanListener {
            override fun onClick(kanban: Kanban) {
                goToSelectedKanbanActivity(kanban)
            }
        }
        adapter.setListener(listener)
        setObserver()

        binding.addKanbanBtn.setOnClickListener(this)

        // Defines swipe card to delete callback
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // Remove swiped item from list and all its related and notify the RecyclerView
                val position = viewHolder.absoluteAdapterPosition
                kanbanVM.deleteKanbanAndNotes(adapter.kanbanList[position])
                kanbanVM.getAllKanbansOfOwner(user.userId)
                adapter.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.kanbansRecyclerView)

    }

    override fun onStart() {
        super.onStart()

        kanbanVM.getAllKanbansOfOwner(user.userId)

    }

    override fun onClick(view: View) {
        if (view.id == R.id.add_kanban_btn) {
            kanbanVM.addKanban(user.userId, resources.getString(R.string.kanban_name_default))
        }
    }

    private fun getUserFromIntent(): User {
        return intent.getSerializableExtra("user") as User
    }

    private fun goToSelectedKanbanActivity(kanban: Kanban) {
        val intent = Intent(this, SelectedKanbanActivity::class.java)
        intent.putExtra("kanban", kanban.id)
        startActivity(intent)
    }

    private fun setObserver() {
        kanbanVM.getListMsg().observe(this) {
            if (it == Constants.BD_MSGS.NOT_FOUND) {
                Toast.makeText(this, R.string.not_found_search, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.FAIL) {
                Toast.makeText(this, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }

        kanbanVM.getKanbanList().observe(this) {
            adapter.updateKanbanList(it)
        }

        kanbanVM.getIsAdded().observe(this) {
            if (it == Constants.BD_MSGS.SUCCESS) {
                kanbanVM.getAllKanbansOfOwner(user.userId)
            } else if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(this, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }

        kanbanVM.getDeleteMsg().observe(this) {
            if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(this, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }
    }
}