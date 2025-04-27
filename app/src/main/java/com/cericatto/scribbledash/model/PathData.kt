package com.cericatto.scribbledash.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.cericatto.scribbledash.ui.navigation.ColorSerializer
import com.cericatto.scribbledash.ui.navigation.OffsetSerializer
import kotlinx.serialization.Serializable
import kotlin.random.Random

typealias SerializableOffset = @Serializable(with = OffsetSerializer::class) Offset

@Serializable
data class PathData(
//	val id: String = System.currentTimeMillis().toString(),
	val id: Int = 0,
	@Serializable(with = ColorSerializer::class)
	val color: Color = Color.Black,
//	@Serializable(with = OffsetListSerializer::class)
//	val path: List<Offset> = emptyList<Offset>()
	// val path: List<@Serializable(with = OffsetSerializer::class) Offset> = emptyList()
	val path: List<SerializableOffset> = emptyList()
)

fun initOffsetList() = List(50) { Offset(Random.nextFloat() * 400, Random.nextFloat() * 400) }