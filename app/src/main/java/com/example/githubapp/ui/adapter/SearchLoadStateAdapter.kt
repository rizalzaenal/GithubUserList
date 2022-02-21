package com.example.githubapp.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.data.util.convertException
import com.example.githubapp.databinding.LayoutPagingStateBinding

class SearchLoadStateAdapter(private val retry: () -> Unit): LoadStateAdapter<SearchLoadStateAdapter.SearchLoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): SearchLoadStateViewHolder {
        val binding = LayoutPagingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchLoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: SearchLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class SearchLoadStateViewHolder(private val binding: LayoutPagingStateBinding, private val retry: () -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.Error -> {
                    binding.apply {
                        val errorData = convertException(loadState.error)
                        errorLayout.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        btnRetry.setOnClickListener { retry() }

                        if (errorData.message != null || errorData.documentationUrl != null ) {
                            tvMessage.text = errorData.message
                            tvDocumentationUrl.text = errorData.documentationUrl
                            tvDocumentationUrl.setOnClickListener {
                                it.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(errorData.documentationUrl)))
                            }
                        }
                    }
                }
                else -> {
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        errorLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
}