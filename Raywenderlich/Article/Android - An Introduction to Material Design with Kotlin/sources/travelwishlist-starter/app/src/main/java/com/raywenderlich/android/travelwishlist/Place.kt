package com.raywenderlich.android.travelwishlist

import android.content.Context


class Place(val name: String, private val imageName: String, val isFav: Boolean = false) {
  fun getImageResourceId(context: Context): Int {
    return context.resources.getIdentifier(this.imageName, "drawable", context.packageName)
  }
}