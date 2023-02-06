package com.example.katify.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.katify.R
import com.example.katify.data.model.Note
import com.example.katify.databinding.FragmentSelectedKanbanBinding
import com.example.katify.utils.Constants
import com.example.katify.view.adapter.NoteAdapter
import com.example.katify.view.listener.OnNoteListener
import com.example.katify.viewModel.KanbanViewModel
import com.example.katify.viewModel.NoteViewModel


/**
 *
 * Class that inflates the [R.layout.fragment_selected_kanban] layout
 *
 * This is the selected kanban fragment, where the kanbans notes are listed
 *
 * Inherits [Fragment] and implements [View.OnClickListener]
 *
 */
class SelectedKanbanFragment : Fragment(R.layout.fragment_selected_kanban), View.OnClickListener {
    private var _binding: FragmentSelectedKanbanBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteVM: NoteViewModel
    private lateinit var kanbanVM: KanbanViewModel
    private val args: SelectedKanbanFragmentArgs by navArgs()
    private lateinit var contextFrg: Context
    private val adapter = NoteAdapter()
    private lateinit var navController: NavController


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

        // Adds listener to update kanban name
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

            /**
             * OnSwiped delete kanban and its cards
             *
             * @param viewHolder
             * @param swipeDir
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //Remove swiped item from list and notify the RecyclerView
                val position = viewHolder.absoluteAdapterPosition
                noteVM.deleteNote(adapter.noteList[position])

                val id = requireArguments().getInt("kanban_id")
                noteVM.getAllNotesOfKanban(id)
                adapter.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.noteCardList)

        return binding.root
    }

    /**
     * Gets kanban id and sets listener to navigate to [NoteFragment] when card clicked
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gets this kanban id
        val id = requireArguments().getInt("kanban_id")
        noteVM.getAllNotesOfKanban(id)
        kanbanVM.getKanban(id)

        navController = findNavController()
        val listener = object : OnNoteListener {
            override fun onClick(note: Note) {

                val bundle = bundleOf("note" to note)
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

    /**
     * Gets activity to which the fragment its attached context
     */
    override fun onAttach(context: Context) {
        contextFrg = context
        super.onAttach(contextFrg)
    }

    /**
     * Sets observers to [noteVM] and [kanbanVM] message getters
     */
    private fun setObserver() {
        noteVM.getListMsg().observe(viewLifecycleOwner) {
            if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(contextFrg, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }

        noteVM.getIsAdded().observe(viewLifecycleOwner) {
            if (it == Constants.BD_MSGS.SUCCESS) {
                val id = requireArguments().getInt("kanban_id")
                noteVM.getAllNotesOfKanban(id)
            } else if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(contextFrg, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }

        noteVM.getNoteList().observe(viewLifecycleOwner) {
            adapter.updateNoteList(it)
        }

        kanbanVM.getKanbanName().observe(viewLifecycleOwner) {
            binding.kanbanNameInput.setText(it)
        }

        kanbanVM.getIsUpdated().observe(viewLifecycleOwner) {
            if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(contextFrg, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }

        noteVM.getDeleteMsg().observe(viewLifecycleOwner) {
            if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(contextFrg, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }

    }
}