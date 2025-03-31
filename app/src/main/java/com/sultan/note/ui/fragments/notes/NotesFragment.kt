package com.sultan.note.ui.fragments.notes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sultan.note.App
import com.sultan.note.R
import com.sultan.note.data.models.Note
import com.sultan.note.databinding.FragmentNotesBinding
import com.sultan.note.ui.adapters.NoteAdapter
import com.sultan.note.ui.fragments.detail.DetailFragment
import com.sultan.note.ui.interfaces.OnClickNote
import com.sultan.note.utils.Date
import com.sultan.note.utils.Preference

class NotesFragment : Fragment(), OnClickNote {

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
        noteAdapter = NoteAdapter(preference.layoutManager, this)

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
        addNoteImageButton.setOnClickListener {
            findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToDetailFragment(
                    -1
                )
            )
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

    @SuppressLint("MissingInflatedId")
    override fun onLongClick(note: Note) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.note_delete_note_alert_dialog, null)
        val alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val negativeButton = dialogView.findViewById<Button>(R.id.negativeButton)
        val positiveButton = dialogView.findViewById<Button>(R.id.positiveButton)

        negativeButton.setOnClickListener {
            alertDialog.dismiss()
        }
        positiveButton.setOnClickListener {
            App.appDatabase?.noteDao()?.delete(note)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onClick(note: Note) {
        findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToDetailFragment(note.id))
    }
}