package com.example.booksearchapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.booksearchapp.data.model.BookReport
import com.example.booksearchapp.databinding.ItemBookReportBinding

class BookReportPagingAdapter :
    PagingDataAdapter<BookReport, BookReportPagingAdapter.BookReportViewHolder>(
        BookReportDiffCallback
    ) {
    override fun onBindViewHolder(holder: BookReportViewHolder, position: Int) {
        val bookReport = getItem(position)
        bookReport?.let { bookReport ->
            holder.bind(bookReport)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(bookReport) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookReportViewHolder {
        return BookReportViewHolder(
            ItemBookReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((BookReport) -> Unit)? = null
    fun setOnItemClickListener(listener: (BookReport) -> Unit) {
        onItemClickListener = listener
    }

    class BookReportViewHolder(
        private val binding: ItemBookReportBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookReport: BookReport) {
            itemView.apply {
                binding.ivArticleImage.load(bookReport.thumbnail)
                binding.tvTitle.text = bookReport.reportTitle
                binding.tvDatetime.text = bookReport.date
            }
        }
    }
}

object BookReportDiffCallback : DiffUtil.ItemCallback<BookReport>() {
    override fun areItemsTheSame(oldItem: BookReport, newItem: BookReport): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: BookReport, newItem: BookReport): Boolean {
        return oldItem == newItem
    }
}
