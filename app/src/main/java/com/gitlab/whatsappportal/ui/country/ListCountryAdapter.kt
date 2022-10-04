package com.gitlab.whatsappportal.ui.country

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gitlab.whatsappportal.R
import com.gitlab.whatsappportal.data.local.db.vo.CountryVo
import com.gitlab.whatsappportal.data.remote.model.Country
import com.gitlab.whatsappportal.databinding.ItemListCountryBinding
import com.google.gson.Gson
import javax.inject.Inject

class ListCountryAdapter @Inject constructor() : ListAdapter<CountryVo, ListCountryAdapter.ViewHolder>(diffUtils) {

    private var onSelectListener: ((CountryVo) -> Unit)? = null

    fun selectListener(select: (CountryVo) -> Unit) {
        onSelectListener = select
    }

    class ViewHolder(itemListCountryBinding: ItemListCountryBinding) :
        RecyclerView.ViewHolder(itemListCountryBinding.root) {
        var binding: ItemListCountryBinding = itemListCountryBinding
    }

    companion object {
        private val diffUtils = object : DiffUtil.ItemCallback<CountryVo>() {
            override fun areItemsTheSame(oldItem: CountryVo, newItem: CountryVo): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: CountryVo, newItem: CountryVo): Boolean =
                oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListCountryBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val context = holder.binding.root.context
            val country = getItem(position)
            holder.binding.tvCountryName.text = country.name
            Glide.with(context).load(country.flag).circleCrop().into(holder.binding.ivFlag)

            val countryCode = Gson().fromJson(country.callingCode, Country.CallingCode::class.java)
            val listCountryCode =
                countryCode.suffixes.joinToString(separator = ", ") { "${countryCode.root}$it" }
            holder.binding.tvCountryCode.text = listCountryCode

            holder.binding.root.setOnClickListener { onSelectListener?.invoke(country) }
        } catch (e: Exception) {
            print(e.message)
        }
    }
}