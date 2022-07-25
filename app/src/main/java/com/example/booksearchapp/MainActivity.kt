package com.example.booksearchapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.booksearchapp.databinding.ActivityMainBinding
import com.example.booksearchapp.ui.view.FavoriteFragment
import com.example.booksearchapp.ui.view.SearchFragment
import com.example.booksearchapp.ui.view.SettingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationView()
        if (savedInstanceState == null) { // savedInstanceState == null 여부를 확인하여 앱이 처음으로 실행되었는지 확인
            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, SearchFragment())
                        .commit()
                    true
                }
                R.id.fragment_favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FavoriteFragment())
                        .commit()
                    true
                }
                R.id.fragment_setting -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, SettingFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}