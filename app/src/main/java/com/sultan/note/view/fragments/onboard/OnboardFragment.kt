package com.sultan.note.view.fragments.onboard

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
import com.sultan.note.view.adapters.OnboardPageAdapter
import com.sultan.note.model.models.OnboardPage
import com.sultan.note.utils.Preference

class OnboardFragment : Fragment() {

    private lateinit var binding : FragmentOnboardBinding
    private val preference = Preference()

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
        preference.unit(requireActivity())
        val onboardPageAdapter = OnboardPageAdapter(this@OnboardFragment, generateOnBoardPages());
        binding.onboardViewPager2.adapter = onboardPageAdapter
    }

    private fun generateOnBoardPages() : ArrayList<OnboardPage> = arrayListOf(
        OnboardPage(
            R.raw.convenience_animation,
            getString(R.string.onboard_convenience_title),
            getString(R.string.onboard_convenience_text)
        ),
        OnboardPage(
            R.raw.organization_animation,
            getString(R.string.onboard_organization_title),
            getString(R.string.onboard_organization_text)
        ),
        OnboardPage(
            R.raw.synchronization_animation,
            getString(R.string.onboard_synchronization_title),
            getString(R.string.onboard_synchronization_text)
        )
    )

    private fun setupListeners() = with(binding) {
        onboardViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeActiveOnboardShower(position)
                skipTextView.visibility = if (position != 2) View.VISIBLE else View.INVISIBLE
                startMaterialButton.isVisible = position == 2
            }
        })
        skipTextView.setOnClickListener {
            toNotesFragment()
        }
        startMaterialButton.setOnClickListener {
            toNotesFragment()
        }
    }

    private fun changeActiveOnboardShower(position : Int) {
        val onboardShowers = binding.onboardShowersLinearLayout;
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
        preference.isFirstVisit = false
        findNavController().navigate(R.id.action_onboardFragment_to_googleAuthFragment)
    }
}