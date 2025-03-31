package com.sultan.note.ui.fragments.detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.sultan.note.App
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import com.sultan.note.R
import com.sultan.note.data.models.Note
import com.sultan.note.databinding.FragmentDetailBinding
import com.sultan.note.utils.Date
import com.sultan.note.ui.interfaces.OnColorSelectedListener

class DetailFragment : Fragment(), OnColorSelectedListener {

    private lateinit var binding : FragmentDetailBinding
    private val currentDate = Date()
    private var noteId = -1
    private lateinit var note : Note
    private var currentColor = R.color.yellow
    private lateinit var dateString : String
    private var showSelectionColor = false
    private lateinit var selectionColorFragment : Fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { args ->
            noteId = args.getInt("noteId")
        }
        dateString = "${currentDate.dayOfMonth} ${getString(currentDate.monthStringRes)} ${currentDate.hour}:${String.format("%02d", currentDate.minute)}"
        note = App.appDatabase?.noteDao()?.getById(noteId) ?: Note("", "", dateString, R.color.yellow)
        currentColor = note.color
        initialize()
        setupListeners()
    }

    private fun initialize() {
        selectionColorFragment = SelectionColorFragment()
        val bundle = Bundle()
        bundle.putInt("noteId", note.id)
        selectionColorFragment.arguments = bundle
        childFragmentManager.beginTransaction().replace(R.id.colorSelection, selectionColorFragment).commit()
        binding.titleEditText.setText(note.title)
        binding.textEditText.setText(note.text)
        setDate()
    }

    private fun setDate() = with(binding) {
        dateDayOfMonthTextView.text = currentDate.dayOfMonth.toString()
        dateMonthTextView.text = getString(currentDate.monthStringRes)
        dateHourTextView.text = currentDate.hour.toString()
        dateMinuteTextView.text = String.format("%02d", currentDate.minute)
    }

    private fun setupListeners() = with(binding) {

        changeColor.setOnClickListener {

            if (showSelectionColor) {
                changeColor.setImageResource(R.drawable.active_overflow_menu)
                colorSelection.visibility = View.GONE
                showSelectionColor = false
            } else {
                changeColor.setImageResource(R.drawable.overflow_menu)
                colorSelection.visibility = View.VISIBLE
                showSelectionColor = true
            }

        }
        readyTextView.setOnClickListener {
            val newNote = createNote()
            if (noteId == -1) {
                App.appDatabase?.noteDao()?.insert(newNote)
            } else {
                newNote.id = noteId
                App.appDatabase?.noteDao()?.replace(newNote)
            }
            findNavController().navigateUp()
        }
        backImageView.setOnClickListener{
            findNavController().navigateUp()
        }

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                readyTextView.isVisible = isCorrectNote()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                readyTextView.isVisible = isCorrectNote()
            }

            override fun afterTextChanged(s: Editable?) {
                readyTextView.isVisible = isCorrectNote()
            }
        })

        textEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                readyTextView.isVisible = isCorrectNote()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                readyTextView.isVisible = isCorrectNote()
            }

            override fun afterTextChanged(s: Editable?) {
                readyTextView.isVisible = isCorrectNote()
            }
        })

        titleEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT && titleEditText.text.trim().isNotEmpty()) {
                textEditText.requestFocus()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener true
        }

        textEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN ) {
                if (keyCode == KeyEvent.KEYCODE_DEL && textEditText.text.trim().isEmpty()) {
                    titleEditText.requestFocus()
                    return@setOnKeyListener true
                }
            }
            false
        }
        textEditText.setOnFocusChangeListener {_, hasFocus ->
            if (hasFocus && titleEditText.text.isEmpty()) {
                titleEditText.requestFocus()
                showKeyboard(titleEditText)
            }
        }
    }

    private fun showKeyboard(view : View) {
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun isCorrectNote() : Boolean {
        val title = binding.titleEditText.text.toString()
        val text = binding.textEditText.text.toString()
        return title.isNotEmpty() && text.isNotEmpty()
    }

    private fun createNote() : Note {
        val title = binding.titleEditText.text.toString()
        val text = binding.textEditText.text.toString()
        return Note(title, text, dateString, currentColor)
    }

    override fun onColorSelected(color: Int) {
        currentColor = color
    }
}