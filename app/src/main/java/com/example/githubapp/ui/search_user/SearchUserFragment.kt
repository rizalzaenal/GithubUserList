package com.example.githubapp.ui.search_user

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.filter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentSearchUserBinding
import com.example.githubapp.ui.adapter.SearchUserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchUserFragment: Fragment(R.layout.fragment_search_user) {
    private var _binding: FragmentSearchUserBinding? = null
    private val binding: FragmentSearchUserBinding get() = _binding!!
    private val viewModel: SearchUserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchUserBinding.bind(view)

        binding.edittext.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && v.text.isNotBlank()) {
                Toast.makeText(requireContext(), v.text.toString(), Toast.LENGTH_SHORT).show()
                viewModel.searchNewQuery(v.text.toString())
                true
            } else {
                false
            }
        }
        initRecyclerView()
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchPagingFlow
                    .collectLatest { pagingData ->
                        (binding.rvSearch.adapter as? SearchUserAdapter)?.submitData(pagingData)
                    }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = SearchUserAdapter()
            val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL).apply {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.line_separator, null)
                drawable?.let { setDrawable(it) }
            }
            addItemDecoration(itemDecoration)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}