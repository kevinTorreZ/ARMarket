package com.example.armarket.ViewModel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log

class GpsStatusReceiver(private val onGpsStatusChanged: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.d("GpsStatusReceiver", "GPS habilitado: $isGpsEnabled") // Para depuración
        onGpsStatusChanged(isGpsEnabled)
    }
}
