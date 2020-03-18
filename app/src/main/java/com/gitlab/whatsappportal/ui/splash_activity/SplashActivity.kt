package com.gitlab.whatsappportal.ui.splash_activity

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gitlab.whatsappportal.R
import com.gitlab.whatsappportal.data.Status
import com.gitlab.whatsappportal.data.sp.StatusManager
import com.gitlab.whatsappportal.databinding.ActivitySplashActivtyBinding
import com.gitlab.whatsappportal.ui.main_activity.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {
    @Inject lateinit var statusManager: StatusManager
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by lazy{
        ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    lateinit var binding: ActivitySplashActivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_activty)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        initComponent()
    }

    private fun initComponent(){
        val isOld = statusManager.getStatus()
        if (!isOld){
            viewModel.getCountry(true).observe(this, Observer {
                statusManager.storeStatus(true)
                if (it.status == Status.SUCCESS) {
                    Handler().postDelayed({
                        startActivity(intentFor<MainActivity>())
                        finish()
                    }, 1500)
                }
            })
        }else{
            Handler().postDelayed({
                startActivity(intentFor<MainActivity>())
                finish()
            }, 3000)
        }
    }
}
