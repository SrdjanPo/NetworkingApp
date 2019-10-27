package com.example.networkingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networkingapp.R
import com.example.networkingapp.Spot

class CardStackAdapter(
        private var spots: List<Spot> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val spot = spots[position]
                holder.name.text = spot.name
                holder.proffesion.text = spot.profession
                Glide.with(holder.image)
                        .load(spot.image)
                        .into(holder.image)
                holder.itemView.setOnClickListener { v ->
                        Toast.makeText(v.context, spot.name, Toast.LENGTH_SHORT).show()
                }
        }

        override fun getItemCount(): Int {
                return spots.size
        }

        fun setSpots(spots: List<Spot>) {
                this.spots = spots
        }

        fun getSpots(): List<Spot> {
                return spots
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
                val name: TextView = view.findViewById(R.id.profileName)
                var proffesion: TextView = view.findViewById(R.id.profileProfession)
                var image: ImageView = view.findViewById(R.id.photoIV)
        }

}