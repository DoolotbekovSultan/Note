package com.sultan.note.ui.fragments.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sultan.note.R
import com.sultan.note.databinding.FragmentOnboardBinding
import com.sultan.note.ui.activity.MainActivity
import com.sultan.note.ui.adapters.OnboardPageAdapter
import com.sultan.note.ui.models.OnboardPage
import com.sultan.note.utils.Preference

class OnboardFragment : Fragment() {

    private lateinit var binding : FragmentOnboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
    }

    private fun initialize() {
        val onboardPageAdapter = OnboardPageAdapter(this@OnboardFragment, generateOnBoardPages());
        binding.viewPager.adapter = onboardPageAdapter
    }

    private fun generateOnBoardPages() : ArrayList<OnboardPage> = arrayListOf(
        OnboardPage(
            R.raw.convenience_animation,
            "Удобство",
            "Создавайте заметки в два клика! Записывайте мысли, идеи и важные задачи мгновенно."
        ),
        OnboardPage(
            R.raw.organization_animation,
            "Организация",
            "Организуйте заметки по папкам и тегам. Легко находите нужную информацию в любое время."
        ),
        OnboardPage(
            R.raw.synchronization_animation,
            "Синхронизация",
            "Синхронизация на всех устройствах. Доступ к записям в любое время и в любом месте."
        )
    )

    private fun setupListeners() = with(binding) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeActiveOnboardShower(position)
                skip.visibility = if (position != 2) View.VISIBLE else View.INVISIBLE
                startButton.isVisible = position == 2
            }
        })
        skip.setOnClickListener {
            toNotesFragment()
        }
        startButton.setOnClickListener {
            toNotesFragment()
        }
    }

    private fun changeActiveOnboardShower(position : Int) {
        val onboardShowers = binding.onboardShowers;
        for (i in 0 until onboardShowers.childCount) {
            val onboardShower = onboardShowers.getChildAt(i)
            if (i == position) {
                onboardShower.setBackgroundResource(R.drawable.active_onboard_shower_circle)
            } else {
                onboardShower.setBackgroundResource(R.drawable.onboard_shower_circle)
            }
        }
    }

    private fun toNotesFragment() {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.preference.isFirstVisit = false
        findNavController().navigate(R.id.action_onboardFragment_to_notesFragment)
    }
}