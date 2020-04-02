package org.nunocky.weatherapi_flux_study01.api

import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherApi {
    @GET("/forecast/webservice/json/v1")
    suspend fun getWhether(@Query("city") cityId: String?): WeatherResponse
}