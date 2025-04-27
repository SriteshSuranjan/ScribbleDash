package com.cericatto.scribbledash.ui.navigation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.cericatto.scribbledash.model.PathData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

object OffsetSerializer : KSerializer<Offset> {
	override val descriptor = PrimitiveSerialDescriptor("Offset", PrimitiveKind.STRING)

	override fun serialize(encoder: Encoder, value: Offset) {
		val string = "${value.x},${value.y}"
		encoder.encodeString(string)
	}

	override fun deserialize(decoder: Decoder): Offset {
		val string = decoder.decodeString()
		val parts = string.split(",")
		if (parts.size != 2) throw IllegalArgumentException("Invalid Offset format: $string")
		val x = parts[0].toFloatOrNull() ?: throw IllegalArgumentException("Invalid x: ${parts[0]}")
		val y = parts[1].toFloatOrNull() ?: throw IllegalArgumentException("Invalid y: ${parts[1]}")
		return Offset(x, y)
	}
}

object ColorSerializer : KSerializer<Color> {
	override val descriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.LONG)

	override fun serialize(encoder: Encoder, value: Color) {
		encoder.encodeLong(value.value.toLong())
	}

	override fun deserialize(decoder: Decoder): Color {
		return Color(decoder.decodeLong().toULong())
	}
}

val json = Json {
	serializersModule = SerializersModule {
		contextual(OffsetSerializer)
		contextual(ColorSerializer)
	}
}

fun List<PathData>.pathsToString(): String {
	val jsonString = json.encodeToString(this)
	println("Serialized JSON: $jsonString") // Debug log
	return jsonString
}

fun String.stringToPath(): List<PathData> {
	println("Input JSON: $this") // Debug log
	return try {
		json.decodeFromString(this)
	} catch (e: Exception) {
		println("Deserialization error: ${e.message}")
		emptyList()
	}
}