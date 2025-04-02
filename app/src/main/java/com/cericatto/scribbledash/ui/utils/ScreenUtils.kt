package com.cericatto.scribbledash.ui.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun isLandscapeOrientation(): Boolean {
	val context = LocalContext.current
	val orientation = context.resources.configuration.orientation
	return when (orientation) {
		Configuration.ORIENTATION_LANDSCAPE -> true
		else -> false
	}
}