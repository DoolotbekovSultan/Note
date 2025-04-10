package com.sultan.note.view.fragments.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sultan.note.App
import com.sultan.note.R
import com.sultan.note.model.data.models.Note
import com.sultan.note.databinding.FragmentNotesBinding
import com.sultan.note.presenter.notes.NotesContract
import com.sultan.note.presenter.notes.NotesPresenter
import com.sultan.note.view.adapters.NoteAdapter
import com.sultan.note.view.interfaces.OnClickNote
import com.sultan.note.utils.Preference

class NotesFragment : Fragment(), OnClickNote, NotesContract.View {

    private lateinit var binding : FragmentNotesBinding
    private val preference = Preference()
    private lateinit var noteAdapter : NoteAdapter
    private val presenter = NotesPresenter(this)

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
        presenter.loadNotes()
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

        menuImageButton.setOnClickListener {
            val drawer = activity?.findViewById<DrawerLayout>(R.id.drawer)
            drawer?.openDrawer(GravityCompat.START)
        }

        alertDialogScreen.setOnClickListener {
            // EMPTY
        }
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

    override fun onLongClick(note: Note) {

        binding.alertDialogScreen.visibility = View.VISIBLE

        binding.negativeButton.setOnClickListener {
            binding.alertDialogScreen.visibility = View.GONE
        }
        binding.positiveButton.setOnClickListener {
            presenter.deleteNote(note)
            binding.alertDialogScreen.visibility = View.GONE
        }

    }

    override fun onClick(note: Note) {
        findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToDetailFragment(note.id))
    }

    override fun showNotes(notes: List<Note>) {
        binding.emptyNotesTextView.isVisible = notes.isEmpty()
        noteAdapter.submitList(notes)
    }

    override fun showError() {
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
    }
}