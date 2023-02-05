package com.example.katify.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var listVM: KanbanViewModel
    private val adapter = KanbanAdapter()
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKanbansPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = getUserFromIntent()

        // Defines user info
        binding.emailProfile.text = user.userEmail
        binding.nameProfile.text = user.name

        // Gets user's google pic
        Picasso.get().load(Uri.parse(user.urlImage)).into(binding.iconImage)
        Log.w("icon", user.urlImage)

        // Recycler View config
        binding.kanbansRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.kanbansRecyclerView.adapter = adapter

        val listener = object : OnKanbanListener {
            override fun onClick(kanban: Kanban) {
                goToSelectedKanbanActivity(kanban)
            }
        }
        adapter.setListener(listener)


        listVM = ViewModelProvider(this).get(KanbanViewModel::class.java)

        supportActionBar?.hide()

        setObserver()

        binding.addKanbanBtn.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()

        listVM.getAllKanbansOfOwner(user.userId)

    }

    override fun onClick(view: View) {
        if (view.id == R.id.add_kanban_btn) {
            listVM.addKanban(user.userId, resources.getString(R.string.kanban_name_default))
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
        listVM.getListMsg().observe(this, Observer {
            if (it == Constants.BD_MSGS.NOT_FOUND){
                Toast.makeText(this, R.string.not_found_search, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.FAIL) {
                Toast.makeText(this, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        })

        listVM.getKanbanList().observe(this, Observer {
            adapter.updateKanbanList(it)
        })

        listVM.getIsAdded().observe(this, Observer {
            if (it == Constants.BD_MSGS.SUCCESS){
                listVM.getAllKanbansOfOwner(user.userId)
            } else if (it == Constants.BD_MSGS.CONSTRAINT){
                Toast.makeText(this, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        })

    }
}