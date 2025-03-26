package com.sultan.note.ui.fragments.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sultan.note.App
import com.sultan.note.R
import com.sultan.note.databinding.FragmentNotesBinding
import com.sultan.note.ui.adapters.NoteAdapter
import com.sultan.note.utils.Preference

class NotesFragment : Fragment() {

    private lateinit var binding : FragmentNotesBinding
    private val preference = Preference()
    private lateinit var noteAdapter : NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
        getData()
    }

    private fun initialize() {
        preference.unit(requireContext())
        noteAdapter = NoteAdapter(preference.layoutManager)

        binding.notesRecyclerView.apply {
            if (preference.layoutManager == NoteAdapter.VIEW_TYPE_LINEAR) {
                layoutManager = LinearLayoutManager(requireContext())
                binding.changeLayoutManagerImageView.setImageResource(R.drawable.ic_grid_layout)
            } else {
                layoutManager = GridLayoutManager(requireContext(), 2)
                binding.changeLayoutManagerImageView.setImageResource(R.drawable.ic_linear_layout)
            }
            adapter = noteAdapter
        }
    }

    private fun setupListeners() = with(binding) {
        addNoteImageButton.setOnClickListener{
            findNavController().navigate(R.id.action_notesFragment_to_detailFragment)
        }
        changeLayoutManagerImageView.setOnClickListener {
            if (noteAdapter.viewType == NoteAdapter.VIEW_TYPE_LINEAR) {
                binding.notesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                noteAdapter.updateViewType(NoteAdapter.VIEW_TYPE_GRID)
                preference.layoutManager = NoteAdapter.VIEW_TYPE_GRID
                binding.changeLayoutManagerImageView.setImageResource(R.drawable.ic_linear_layout)
            } else {
                binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                noteAdapter.updateViewType(NoteAdapter.VIEW_TYPE_LINEAR)
                preference.layoutManager = NoteAdapter.VIEW_TYPE_LINEAR
                binding.changeLayoutManagerImageView.setImageResource(R.drawable.ic_grid_layout)
            }
        }
    }

    private fun getData() {
        App.appDatabase?.noteDao()?.getAll()?.observe(viewLifecycleOwner){ notes ->
            binding.emptyNotesTextView.isVisible = notes.isEmpty()
            noteAdapter.submitList(notes)
        }
    }
}