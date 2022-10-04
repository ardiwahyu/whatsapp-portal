package com.gitlab.whatsappportal.ui.main

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gitlab.whatsappportal.R
import com.gitlab.whatsappportal.data.local.sp.StatusManager
import com.gitlab.whatsappportal.databinding.ActivityBerandaBinding
import com.gitlab.whatsappportal.databinding.FragmentLocalizationBinding
import com.gitlab.whatsappportal.ui.country.CountryActivity
import com.gitlab.whatsappportal.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.intentFor
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityBerandaBinding
    @Inject lateinit var statusManager: StatusManager
    private lateinit var local: String

    private val selectCountryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            binding.tvCountryCode.text = statusManager.getCode()
            binding.tvCountryName.text = statusManager.getCountry()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        local = statusManager.getLanguage()
        LocaleHelper.setLocale(this, local)
        initUi()
    }

    private fun initUi() {
        val fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom)
        binding.llContainer.startAnimation(fromBottom)
        binding.tvCountryName.text = statusManager.getCountry()
        binding.tvCountryCode.text = statusManager.getCode()
        binding.tvLocalization.text = statusManager.getLanguage().uppercase()

        binding.tvCountryCode.setOnClickListener(this)
        binding.btnStartChat.setOnClickListener(this)
        binding.tvCountryName.setOnClickListener(this)
        binding.tvLocalization.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            binding.tvCountryCode.id, binding.tvCountryName.id -> {
                selectCountryLauncher.launch(intentFor<CountryActivity>())
            }
            binding.tvLocalization.id -> {
                showDialogLocalization()
            }
            binding.btnStartChat.id -> {
                if (!binding.etNumberPhone.text.isNullOrEmpty()) startChat()
                else Toast.makeText(this, "Nomor tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startChat() {
        val code = binding.tvCountryCode.text.removePrefix("+").toString().toInt()
        val number = binding.etNumberPhone.text.toString().toLong()
        val url = "https://wa.me/$code$number"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun showDialogLocalization() {
        val dialog = Dialog(this)
        val bindingDialog = FragmentLocalizationBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(bindingDialog.root)
        if (statusManager.getLanguage() == resources.getString(R.string.id))
            bindingDialog.tvIndonesia.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        else bindingDialog.tvEnglish.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        bindingDialog.llEnglish.setOnClickListener { setLocale(getString(R.string.en)) }
        bindingDialog.llIndonesia.setOnClickListener { setLocale(getString(R.string.id)) }
        dialog.show()
    }

    private fun setLocale(language: String) {
        statusManager.storeLanguage(language)
        startActivity(intentFor<MainActivity>())
        finish()
    }
}
