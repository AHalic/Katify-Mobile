package com.example.katify.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.katify.R
import com.example.katify.databinding.FragmentSelectedKanbanBinding
import com.example.katify.utils.Constants
import com.example.katify.viewModel.NoteViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [SelectedKanbanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedKanbanFragment : Fragment(R.layout.fragment_selected_kanban), View.OnClickListener {
    private var _binding: FragmentSelectedKanbanBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteVM: NoteViewModel
    val args: SelectedKanbanFragmentArgs by navArgs()
    private lateinit var contextFrg: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentSelectedKanbanBinding.inflate(inflater, container, false)
        noteVM = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        setObserver()
        binding.addNoteBtn.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(view: View) {
        if (view.id == R.id.add_note_btn) {
            noteVM.addNote(args.kanbanId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onAttach(context: Context) {
        contextFrg = context
        super.onAttach(contextFrg)
    }

    private fun setObserver() {
        noteVM.getIsAdded().observe(viewLifecycleOwner, Observer {
            if (it == Constants.BD_MSGS.SUCCESS){
                Toast.makeText(contextFrg, R.string.success_create, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.CONSTRAINT){
                Toast.makeText(contextFrg, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        })
    }
}