package com.gitlab.whatsappportal.ui.country

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.gitlab.whatsappportal.R
import com.gitlab.whatsappportal.data.local.db.vo.CountryVo
import com.gitlab.whatsappportal.data.local.sp.StatusManager
import com.gitlab.whatsappportal.data.remote.model.Country
import com.gitlab.whatsappportal.databinding.ActivityCountryBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class CountryActivity : AppCompatActivity() {
    @Inject lateinit var statusManager: StatusManager

    private val viewModel by viewModels<CountryViewModel>()

    private lateinit var binding: ActivityCountryBinding
    @Inject lateinit var listCountryAdapter: ListCountryAdapter
    private lateinit var searchView: SearchView

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.pilih_negara)

        initUi()
    }

    private fun initUi() {
        binding.rvCountry.adapter = listCountryAdapter
        listCountryAdapter.selectListener { onSelectedCountry(it) }

        initObservers()
        viewModel.getCountry()
    }

    private fun initObservers() {
        viewModel.loadingState.observe(this) {

        }
        viewModel.errorState.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.successState.observe(this) {
            listCountryAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.country_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.nama_negara)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchCountry(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                timer?.cancel()
                timer = Timer()
                timer?.schedule(timerTask {
                    Handler(Looper.getMainLooper()).post {
                        newText?.let { viewModel.searchCountry(it) }
                    }
                }, 500)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun onSelectedCountry(countryVo: CountryVo) {
        val callingCode = Gson().fromJson(countryVo.callingCode, Country.CallingCode::class.java)
        if (callingCode.suffixes.size > 1) {
            //open dialog
        } else {
            statusManager.storeCountry(countryVo.name)
            statusManager.storeCode("${callingCode.root}${callingCode.suffixes[0]}")
            setResult(RESULT_OK, Intent())
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
