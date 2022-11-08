package com.stark.booksearchapp.ui.view.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stark.booksearchapp.data.model.Book
import com.stark.booksearchapp.databinding.ItemBookPreviewBinding

class BookSearchAdapter : ListAdapter<Book, BookSearchAdapter.BookSearchViewHolder>(BookDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSearchViewHolder {
        val binding = ItemBookPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(getItem(position))
            }
        }
    }

    private var onItemClickListener: ((Book) -> Unit)? = null
    fun setOnItemClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }

    class BookSearchViewHolder(
        private val binding: ItemBookPreviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(book: Book) {
            val author = book.authors.toString().removeSurrounding("[", "]")
            val publisher = book.publisher
            val date = if (book.datetime.isNotEmpty()) book.datetime.substring(0, 10) else ""

            itemView.apply {
                binding.ivArticleImage.load(book.thumbnail)
                binding.tvTitle.text = book.title
                binding.tvAuthor.text = "$author | $publisher"
                binding.tvDatetime.text = date
            }
        }
    }
}

object BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}
