package com.example.booksearchapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.booksearchapp.R
import com.example.booksearchapp.databinding.FragmentBookBinding
import com.example.booksearchapp.databinding.FragmentBookReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookReportFragment : Fragment() {
    private var _binding: FragmentBookReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
