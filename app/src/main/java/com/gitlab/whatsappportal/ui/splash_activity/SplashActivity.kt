package com.gitlab.whatsappportal.ui.splash_activity

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.gitlab.whatsappportal.BuildConfig
import com.gitlab.whatsappportal.R
import com.gitlab.whatsappportal.data.Status
import com.gitlab.whatsappportal.data.sp.StatusManager
import com.gitlab.whatsappportal.databinding.ActivitySplashActivtyBinding
import com.gitlab.whatsappportal.ui.main_activity.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import org.jetbrains.anko.intentFor
import javax.inject.Inject

@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SplashActivity : DaggerAppCompatActivity() {
    @Inject lateinit var statusManager: StatusManager
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by lazy{
        ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    private lateinit var binding: ActivitySplashActivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_activty)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        initComponent()
    }

    private fun initComponent(){
        getVersion()
        viewModel.version.observe(this, Observer {
            if (it.data?.version == BuildConfig.VERSION_NAME){
                downloadData()
            }else{
                val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error")
                    .setContentText("Anda tidak diijinkan menggunakan aplikasi ini, silahkan hubungi developer")
                    .setConfirmButton("EXIT") { sw ->
                        sw.dismissWithAnimation()
                        finish()
                    }
                sweetAlertDialog.setCancelable(false)
                sweetAlertDialog.show()
            }
        })
    }

    private fun getVersion(){
        if (isNetworkConnected()) {
            viewModel.getVersion()
        }else{
            val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Tidak ada koneksi")
                .setContentText("Harap periksa kembali koneksi internet anda")
                .setCancelButton("EXIT") {
                    it.dismissWithAnimation()
                    finish()
                }
                .setConfirmButton("TRY AGAIN") {
                    it.dismissWithAnimation()
                    getVersion()
                }
            sweetAlertDialog.setCancelable(false)
            sweetAlertDialog.show()
        }
    }

    private fun downloadData(){
        val isOld = statusManager.getStatus()
        if (!isOld){
            viewModel.getCountry(true).observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.pgDownload.visibility = View.GONE
                        statusManager.storeStatus(true)
                        startActivity(intentFor<MainActivity>())
                        finish()
                    }
                    Status.ERROR -> {
                        binding.pgDownload.visibility = View.GONE
                        val sweetAlertDialogError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Download Data Error")
                            .setContentText("Harap periksa koneksi internet anda atau cobalah beberapa saat lagi")
                            .setCancelButton("EXIT") { sw ->
                                sw.dismissWithAnimation()
                                finish()
                            }
                            .setConfirmButton("TRY AGAIN"){sw ->
                                sw.dismissWithAnimation()
                                downloadData()
                            }
                        sweetAlertDialogError.setCancelable(false)
                        sweetAlertDialogError.show()
                    }
                    else -> {
                        binding.pgDownload.visibility = View.VISIBLE
                    }
                }
            })
        }else{
            val intent = Intent(this, MainActivity::class.java)
            val pair = Pair<View, String>(binding.iconWhatsapp, binding.iconWhatsapp.transitionName)
            val option = ActivityOptions.makeSceneTransitionAnimation(this, pair)
            startActivity(intent, option.toBundle())
            finishAfterTransition()
        }
    }

    private fun isNetworkConnected(): Boolean{
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}
