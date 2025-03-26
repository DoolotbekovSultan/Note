package com.sultan.note.ui.fragments.detail

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
import com.sultan.note.data.models.Note
import com.sultan.note.databinding.FragmentDetailBinding
import com.sultan.note.utils.Date

class DetailFragment : Fragment() {

    private lateinit var binding : FragmentDetailBinding
    private val currentDate = Date()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
    }

    private fun initialize() {
        setDate()
    }

    private fun setDate() = with(binding) {
        dateDayOfMonthTextView.text = currentDate.dayOfMonth.toString()
        dateMonthTextView.text = getString(currentDate.monthStringRes)
        dateHourTextView.text = currentDate.hour.toString()
        dateMinuteTextView.text = currentDate.minute.toString()
    }

    private fun setupListeners() = with(binding) {
        readyTextView.setOnClickListener {
            val note = createNote()
            App.appDatabase?.noteDao()?.insert(note)
            findNavController().navigateUp()
        }
        backImageView.setOnClickListener{
            findNavController().navigateUp()
        }

        overflowMenu.setOnClickListener {
            // TODO overflow menu on click logic
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

        titleEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT && titleEditText.text.toString() != "") {
                textEditText.requestFocus()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener true
        }

        textEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN ) {
                if (keyCode == KeyEvent.KEYCODE_DEL && textEditText.text.isEmpty()) {
                    titleEditText.requestFocus()
                    return@setOnKeyListener true
                }
            }
            false
        }
    }

    private fun isCorrectNote() : Boolean {
        val title = binding.titleEditText.text.toString()
        val text = binding.textEditText.text.toString()
        return title.isNotEmpty() && text.isNotEmpty()
    }

    private fun createNote() : Note {
        val title = binding.titleEditText.text.toString()
        val text = binding.textEditText.text.toString()
        val date = "${currentDate.dayOfMonth} ${getString(currentDate.monthStringRes)} ${currentDate.hour}:${currentDate.minute}"
        return Note(title, text, date)
    }
}