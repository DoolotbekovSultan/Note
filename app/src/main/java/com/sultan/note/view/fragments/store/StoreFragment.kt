package com.sultan.note.view.fragments.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.sultan.note.databinding.FragmentStoreBinding
import com.sultan.note.view.adapters.StoreAdapter


class StoreFragment : Fragment() {

    private val storeAdapter = StoreAdapter()
    private val database = Firebase.firestore
    private lateinit var binding : FragmentStoreBinding
    private lateinit var listener : ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
    }

    private fun initialize() {
        binding.storeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.storeRecyclerView.adapter = storeAdapter
    }

    private fun setupListeners() {
        
        binding.storeAddButton.setOnClickListener {
            val string = hashMapOf(
                "string" to binding.storeEditText.text.toString()
            )
            database.collection("strings")
                .add(string)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
            binding.storeEditText.text.clear()
        }

        val query = database.collection("strings")
        listener = query.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            value?.let { snapshot ->
                val list = mutableListOf<String>()
                for (document in snapshot) {
                    val string = document.getString("string")
                    string?.let { s ->
                        list.add(s)
                    }
                    storeAdapter.submitList(list)
                }
            }
        }

    }
}