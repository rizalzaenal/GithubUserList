package com.example.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.data.model.UserItem
import com.example.githubapp.databinding.LayoutItemUserBinding

class SearchUserAdapter: PagingDataAdapter<UserItem, SearchUserAdapter.SearchUserViewHolder>(UserComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val binding = LayoutItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class SearchUserViewHolder(private val binding: LayoutItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserItem?) {
            item?.let {
                Glide.with(itemView)
                    .load(it.avatarUrl)
                    .into(binding.ivAvatar)
                binding.tvName.text = it.login
                binding.tvGithubUsername.text = it.id.toString()
                binding.tvGithubUrl.text = it.htmlUrl
            }
        }
    }
}

object UserComparator : DiffUtil.ItemCallback<UserItem>() {
    override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
        return oldItem == newItem
    }
}