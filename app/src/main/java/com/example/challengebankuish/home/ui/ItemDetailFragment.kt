package com.example.challengebankuish.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.challengebankuish.common.models.RepoModel
import com.example.challengebankuish.databinding.FragmentItemDetailBinding
import com.squareup.picasso.Picasso

/**
 * Representa el detalle del item seleccionado, obtiene la información a través de los argumentos.
 */
class ItemDetailFragment : Fragment() {

    private var item: RepoModel? = null

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_PARCELABLE)) {
                item = it.getParcelable(ARG_ITEM_PARCELABLE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        updateContent()
        return rootView
    }

    private fun updateContent() {
        item?.let {
            binding.toolbarLayout.title = it.name
            binding.itemDetail.text = it.cloneUrl
            Picasso
                .get()
                .load(it.ownerRepoModel?.avatarUrl)
                .into(binding.imageBackground)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_ITEM_PARCELABLE = "item_repo_model_parcelable"
    }
}
