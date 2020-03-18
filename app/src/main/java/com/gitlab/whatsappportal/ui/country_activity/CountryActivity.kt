package com.gitlab.whatsappportal.ui.country_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gitlab.whatsappportal.R
import com.gitlab.whatsappportal.data.db.vo.CountryVo
import com.gitlab.whatsappportal.databinding.ActivityCountryBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CountryActivity : DaggerAppCompatActivity() {
    companion object{
        const val EXTRA_COUNTRY = "extra_country"
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CountryViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CountryViewModel::class.java)
    }
    @Inject lateinit var listCountryAdapter: ListCountryAdapter
    lateinit var binding: ActivityCountryBinding
    private lateinit var searchView: SearchView
    private val list: ArrayList<CountryVo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_country)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.pilih_negara)
        initComponent()
    }

    private fun initComponent(){
        try {
            binding.rvCountry.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
            )
            binding.rvCountry.adapter = listCountryAdapter
            listCountryAdapter.setOnItemClickCallback(object : ListCountryAdapter.OnItemClickCallback{
                override fun onItemClicked(countryVo: CountryVo, position: Int) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_COUNTRY, countryVo)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            })
        }catch (e: Exception){
            print(e.message)
        }
        viewModel.getCountry(false).observe(this, Observer {
            list.clear()
            it.data?.let { it1 -> list.addAll(it1) }
            listCountryAdapter.submitList(list)
            listCountryAdapter.notifyDataSetChanged()
        })
        viewModel.countryList.observe(this, Observer {
            list.clear()
            it.data?.let { it1 -> list.addAll(it1) }
            listCountryAdapter.submitList(list)
            listCountryAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.country_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.nama_negara)
        val handler = Handler()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchCountry(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val runnable = Runnable {
                    newText?.let { viewModel.searchCountry(it) }
                }
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(runnable, 400)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
