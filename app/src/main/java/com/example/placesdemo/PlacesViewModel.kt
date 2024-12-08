package com.example.placesdemo

import androidx.lifecycle.ViewModel
import com.example.placesdemo.models.MarkerData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PlacesViewModel : ViewModel() {
    private val db = Firebase.firestore

    // StateFlow to hold markers for AvoidPlaces and VisitPlaces
    private val _avoidMarkers = MutableStateFlow<List<MarkerData>>(emptyList())
    val avoidMarkers: StateFlow<List<MarkerData>> = _avoidMarkers

    private val _visitMarkers = MutableStateFlow<List<MarkerData>>(emptyList())
    val visitMarkers: StateFlow<List<MarkerData>> = _visitMarkers

    fun loadMarkers(collectionName: String) {
        val targetFlow = if (collectionName == "avoidPlaces") _avoidMarkers else _visitMarkers
        db.collection(collectionName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val markers = snapshot.documents.mapNotNull { doc ->
                        val lat = doc.getDouble("lat")
                        val lng = doc.getDouble("lng")
                        if (lat != null && lng != null) MarkerData(lat, lng) else null
                    }
                    targetFlow.value = markers
                }
            }
    }

    fun saveMarker(collectionName: String, latitude: Double, longitude: Double) {
        val marker = hashMapOf("lat" to latitude, "lng" to longitude)
        db.collection(collectionName)
            .add(marker)
    }
}
