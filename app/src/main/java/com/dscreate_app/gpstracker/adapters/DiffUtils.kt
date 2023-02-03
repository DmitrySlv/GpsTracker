package com.dscreate_app.gpstracker.adapters

import androidx.recyclerview.widget.DiffUtil
import com.dscreate_app.gpstracker.database.TrackItem

object DiffUtils: DiffUtil.ItemCallback<TrackItem>() {

    override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
        return oldItem == newItem
    }
}