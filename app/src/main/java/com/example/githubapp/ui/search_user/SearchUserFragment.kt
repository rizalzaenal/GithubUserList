package com.example.githubapp.ui.search_user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.example.githubapp.data.util.convertException
import com.example.githubapp.databinding.FragmentSearchUserBinding
import com.example.githubapp.ui.adapter.SearchLoadStateAdapter
import com.example.githubapp.ui.adapter.SearchUserAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchUserFragment : Fragment(R.layout.fragment_search_user) {
    private var _binding: FragmentSearchUserBinding? = null
    private val binding: FragmentSearchUserBinding get() = _binding!!
    private val viewModel: SearchUserViewModel by viewModels()
    private val searchUserAdapter = SearchUserAdapter {
        viewModel.getUserDetailData(it.login ?: return@SearchUserAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchUserBinding.bind(view)
        initRecyclerView()
        initObservers()

        binding.swipeRefresh.setOnRefreshListener {
            if (searchUserAdapter.itemCount > 0) {
                searchUserAdapter.refresh()
            } else {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchPagingFlow
                    .collectLatest { pagingData ->
                        searchUserAdapter.submitData(pagingData)
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userDetailFlow
                    .filter { it.shouldShown }
                    .collect {
                        if (it.errorMessage.isEmpty()) {
                            val toastText = getString(R.string.name, it.name) +
                                    "\n${getString(R.string.email, it.email)} - " +
                                    getString(R.string.created, it.createdAt)
                            Toast.makeText(requireContext(), toastText, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvSearch.apply {
            val concatAdapter =
                searchUserAdapter.withLoadStateFooter(SearchLoadStateAdapter(searchUserAdapter::retry))
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = concatAdapter
            addDecoration(this)

            searchUserAdapter.addLoadStateListener {
                when (it.refresh) {
                    is LoadState.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                        binding.errorLayout.root.visibility = View.GONE
                    }
                    is LoadState.Error -> {
                        binding.swipeRefresh.isRefreshing = false
                        val errorData = convertException((it.refresh as LoadState.Error).error)
                        binding.errorLayout.root.visibility = View.VISIBLE
                        binding.errorLayout.tvMessage.text = errorData.message
                        binding.errorLayout.tvDocumentationUrl.text = errorData.documentationUrl
                        binding.errorLayout.btnRetry.setOnClickListener { searchUserAdapter.retry() }
                    }
                    else -> {
                        binding.errorLayout.root.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun addDecoration(rv: RecyclerView) {
        val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        ResourcesCompat.getDrawable(resources, R.drawable.line_separator, null)?.let {
            itemDecoration.setDrawable(it)
        }
        rv.addItemDecoration(itemDecoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}