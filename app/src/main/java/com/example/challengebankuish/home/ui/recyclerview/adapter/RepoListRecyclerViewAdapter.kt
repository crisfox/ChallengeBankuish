package com.example.challengebankuish.home.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.challengebankuish.common.models.RepoModel
import com.example.challengebankuish.databinding.ItemListContentBinding
import com.example.challengebankuish.home.ui.recyclerview.viewholder.RepoViewHolder

/**
 * Adapter paginado con comparación de datos para optimizar procesos.
 */
class RepoListRecyclerViewAdapter : PagingDataAdapter<RepoModel, RepoViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<RepoModel>() {
            override fun areItemsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
