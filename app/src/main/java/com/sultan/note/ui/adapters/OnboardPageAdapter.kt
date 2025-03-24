package com.sultan.note.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sultan.note.ui.fragments.onboard.OnboardPageFragment
import com.sultan.note.models.OnboardPage

class OnboardPageAdapter(
    fragment : Fragment,
    private val onboardPages : ArrayList<OnboardPage>
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = OnboardPageFragment().apply {
        val bundle = Bundle()
        bundle.putSerializable("ON_BOARD_PAGE", onboardPages[position])
        arguments = bundle
    }

    override fun getItemCount() = onboardPages.size
}