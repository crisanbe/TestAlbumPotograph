package com.cvelez.photos.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.cvelez.photos.R
import com.cvelez.photos.core.observe
import com.cvelez.photos.utils.InternetConnectionCallback
import com.cvelez.photos.utils.InternetConnectionObserver
import com.cvelez.photos.utils.ToastHelper
import com.cvelez.photos.utils.showNoInternetDialog
import com.cvelez.photos.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), InternetConnectionCallback {

    @Inject
    lateinit var toastHelper: ToastHelper
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InternetConnectionObserver
            .instance(this)
            .setCallback(this)
            .register()

            Thread.sleep(3000)
            installSplashScreen()
            setContentView(R.layout.activity_main)

            navController = findNavController(R.id.nav_host_fragment)
            NavigationUI.setupActionBarWithNavController(this, navController)

            toastHelper.toastMessages.observe(this) {
                showToast(it)
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        InternetConnectionObserver.unRegister()
    }
    override fun onConnected() {
        showNoInternetDialog(this,"Hay conexión","Ya puedes continuar👌.",R.drawable.wifisignal)
    }
    override fun onDisconnected() {
        showNoInternetDialog(this,"No hay conexión","Por favor, verifica tu conexión y vuelve a intentarlo.",R.drawable.wifioffline)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}