package com.sultan.note.ui.fragments.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sultan.note.databinding.FragmentOnboardPageBinding
import com.sultan.note.ui.models.OnboardPage

class OnboardPageFragment : Fragment() {

    private lateinit var binding : FragmentOnboardPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() = with(binding) {

        val onboardPage = arguments?.getSerializable("ON_BOARD_PAGE") as OnboardPage;

        onboardPage.let { board ->
            animationLottieAnimationView.setAnimation(board.animation)
            titleTextView.text = board.title
            textTextView.text = board.text
        }

    }

}