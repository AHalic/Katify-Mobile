package com.example.katify.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.katify.R
import com.example.katify.databinding.FragmentNoteBinding
import com.example.katify.utils.Constants
import com.example.katify.viewModel.NoteViewModel

/**
 *
 * Class that inflates the [R.layout.fragment_note] layout
 *
 * This is the note fragment, where it's description and name are shown
 *
 * Inherits [Fragment]
 *
 */
class NoteFragment : Fragment(R.layout.fragment_note) {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteVM: NoteViewModel
    val args: NoteFragmentArgs by navArgs()
    private lateinit var contextFrg: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        container?.removeAllViews()
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        noteVM = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        setObserver()

        binding.noteText.setText(args.note.content)
        binding.noteNameInput.setText(args.note.note_name)

        // Defines de note name on change listener to update it on the db
        binding.noteNameInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                noteVM.updateNoteName(args.note.note_id, s.toString())
            }
        })

        // Defines de note description on change listener to update it on the db
        binding.noteText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                noteVM.updateNoteDesc(args.note.note_id, s.toString())
            }
        })

        return binding.root
    }

    /**
     * Gets activity to which the fragment its attached context
     */
    override fun onAttach(context: Context) {
        contextFrg = context
        super.onAttach(contextFrg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Sets observers to [noteVM] message getters
     */
    private fun setObserver() {
        noteVM.getIsDescUpdated().observe(viewLifecycleOwner) {
            if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(contextFrg, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }

        noteVM.getIsNameUpdated().observe(viewLifecycleOwner) {
            if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(contextFrg, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        }

    }
}