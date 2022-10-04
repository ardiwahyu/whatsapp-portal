package com.gitlab.whatsappportal.ui.splashscreen

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.gitlab.whatsappportal.data.local.sp.StatusManager
import com.gitlab.whatsappportal.databinding.ActivitySplashActivtyBinding
import com.gitlab.whatsappportal.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.intentFor
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var statusManager: StatusManager
    private val viewModel by viewModels<SplashViewModel>()
    private lateinit var binding: ActivitySplashActivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi(){
        downloadData()
        initObservers()
    }

    private fun initObservers() {
        viewModel.loadingState.observe(this) {
            binding.pgDownload.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.errorState.observe(this) {
            showDialogError(it)
        }
        viewModel.successState.observe(this) {
            statusManager.storeStatus(true)
            navigateToMainActivity()
        }
    }

    private fun downloadData() {
        val isOld = statusManager.getStatus()
        if (!isOld) {
            viewModel.getCountry()
        } else {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = intentFor<MainActivity>()
        val pair = Pair<View, String>(binding.iconWhatsapp, binding.iconWhatsapp.transitionName)
        val option = ActivityOptions.makeSceneTransitionAnimation(this, pair)
        startActivity(intent, option.toBundle())
        finishAfterTransition()
    }

    private fun showDialogError(message: String) {
        val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Download Data Error")
            .setContentText(message)
            .setCancelButton("EXIT") {
                it.dismissWithAnimation()
                finish()
            }
            .setConfirmButton("TRY AGAIN") {
                it.dismissWithAnimation()
                downloadData()
            }
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.show()
    }
}
