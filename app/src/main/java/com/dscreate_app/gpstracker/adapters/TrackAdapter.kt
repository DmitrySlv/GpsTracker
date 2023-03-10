package com.dscreate_app.gpstracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dscreate_app.gpstracker.R
import com.dscreate_app.gpstracker.database.TrackItem
import com.dscreate_app.gpstracker.databinding.TrackItemBinding
import com.dscreate_app.gpstracker.utils.ClickType

class TrackAdapter(private val listener: Listener): ListAdapter<TrackItem, TrackAdapter.TrackHolder>(DiffUtils) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return TrackHolder(view, listener)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class TrackHolder(
        itemView: View, private val listener: Listener
        ): RecyclerView.ViewHolder(itemView), OnClickListener {

        private val binding = TrackItemBinding.bind(itemView)
        private var trackTemp: TrackItem? = null
        init {
            binding.ibDelete.setOnClickListener(this)
            binding.cardItem.setOnClickListener(this)
        }

        fun setData(trackItem: TrackItem) = with(binding) {
            trackTemp = trackItem
            trackItem.apply {
                val date = String.format(root.context.getString(R.string.date_tv) + date)
                val speed = String.format(root.context.getString(R.string.speed_tv) + speed +
                        root.context.getString(R.string.meter_in_sec))
                val time = String.format(time + root.context.getString(R.string.minutes))
                val distance = String.format(distance +
                        root.context.getString(R.string.distance_in_kilometer))

                tvDate.text = date
                tvSpeed.text = speed
                tvTime.text = time
                tvDistance.text = distance
            }
        }

        override fun onClick(view: View?) {
          val type = when(view?.id) {
                R.id.ibDelete -> ClickType.DELETE
                R.id.cardItem -> ClickType.OPEN
                else -> ClickType.OPEN
            }
            trackTemp?.let { listener.onClick(it, type) }
        }
    }

    interface Listener {
        fun onClick(trackItem: TrackItem, type: ClickType)
    }
}