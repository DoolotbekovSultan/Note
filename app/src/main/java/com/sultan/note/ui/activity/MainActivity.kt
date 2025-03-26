package com.sultan.note.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.sultan.note.R
import com.sultan.note.databinding.ActivityMainBinding
import com.sultan.note.utils.Preference

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    val preference = Preference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initialize()
    }

    private fun initialize() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController ?: return
        preference.unit(this)

        if (!preference.isFirstVisit) {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.onboardFragment, true).build()
            navController.navigate(R.id.notesFragment, null, navOptions)
        }
    }
}