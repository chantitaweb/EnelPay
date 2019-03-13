package net.cmadrid.www.enelpay

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity

class Network {
    companion object {
        fun hayRed(activity: AppCompatActivity):Boolean
        {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            return networkInfo != null && networkInfo.isConnected
        }
        val StrEmpresa=""
    }
}