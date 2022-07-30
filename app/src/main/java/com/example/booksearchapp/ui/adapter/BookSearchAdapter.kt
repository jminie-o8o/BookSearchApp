package com.example.booksearchapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.booksearchapp.data.model.Document
import com.example.booksearchapp.databinding.ItemBookPreviewBinding

class BookSearchAdapter : ListAdapter<Document, BookSearchAdapter.BookSearchViewHolder>(BookDiffCallback) {
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

    private var onItemClickListener: ((Document) -> Unit)? = null
    fun setOnItemClickListener(listener: (Document) -> Unit) {
        onItemClickListener = listener
    }

    class BookSearchViewHolder(
        private val binding: ItemBookPreviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(document: Document) {
            val author = document.authors.toString().removeSurrounding("[", "]")
            val publisher = document.publisher
            val date = if (document.datetime.isNotEmpty()) document.datetime.substring(0, 10) else ""

            itemView.apply {
                binding.ivArticleImage.load(document.thumbnail)
                binding.tvTitle.text = document.title
                binding.tvAuthor.text = "$author | $publisher"
                binding.tvDatetime.text = date
            }
        }
    }
}

object BookDiffCallback : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem == newItem
    }
}
