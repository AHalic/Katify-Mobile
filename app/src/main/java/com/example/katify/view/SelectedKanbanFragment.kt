package com.example.katify.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.katify.R
import com.example.katify.data.model.Note
import com.example.katify.databinding.FragmentSelectedKanbanBinding
import com.example.katify.utils.Constants
import com.example.katify.view.adapter.NoteAdapter
import com.example.katify.view.listener.OnNoteListener
import com.example.katify.viewModel.KanbanViewModel
import com.example.katify.viewModel.NoteViewModel
import javax.security.auth.callback.Callback


/**
 * A simple [Fragment] subclass.
 * Use the [SelectedKanbanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedKanbanFragment : Fragment(R.layout.fragment_selected_kanban), View.OnClickListener {
    private var _binding: FragmentSelectedKanbanBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteVM: NoteViewModel
    private lateinit var kanbanVM: KanbanViewModel
    val args: SelectedKanbanFragmentArgs by navArgs()
    private lateinit var contextFrg: Context
    private val adapter = NoteAdapter()
    lateinit var navController: NavController
    private lateinit var kanbanName: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentSelectedKanbanBinding.inflate(inflater, container, false)
        noteVM = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        kanbanVM = ViewModelProvider(requireActivity())[KanbanViewModel::class.java]

        // Recycler View config
        binding.noteCardList.layoutManager = LinearLayoutManager(contextFrg)
        binding.noteCardList.adapter = adapter

        setObserver()

        binding.addNoteBtn.setOnClickListener(this)

        binding.kanbanNameInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val id = requireArguments().getInt("kanban_id")
                kanbanVM.updateKanban(id, s.toString())
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getInt("kanban_id")
        noteVM.getAllNotesOfKanban(id)

        kanbanVM.getKanban(id)

        navController = findNavController()
        val listener = object : OnNoteListener {
            override fun onClick(note: Note) {

                val bundle = bundleOf("note_id" to note.note_id)
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<NoteFragment>(R.id.fragment_container_view, args=bundle)
                }
            }
        }
        adapter.setListener(listener)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.add_note_btn) {
            noteVM.addNote(args.kanbanId, resources.getString(R.string.new_note_default))
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
                val id = requireArguments().getInt("kanban_id")
                noteVM.getAllNotesOfKanban(id)
            } else if (it == Constants.BD_MSGS.CONSTRAINT){
                Toast.makeText(contextFrg, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        })

        noteVM.getNoteList().observe(viewLifecycleOwner, Observer {
            adapter.updateNoteList(it)
        })

        kanbanVM.getKanbanName().observe(viewLifecycleOwner, Observer {
            binding.kanbanNameInput.setText(it)
        })

    }
}