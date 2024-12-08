package com.example.placesdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.placesdemo.adapters.MarkerAdapter
import com.example.placesdemo.databinding.FragmentVisitBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class VisitFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentVisitBinding? = null
    private val binding get() = checkNotNull(_binding)
    private lateinit var markerAdapter: MarkerAdapter
    private lateinit var map: GoogleMap

    private val viewModel: PlacesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVisitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        markerAdapter = MarkerAdapter(emptyList())
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        binding.listView.adapter = markerAdapter
        binding.listView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        // Observe marker data
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.visitMarkers.collect { markers ->
                markerAdapter.updateData(markers.map { it.toPair() })
                if (::map.isInitialized) {
                    map.clear()
                    markers.forEach { marker ->
                        map.addMarker(MarkerOptions().position(LatLng(marker.lat, marker.lng)))
                    }
                }
            }
        }

        // Load markers
        viewModel.loadMarkers("visitPlaces")

        // Initialize map
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener { latLng ->
            viewModel.saveMarker("visitPlaces", latLng.latitude, latLng.longitude)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
