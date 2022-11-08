package com.stark.booksearchapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.stark.booksearchapp.data.model.Book
import com.stark.booksearchapp.databinding.ItemBookPreviewBinding
import com.stark.booksearchapp.ui.view.search.BookSearchAdapter

class BookSearchPagingAdapter :
    PagingDataAdapter<Book, BookSearchAdapter.BookSearchViewHolder>(BookDiffCallback) {
    override fun onBindViewHolder(holder: BookSearchAdapter.BookSearchViewHolder, position: Int) {
        val pagedBook = getItem(position)
        pagedBook?.let { book ->
            holder.bind(book)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(book) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookSearchAdapter.BookSearchViewHolder {
        return BookSearchAdapter.BookSearchViewHolder(
            ItemBookPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Book) -> Unit)? = null
    fun setOnItemClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }

    object BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.isbn == newItem.isbn
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}
