package com.example.katify.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.katify.R
import com.example.katify.databinding.FragmentNoteBinding
import com.example.katify.databinding.FragmentSelectedKanbanBinding
import com.example.katify.databinding.NoteContentViewBinding
import com.example.katify.view.adapter.NoteAdapter
import com.example.katify.viewModel.KanbanViewModel
import com.example.katify.viewModel.NoteViewModel

class NoteFragment : Fragment(R.layout.fragment_note), View.OnClickListener {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteVM: NoteViewModel
    val args: NoteFragmentArgs by navArgs()
    private lateinit var contextFrg: Context
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        noteVM = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        return binding.root
    }

    override fun onClick(view: View) {
        if (view.id == R.id.back_arrow) {
            findNavController().popBackStack()
        }
    }

    override fun onAttach(context: Context) {
        contextFrg = context
        super.onAttach(contextFrg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}