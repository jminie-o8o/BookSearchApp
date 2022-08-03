package com.example.booksearchapp.ui.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.booksearchapp.R
import com.example.booksearchapp.data.db.BookSearchDatabase
import com.example.booksearchapp.data.repository.BookSearchRepositoryImpl
import com.example.booksearchapp.databinding.ActivityMainBinding
import com.example.booksearchapp.ui.viewmodel.BookSearchViewModel
import com.example.booksearchapp.ui.viewmodel.BookSearchViewModelFactory
import com.example.booksearchapp.util.Constants.DATASTORE_NAME

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bookSearchViewModel: BookSearchViewModel
    private lateinit var navController: NavController

    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        val database = BookSearchDatabase.getInstance(this)
        val bookSearchRepository = BookSearchRepositoryImpl(database, dataStore)
        val factory = BookSearchViewModelFactory(bookSearchRepository)
        bookSearchViewModel = ViewModelProvider(this, factory)[BookSearchViewModel::class.java]
    }

    private fun setupNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}
