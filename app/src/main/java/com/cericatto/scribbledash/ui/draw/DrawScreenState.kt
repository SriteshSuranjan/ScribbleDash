package com.cericatto.scribbledash.ui.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.model.PathData
import com.cericatto.scribbledash.model.initOffsetList
import java.util.Stack

const val HISTORY_LIMIT = 5

data class DrawScreenState(
	val count: Int = -1,
	val selectedColor: Color = Color.Black,
	val currentPath: PathData? = null,
	val paths: List<PathData> = emptyList(),
	val actionStack: Stack<PathData> = Stack(),
	val drawMode: Boolean = false,
	val timer: Int = 3,
	val drawableId: Int = R.drawable.alien
)

fun initPathList(): List<PathData> {
	val move = PathData(
		path = initOffsetList()
	)
	return listOf(move)
}

fun initPathList(
	offsetList: List<Offset>
): List<PathData> {
	val data = PathData(
		path = offsetList
	)
	return listOf(data)
}

fun initDrawableList() = listOf(
	R.drawable.alien,
	R.drawable.bicycle,
	R.drawable.boat,
	R.drawable.book,
	R.drawable.butterfly,
	R.drawable.camera,
	R.drawable.car,
	R.drawable.castle,
	R.drawable.cat,
	R.drawable.clock,
	R.drawable.crown,
	R.drawable.cup,
	R.drawable.dog,
	R.drawable.envelope,
	R.drawable.eye,
	R.drawable.fish,
	R.drawable.flower,
	R.drawable.football_field,
	R.drawable.frog,
	R.drawable.glasses,
	R.drawable.heart,
	R.drawable.helicotper,
	R.drawable.hotairballoon,
	R.drawable.house,
	R.drawable.moon,
	R.drawable.mountains,
	R.drawable.robot,
	R.drawable.rocket,
	R.drawable.smiley,
	R.drawable.snowflake,
	R.drawable.sofa,
	R.drawable.star,
	R.drawable.train,
	R.drawable.umbrella,
	R.drawable.whale
)