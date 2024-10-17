package com.example.armarket.ViewModel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> get() = _location

    private val _isGpsEnabled = MutableLiveData<Boolean>()
    val isGpsEnabled: LiveData<Boolean> get() = _isGpsEnabled

    private val _nearbySupermarkets = MutableLiveData<List<Supermarket>>()
    val nearbySupermarkets: LiveData<List<Supermarket>> get() = _nearbySupermarkets

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val gpsStatusReceiver = GpsStatusReceiver { isEnabled ->
        _isGpsEnabled.postValue(isEnabled)
        if (isEnabled) {
            requestLocationUpdates() // Solicitar actualizaciones de ubicación si el GPS está habilitado
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                _location.value = location
                getNearbySupermarkets() // Obtener supermercados cercanos
            }
        }
    }

    init {
        // Registrar el receptor de cambios de estado del GPS
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        getApplication<Application>().registerReceiver(gpsStatusReceiver, filter)
        _isGpsEnabled.value = isLocationEnabled() // Verificar el estado inicial
    }

    override fun onCleared() {
        super.onCleared()
        // Anular el registro del receptor
        getApplication<Application>().unregisterReceiver(gpsStatusReceiver)
    }

    fun requestLocationUpdates() {
        if (isLocationEnabled()) {
            if (ContextCompat.checkSelfPermission(
                    getApplication(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationRequest = LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 10000)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(5000)
                    .build()

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null // Usar el looper principal
                )
            } else {
                _isGpsEnabled.value = false // GPS desactivado
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getApplication<Application>().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private val firestore = Firebase.firestore

    fun getNearbySupermarkets() {
        _location.value?.let { userLocation ->
            val userLatLng = LatLng(userLocation.latitude, userLocation.longitude)
            firestore.collection("Supermercados")
                .get()
                .addOnSuccessListener { result ->
                    val nearbySupermarkets = mutableListOf<Supermarket>()
                    for (document in result) {
                        val name = document.getString("Nombre") ?: ""
                        val latitude = document.getDouble("Latitud") ?: 0.0
                        val longitude = document.getDouble("Longitud") ?: 0.0
                        val supermarketLocation = LatLng(latitude, longitude)

                        // Calcular la distancia
                        val distance = calculateDistance(userLatLng, supermarketLocation)
                        nearbySupermarkets.add(Supermarket(name, latitude, longitude, distance))
                    }
                    // Ordenar los supermercados por distancia
                    nearbySupermarkets.sortBy { it.distance }
                    _nearbySupermarkets.postValue(nearbySupermarkets)
                }
        }
    }

    private fun calculateDistance(userLocation: LatLng, supermarketLocation: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            userLocation.latitude,
            userLocation.longitude,
            supermarketLocation.latitude,
            supermarketLocation.longitude,
            results
        )
        return results[0] // Devuelve la distancia en metros
    }
}

data class Supermarket(val name: String, val latitude: Double, val longitude: Double, val distance: Float)
