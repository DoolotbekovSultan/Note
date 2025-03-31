package com.sultan.note.ui.fragments.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sultan.note.App
import com.sultan.note.R
import com.sultan.note.databinding.FragmentSelectionColorBinding
import com.sultan.note.ui.interfaces.OnColorSelectedListener

class SelectionColorFragment : Fragment() {

    private lateinit var binding : FragmentSelectionColorBinding

    private val colors = arrayOf(R.color.yellow, R.color.violet, R.color.ping,
                                 R.color.red, R.color.green, R.color.blue)

    private val inactiveBackgrounds = arrayOf(R.drawable.yellow_color_background, R.drawable.violet_color_background, R.drawable.ping_color_background,
                                                R.drawable.red_color_background, R.drawable.green_color_background, R.drawable.blue_color_background)

    private val activeBackgrounds = arrayOf(R.drawable.active_yellow_color_background, R.drawable.active_violet_color_background, R.drawable.active_ping_color_background,
        R.drawable.active_red_color_background, R.drawable.active_green_color_background, R.drawable.active_blue_color_background)

    private var active = 0

    private var onColorSelectedListener : OnColorSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnColorSelectedListener) {
            onColorSelectedListener = parentFragment as OnColorSelectedListener
        } else {
            throw RuntimeException("$context must implement OnColorSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onColorSelectedListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectionColorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = arguments?.getInt("noteId") ?: -1

        if (noteId == -1) {
            active = 0
        } else {
            val note = App.appDatabase?.noteDao()?.getById(noteId)
            val activeColor = note?.color
            active = colors.indexOf(activeColor)
            if (active == -1) {
                active = 0
            }
        }

        for (i in 0 until binding.colors.childCount) {
            val view = binding.colors.getChildAt(i)
            if (i == active) {
                view.setBackgroundResource(activeBackgrounds[i])
            }
            view.setOnClickListener {
                updateActive(i)
            }
        }
    }

    private fun updateActive(newActive : Int) {
        if (newActive < 0 || newActive >= colors.size) return
        val oldActiveColor = binding.colors.getChildAt(active)
        oldActiveColor.setBackgroundResource(inactiveBackgrounds[active])
        active = newActive
        onColorSelectedListener?.onColorSelected(colors[active])
        val newActiveColor = binding.colors.getChildAt(active)
        newActiveColor.setBackgroundResource(activeBackgrounds[active])
    }
}