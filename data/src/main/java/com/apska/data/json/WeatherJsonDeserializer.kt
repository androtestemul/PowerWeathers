package com.apska.data.json

import com.apska.domain.model.Weather
import com.apska.domain.model.WeatherDay
import com.apska.domain.model.WeatherHour
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
        val current = json.asJsonObject.get("current")

        val hourList = mutableListOf<WeatherHour>()
        val dayList = mutableListOf<WeatherDay>()

        var tempMin = ""
        var tempMax = ""

        json.asJsonObject
            .get("forecast").asJsonObject
            .getAsJsonArray("forecastday").forEachIndexed { index, element ->
                if (index == 0) {
                    tempMin = element.asJsonObject.getAsJsonObject("day").get("mintemp_c").asString
                    tempMax = element.asJsonObject.getAsJsonObject("day").get("maxtemp_c").asString

                    fillHourList(element, hourList)
                }

                fillDayList(element, dayList)
            }

        return Weather(
            city = location.asJsonObject.get("name").asString,
            region = location.asJsonObject.get("region").asString,
            country = location.asJsonObject.get("country").asString,
            lastUpdated = current.asJsonObject.get("last_updated_epoch").asLong * 1000,
            condition = current.asJsonObject.getAsJsonObject("condition").get("text").asString,
            tempCurrent = current.asJsonObject.get("temp_c").asString,
            tempMin = tempMin,
            tempMax = tempMax,
            windSpeed = current.asJsonObject.get("wind_kph").asString,
            windDirection = current.asJsonObject.get("wind_dir").asString,
            windDegree = current.asJsonObject.get("wind_degree").asFloat,

            hourList = hourList,
            dayList = dayList
        )
    }

    private fun fillHourList(element: JsonElement, hourList: MutableList<WeatherHour>) {
        val currentEpochSeconds = System.currentTimeMillis() / 1000

        element.asJsonObject.getAsJsonArray("hour")
            .filter { it.asJsonObject.get("time_epoch").asLong >= currentEpochSeconds }
            .forEach {
                hourList.add(
                    WeatherHour(
                        dateTime = it.asJsonObject.get("time_epoch").asLong * 1000,
                        conditionText = it.asJsonObject
                            .getAsJsonObject("condition")
                            .get("text").asString,
                        conditionIconUrl = getIconUrlFormatted(
                            it.asJsonObject
                                .getAsJsonObject("condition")
                                .get("icon").asString
                        ),
                        tempCurrent = it.asJsonObject.get("temp_c").asString
                    )
                )
            }
    }

    private fun fillDayList(element: JsonElement, dayList: MutableList<WeatherDay>) {
        val day = element.asJsonObject.get("day")

        dayList.add(
            WeatherDay(
                date = element.asJsonObject.get("date_epoch").asLong * 1000,
                conditionIconUrl = getIconUrlFormatted(
                    day.asJsonObject
                        .getAsJsonObject("condition")
                        .get("icon").asString
                ),
                tempAvg = day.asJsonObject.get("avgtemp_c").asString
            )
        )
    }

    private fun getIconUrlFormatted(conditionUrl: String) =
        if (conditionUrl.startsWith("http",true)) {
            conditionUrl
        } else {
            "https:$conditionUrl"
        }

}