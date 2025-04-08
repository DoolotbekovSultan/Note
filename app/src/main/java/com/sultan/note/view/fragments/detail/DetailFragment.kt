package com.sultan.note.view.fragments.detail

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
import com.sultan.note.model.data.models.Note
import com.sultan.note.databinding.FragmentDetailBinding
import com.sultan.note.utils.Date

class DetailFragment : Fragment()
{

    private lateinit var binding : FragmentDetailBinding
    private val currentDate = Date()
    private var noteId = -1
    private lateinit var note : Note
    private var currentColor = R.color.yellow
    private lateinit var dateString : String
    private var showSelectionColor = false
    private var activeColor : Int = 0

    private val colors = arrayOf(R.color.yellow, R.color.violet, R.color.ping,
        R.color.red, R.color.green, R.color.blue)

    private val inactiveBackgrounds = arrayOf(R.drawable.yellow_color_background, R.drawable.violet_color_background, R.drawable.ping_color_background,
        R.drawable.red_color_background, R.drawable.green_color_background, R.drawable.blue_color_background)

    private val activeBackgrounds = arrayOf(R.drawable.active_yellow_color_background, R.drawable.active_violet_color_background, R.drawable.active_ping_color_background,
        R.drawable.active_red_color_background, R.drawable.active_green_color_background, R.drawable.active_blue_color_background)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.readyTextView.isVisible = isCorrectNote()
        initialize()
        setupListeners()
    }

    private fun initialize() {
        arguments?.let { args ->
            noteId = args.getInt("noteId")
        }
        dateString = "${currentDate.dayOfMonth} ${getString(currentDate.monthStringRes)} ${currentDate.hour}:${String.format("%02d", currentDate.minute)}"
        note = App.appDatabase?.noteDao()?.getById(noteId) ?: Note("", "", dateString, R.color.yellow)
        currentColor = note.color
        activeColor = colors.indexOf(currentColor)
        for (i in 0 until binding.colors.childCount) {
            val view = binding.colors.getChildAt(i)
            view.setOnClickListener {
                updateActive(i)
            }
        }
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

        for (i in 0 until binding.colors.childCount) {
            val view = binding.colors.getChildAt(i)
            if (i == activeColor) {
                view.setBackgroundResource(activeBackgrounds[i])
            }
            view.setOnClickListener {
                updateActive(i)
            }
        }

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

    private fun updateActive(newActive : Int) {
        if (newActive < 0 || newActive >= colors.size) return
        val oldActiveColor = binding.colors.getChildAt(activeColor)
        oldActiveColor.setBackgroundResource(inactiveBackgrounds[activeColor])
        activeColor = newActive
        currentColor = colors[activeColor]
        val newActiveColor = binding.colors.getChildAt(activeColor)
        newActiveColor.setBackgroundResource(activeBackgrounds[activeColor])
    }
}