package com.gitlab.whatsappportal.ui.main_activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.gitlab.whatsappportal.R
import com.gitlab.whatsappportal.data.db.vo.CountryVo
import com.gitlab.whatsappportal.data.sp.StatusManager
import com.gitlab.whatsappportal.databinding.ActivityBerandaBinding
import com.gitlab.whatsappportal.databinding.FragmentLocalizationBinding
import com.gitlab.whatsappportal.ui.country_activity.CountryActivity
import dagger.android.support.DaggerAppCompatActivity
import org.jetbrains.anko.intentFor
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), View.OnClickListener {
    companion object{
        val REQUEST_CODE = 1
    }
    lateinit var binding: ActivityBerandaBinding
    @Inject lateinit var statusManager: StatusManager
    lateinit var local: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        local = statusManager.getLanguage()
        statusManager.setLocale(this, local)
        Timber.d("lokal ${Locale.getDefault()}")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beranda)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        initComponent()
    }

    private fun initComponent(){
        binding.tvCountryName.text = statusManager.getCountry()
        binding.tvCountryCode.text = statusManager.getCode()
        if (local == "in"){
            binding.tvLocalization.text = "ID"
        }else{
            binding.tvLocalization.text = "EN"
        }
        binding.tvCountryCode.setOnClickListener(this)
        binding.btnStartChat.setOnClickListener(this)
        binding.tvCountryName.setOnClickListener(this)
        binding.tvLocalization.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tvCountryCode -> {
                startActivityForResult(intentFor<CountryActivity>(), REQUEST_CODE)
            }
            R.id.tvCountryName ->{
                startActivityForResult(intentFor<CountryActivity>(), REQUEST_CODE)
            }
            R.id.btnStartChat -> {
                try {
                    val nomer = "${binding.tvCountryCode.text}${numberFormat(binding.etNumberPhone.text.toString())}"
                        .replace("+","")
                    val intent = Intent("android.intent.action.MAIN")
                    packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
                    intent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
                    intent.action = Intent.ACTION_SEND
                    intent.setPackage("com.whatsapp")
                    intent.putExtra(Intent.EXTRA_TEXT, "")
                    intent.putExtra("jid", "$nomer@s.whatsapp.net")
                    startActivity(intent)
                }catch (e: Exception){
                    if (binding.etNumberPhone.text.trim().isEmpty())
                        Toast.makeText(this, resources.getString(R.string.peringatan_nomor_kosong), Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this, resources.getString(R.string.peringatan_wa), Toast.LENGTH_SHORT).show()
                }
            }
            R.id.tvLocalization -> {
                val dialog = Dialog(this)
                val bindingFragment: FragmentLocalizationBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.fragment_localization, null, false)
                dialog.setContentView(bindingFragment.root)
                dialog.show()
                if (local == "in") {
                    bindingFragment.tvIndonesia.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                    bindingFragment.tvEnglish.setTextColor(ContextCompat.getColor(this, R.color.black))
                }else{
                    bindingFragment.tvIndonesia.setTextColor(ContextCompat.getColor(this, R.color.black))
                    bindingFragment.tvEnglish.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                }
                bindingFragment.llEnglish.setOnClickListener {
                    setLocale("en")
                    refresh()
                }
                bindingFragment.llIndonesia.setOnClickListener {
                    setLocale("in")
                    refresh()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CODE){
                val country: CountryVo? = data?.getParcelableExtra(CountryActivity.EXTRA_COUNTRY)
                binding.tvCountryCode.text = "+${country?.callingCode}"
                binding.tvCountryName.text = country?.name
                statusManager.storeCountry("${country?.name}")
                statusManager.storeCode("+${country?.callingCode}")
            }
        }
    }

    private fun numberFormat(string: String): String{
        val char = string[0]
        if(char == '0'){
            return string.substring(1)
        }
        return string
    }

    private fun setLocale(language: String){
        statusManager.storeLanguage(language)
    }

    private fun refresh(){
        startActivity(intentFor<MainActivity>())
        finish()
    }
}
