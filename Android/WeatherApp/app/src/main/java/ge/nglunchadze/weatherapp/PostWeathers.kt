package ge.nglunchadze.weatherapp

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class PostWeathers {
    private val key = "322d921b0565ac23f8f648e6edd81b3e"

    private val url = "https://api.openweathermap.org/data/2.5/"

    private val iconUrl = "https://openweathermap.org/img/wn/"

    private var client: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getWeather(cityName : String, callback: WeatherCallback) {
        val api = client.create(WeatherAPI::class.java)

        val getPostsCall = api.getWeather(cityName, key, "metric")

        getPostsCall.enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(
                call: Call<WeatherInfo>,
                response: Response<WeatherInfo>
            ) {
                callback.onSuccess(response)
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                callback.onFailure()
            }
        })
    }

    fun getHourlyWeather(cityName : String, callback: HourlyWeatherCallback) {

        val api = client.create(WeatherAPI::class.java)

        val getPostsCall = api.getHourlyWeather(cityName, key, "metric")

        getPostsCall.enqueue(object : Callback<HourlyWeatherInfo> {
            override fun onResponse(
                call: Call<HourlyWeatherInfo>,
                response: Response<HourlyWeatherInfo>
            ) {
                callback.onSuccess(response)
            }

            override fun onFailure(call: Call<HourlyWeatherInfo>, t: Throwable) {
                callback.onFailure()
            }
        })
    }

}

interface WeatherAPI {
    @GET("weather")
    fun getWeather(
        @Query("q")
        cityName: String,
        @Query("appid")
        apiKey: String,
        @Query("units")
        units: String
    ): Call<WeatherInfo>

    @GET("forecast")
    fun getHourlyWeather(
        @Query("q")
        cityName: String,
        @Query("appid")
        apikey: String,
        @Query("units")
        units: String
    ): Call<HourlyWeatherInfo>
}

data class Weather(
    val description: String,
    val icon: String
)

data class WeatherTemperatureInfo(
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Double,
)

data class Sys(
    val sunrise: Long,
    val sunset: Long
)

data class WeatherInfo(
    @SerializedName("weather")
    val weathers: ArrayList<Weather>,
    val main: WeatherTemperatureInfo,
    val dt: Long,
    val sys: Sys,
    val timezone: Long
)
data class City(
    val name: String,
    val timezone: Long
)

data class HourlyWeatherInfoHelper(
    @SerializedName("weather")
    val weathers: ArrayList<Weather>,
    val main: WeatherTemperatureInfo,
    val dt: Long,
    val sys: Sys
)
data class HourlyWeatherInfo(
    val list: List<HourlyWeatherInfoHelper>,
    val city: City
)

interface WeatherCallback {
    fun onSuccess(weatherInfo: Response<WeatherInfo>)
    fun onFailure()
}

interface HourlyWeatherCallback {
    fun onSuccess(hourlyWeatherInfo: Response<HourlyWeatherInfo>)
    fun onFailure()
}