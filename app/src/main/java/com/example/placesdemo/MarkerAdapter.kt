package com.example.placesdemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.placesdemo.databinding.ListItemMarkerBinding

class MarkerAdapter(private var markers: List<Pair<String, String>>) : RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    fun updateData(newMarkers: List<Pair<String, String>>) {
        markers = newMarkers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemMarkerBinding.inflate(inflater, parent, false)
        return MarkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val (title, subtitle) = markers[position]
        holder.bind(title, subtitle)
    }

    override fun getItemCount(): Int = markers.size

    class MarkerViewHolder(private val binding: ListItemMarkerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, subtitle: String) {
            binding.markerTitle.text = title
            binding.markerSubtitle.text = subtitle
        }
    }
}
