package com.dscreate_app.gpstracker.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.dscreate_app.gpstracker.R

object DialogManager {

    fun showLocEnabledDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle(R.string.location_disabled)
        dialog.setMessage(context.getString(R.string.location_disabled_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.yes)) {
            _, _ -> listener.onClick()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.no)) {
                _, _ -> dialog.dismiss()
        }
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}