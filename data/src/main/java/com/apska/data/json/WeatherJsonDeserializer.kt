package com.apska.data.json

import com.apska.domain.model.Weather
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WeatherJsonDeserializer : JsonDeserializer<Weather> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Weather {
        val location = json.asJsonObject.get("location")

        return Weather(
            city = location.asJsonObject.get("name").asString,
            region = location.asJsonObject.get("region").asString,
            country = location.asJsonObject.get("country").asString
        )
    }
}