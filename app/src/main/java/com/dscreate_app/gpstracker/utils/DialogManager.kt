package com.dscreate_app.gpstracker.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.dscreate_app.gpstracker.R
import com.dscreate_app.gpstracker.databinding.SaveDialogBinding

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

    fun showSaveDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val binding = SaveDialogBinding.inflate(
            LayoutInflater.from(context), null, false
        )
        builder.setView(binding.root)
        val dialog = builder.create()
        with(binding) {
            bSave.setOnClickListener {
                listener.onClick()
                dialog.dismiss()
            }
            bCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}