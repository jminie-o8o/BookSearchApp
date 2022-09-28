package com.example.booksearchapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.booksearchapp.data.model.Book
import com.example.booksearchapp.data.model.SearchResponse
import com.example.booksearchapp.data.repository.BookSearchRepository
import com.example.booksearchapp.worker.CacheDeleteWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val bookSearchRepository: BookSearchRepository,
    private val workManager: WorkManager
) : ViewModel() {

    companion object {
        private val WORKER_KEY = "cache_worker"
    }

    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> = _searchResult

    fun searchBooks(query: String) {
        viewModelScope.launch {
            val response = bookSearchRepository.searchBooks(query, getSortMode(), 1, 15)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    _searchResult.value = body
                }
            }
        }
    }

    // Room
    fun saveBook(book: Book) {
        viewModelScope.launch {
            bookSearchRepository.insertBook(book)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookSearchRepository.deleteBook(book)
        }
    }

    val favoriteBooks: StateFlow<List<Book>> = bookSearchRepository.getFavoriteBooks()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    // DataStore
    fun saveSortMode(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            bookSearchRepository.saveSortMode(value)
        }
    }

    suspend fun getSortMode(): String {
        return withContext(Dispatchers.IO) {
            bookSearchRepository.getSortMode().first()
        }
    }

    fun saveCacheDeleteMode(value: Boolean) = viewModelScope.launch {
        bookSearchRepository.saveCacheDeleteMode(value)
    }

    suspend fun getCacheDeleteMode() = withContext(Dispatchers.IO) {
        bookSearchRepository.getCacheDeleteMode().first()
    }

    // Paging
    val favoritePagingBooks: StateFlow<PagingData<Book>> =
        bookSearchRepository.getFavoritePagingBooks()
            .cachedIn(viewModelScope) // 코루틴이 데이터흐름을 캐시하고 공유 가능하게 만든다.
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())
    // UI 에서 관찰해야하는 데이터이기 때문에 stateIn을 써서 StateFlow 로 만들어준다.

    private val _searchPagingResult = MutableStateFlow<PagingData<Book>>(PagingData.empty())
    val searchPagingResult: StateFlow<PagingData<Book>> = _searchPagingResult.asStateFlow()

    fun searchBookPaging(query: String) {
        viewModelScope.launch {
            bookSearchRepository.searchBooksPaging(query, getSortMode())
                .cachedIn(viewModelScope)
                .collect {
                    _searchPagingResult.value = it
                }
        }
    }

    // WorkManger
    fun setWork() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true) // 충전중이고
            .setRequiresBatteryNotLow(true) // 배터리 잔량이 낮지 않을 때만 작업 수행
            .build()

        // 15분마다 작업이 수행되도록 설정
        val workRequest = PeriodicWorkRequestBuilder<CacheDeleteWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        // 동일한 작업을 중복해서 작업하지 않도록  enqueueUniquePeriodicWork 로 설정
        workManager.enqueueUniquePeriodicWork(
            WORKER_KEY, ExistingPeriodicWorkPolicy.REPLACE, workRequest
        )
    }

    // WORKER_KEY 라는 키를 가진 작업을 찾아서 삭제하는 함수
    fun deleteWork() = workManager.cancelUniqueWork(WORKER_KEY)

    // 현재 작업중에서 WORKER_KEY 라는 키를 가진 작업의 상태를 LiveData 타입으로 반환하는 함수
    fun getWorkStatus(): LiveData<MutableList<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(WORKER_KEY)
}
