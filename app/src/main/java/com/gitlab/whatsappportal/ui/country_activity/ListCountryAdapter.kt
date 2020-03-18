package com.gitlab.whatsappportal.ui.country_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gitlab.whatsappportal.R
import com.gitlab.whatsappportal.data.db.vo.CountryVo
import com.gitlab.whatsappportal.databinding.ItemListCountryBinding
import timber.log.Timber
import javax.inject.Inject

class ListCountryAdapter @Inject constructor() :
    ListAdapter<CountryVo, ListCountryAdapter.ViewHolder>(
        CATEGORY_COMPARATOR
    ) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var layoutInflater: LayoutInflater

    class ViewHolder(itemListCountryBinding: ItemListCountryBinding) :
        RecyclerView.ViewHolder(itemListCountryBinding.root) {
        var binding: ItemListCountryBinding = itemListCountryBinding
    }

    companion object {
        private val CATEGORY_COMPARATOR = object : DiffUtil.ItemCallback<CountryVo>() {
            override fun areItemsTheSame(oldItem: CountryVo, newItem: CountryVo): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: CountryVo, newItem: CountryVo): Boolean =
                oldItem.name == newItem.name
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemListCountryBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_list_country, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.binding.country = getItem(position)
        } catch (e: Exception) {
            print(e.message)
        }
        holder.itemView.setOnClickListener(
            CustomOnItemClickListener(position,
                object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View?, position: Int) {
                        onItemClickCallback.onItemClicked(getItem(position), position)
                    }
                })
        )
    }

    interface OnItemClickCallback {
        fun onItemClicked(countryVo: CountryVo, position: Int)
    }
}