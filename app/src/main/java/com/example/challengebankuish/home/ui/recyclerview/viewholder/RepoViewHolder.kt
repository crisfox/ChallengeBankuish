package com.example.challengebankuish.home.ui.recyclerview.viewholder

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.challengebankuish.R
import com.example.challengebankuish.common.models.RepoModel
import com.example.challengebankuish.databinding.ItemListContentBinding
import com.example.challengebankuish.home.ui.ItemDetailFragment
import com.squareup.picasso.Picasso

/**
 * Se encarga de mostrar los datos necesarios para la vista.
 */
class RepoViewHolder(private val binding: ItemListContentBinding) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bindTo(repoModel: RepoModel?) {
        repoModel?.let {
            binding.idText.text = it.name
            binding.content.text = it.ownerRepoModel?.login
            Picasso
                .get()
                .load(it.ownerRepoModel?.avatarUrl)
                .into(binding.imageAvatar)
        }

        with(itemView) {
            tag = repoModel
            binding.item.setOnClickListener {
                val itemSelected = tag as RepoModel
                val bundle = Bundle()
                bundle.putParcelable(
                    ItemDetailFragment.ARG_ITEM_PARCELABLE,
                    itemSelected
                )
                findNavController()
                    .navigate(R.id.show_item_detail, bundle)
            }
        }
    }
}
