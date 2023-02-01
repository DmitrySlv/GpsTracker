package com.dscreate_app.gpstracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dscreate_app.gpstracker.R
import com.dscreate_app.gpstracker.database.TrackItem
import com.dscreate_app.gpstracker.databinding.TrackItemBinding

class TrackAdapter: ListAdapter<TrackItem, TrackAdapter.TrackHolder>(DiffUtils) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class TrackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TrackItemBinding.bind(itemView)

        fun setData(trackItem: TrackItem) = with(binding) {
            trackItem.apply {
                val speed = String.format(speed + root.context.getString(R.string.meter_in_sec))
                val time = String.format(time + root.context.getString(R.string.minutes))
                val distance = String.format(distance +
                        root.context.getString(R.string.distance_in_kilometer))

                tvDate.text = date
                tvSpeed.text = speed
                tvTime.text = time
                tvDistance.text = distance
            }
        }
    }
}