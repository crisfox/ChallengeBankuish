package com.example.challengebankuish.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.challengebankuish.databinding.FragmentItemListBinding
import com.example.challengebankuish.home.ui.recyclerview.adapter.ItemLoadStateAdapter
import com.example.challengebankuish.home.ui.recyclerview.adapter.RepoListRecyclerViewAdapter
import com.example.challengebankuish.home.ui.viewmodel.RepoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Muestra el listado que llega desde el source correspondiente
 */
class ItemListFragment : Fragment() {

    private val viewModel by viewModel<RepoViewModel>()
    private lateinit var adapterRecyclerView: RepoListRecyclerViewAdapter
    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupSubmitData()
        setupStateViews()
        setupButtons()
    }

    /**
     * Configura el recycler junto con el adapter.
     */
    private fun setupRecycler() {
        adapterRecyclerView = RepoListRecyclerViewAdapter()
        binding.itemList.adapter = adapterRecyclerView.withLoadStateFooter(
            footer = ItemLoadStateAdapter(adapterRecyclerView::retry)
        )
    }

    /**
     * Configura para que cuando reciba nueva información la envie al adapter y oculte el swipe refresh.
     */
    private fun setupSubmitData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allRepoModels.collectLatest { pagingData ->
                adapterRecyclerView.submitData(pagingData)
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    /**
     * Configura el botón de retry y refresh.
     */
    private fun setupButtons() {
        binding.retryButtonError.setOnClickListener { adapterRecyclerView.retry() }
        binding.swipeRefresh.setOnRefreshListener {
            adapterRecyclerView.refresh()
        }
    }

    /**
     * Según los estados que recibe controla las visibilidades de la ui.
     */
    private fun setupStateViews() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapterRecyclerView.loadStateFlow.collect {
                with(binding) {
                    val isListEmpty = it.refresh is LoadState.NotLoading && adapterRecyclerView.itemCount == 0
                    val isListError = it.source.refresh is LoadState.Error
                    val isListLoading = it.source.refresh is LoadState.Loading
                    emptyState.isVisible = isListEmpty
                    progressBarHome.isVisible = isListLoading
                    errorState.isVisible = isListError
                    itemList.isVisible = !isListEmpty && !isListError && !isListLoading
                    val errorState = it.source.append as? LoadState.Error
                        ?: it.source.prepend as? LoadState.Error
                        ?: it.append as? LoadState.Error
                        ?: it.prepend as? LoadState.Error
                        ?: it.refresh as? LoadState.Error
                    errorState?.let {
                        "Wooops Error".also { errorText.text = it }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
