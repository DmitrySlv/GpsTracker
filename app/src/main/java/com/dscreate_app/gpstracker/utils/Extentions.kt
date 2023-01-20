package com.dscreate_app.gpstracker.utils

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dscreate_app.gpstracker.R

fun Fragment.openFragment(frag: Fragment) {
    requireActivity().supportFragmentManager.beginTransaction()
        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        .replace(R.id.placeHolder, frag)
        .commit()
}

fun AppCompatActivity.openFragment(frag: Fragment) {
    if (supportFragmentManager.fragments.isNotEmpty()) {
        if (supportFragmentManager.fragments[0].javaClass == frag.javaClass) {
            return
        }
    }
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.placeHolder, frag)
            .commit()
}

fun Fragment.showToast(string: String) {
    Toast.makeText(activity, string, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}