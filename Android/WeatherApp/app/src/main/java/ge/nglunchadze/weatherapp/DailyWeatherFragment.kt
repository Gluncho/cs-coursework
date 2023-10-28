package ge.nglunchadze.weatherapp
import kotlin.math.roundToInt
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import ge.nglunchadze.weatherapp.databinding.FragmentDailyWeatherBinding
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
// Unicode for degree symbol
private const val DEGREE = "\u00B0"
const val CITY_NAME_RESULT_KEY = "cityName"
const val CITY_NAME_RESULT_KEY_2 = "cityName2"

/**
 * A simple [Fragment] subclass.
 * Use the [DailyWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DailyWeatherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var city: String = "Tbilisi"
    private var postWeathers: PostWeathers = PostWeathers()
    private lateinit var binding: FragmentDailyWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CITY_NAME_RESULT_KEY, city)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if(savedInstanceState != null){
            city = savedInstanceState.getString(CITY_NAME_RESULT_KEY).toString()
        }
        binding = FragmentDailyWeatherBinding.inflate(inflater, container, false)
        parentFragmentManager.setFragmentResultListener(CITY_NAME_RESULT_KEY_2, viewLifecycleOwner) { _, result ->
            val cityName = result.getString(CITY_NAME_RESULT_KEY_2)
            if (cityName != null) {
                displayWeather(cityName, binding, false)
            }
        }
        displayWeather(city, binding, false)
        binding.georgia.setOnClickListener {
            displayWeather("Tbilisi", binding)
        }
        binding.uk.setOnClickListener {
            displayWeather("London", binding)
        }
        binding.jamaica.setOnClickListener {
            displayWeather("Kingston", binding)
        }
        return binding.root
    }

    private fun displayWeather(cityName: String, binding: FragmentDailyWeatherBinding, should_send: Boolean = true) {
        postWeathers.getWeather(cityName, object : WeatherCallback {
            override fun onSuccess(response: Response<WeatherInfo>) {
                if(!response.isSuccessful){
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorBody).getString("message")
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    return
                }
                val weatherInfo: WeatherInfo = response.body()!!
                city = cityName
                if (should_send){
                    val result = Bundle().apply {
                        putString(CITY_NAME_RESULT_KEY, cityName)
                    }
                    parentFragmentManager.setFragmentResult(CITY_NAME_RESULT_KEY, result)
                }
                binding.locationText.text = cityName
                binding.locationText.isAllCaps = true
                val weatherIcon = weatherInfo.weathers[0].icon
                val weatherDescription = weatherInfo.weathers[0].description
                val temperature = weatherInfo.main.temp.roundToInt()
                val weatherImage = binding.weatherIcon
                Glide.with(this@DailyWeatherFragment)
                    .load("https://openweathermap.org/img/wn/${weatherIcon}@2x.png")
                    .into(weatherImage)
                val weatherTempText = binding.weatherTemp
                weatherTempText.text = "$temperature$DEGREE"

                val weatherDescTextView = binding.weatherDesc
                weatherDescTextView.text = weatherDescription

                val weatherTemperatureBottomText = binding.temperatureTextview
                weatherTemperatureBottomText.text = "$temperature$DEGREE"

                val feelsLikeTextView = binding.feelsLikeTextview
                val feelsLike = weatherInfo.main.feels_like.roundToInt()
                feelsLikeTextView.text = "$feelsLike$DEGREE"


                val humidityTextView = binding.humidityTextview
                val humidity = weatherInfo.main.humidity.roundToInt()
                humidityTextView.text = "$humidity%"

                val pressureTextView = binding.pressureTextview
                val pressure = weatherInfo.main.pressure
                pressureTextView.text = "$pressure"
                setBackgroundColor(weatherInfo);
            }

            override fun onFailure() {
                Toast.makeText(context, "Failed to connect server",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setBackgroundColor(weatherInfo: WeatherInfo) {
        val time = weatherInfo.dt
        val timezone = weatherInfo.timezone
        val timezoneMillis = TimeUnit.SECONDS.toMillis(timezone)
        val targetTimestamp = time * 1000L + timezoneMillis
        val dateFormat = SimpleDateFormat("HH", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        var formattedDate = dateFormat.format(Date(targetTimestamp))
        var hourOfDay: Int = formattedDate.toInt()
        // Check if the hour of day is between 6AM and 6PM
        if (hourOfDay in 6..17) {
            binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.day_color))
        }else{
            binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.night_color))
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WeatherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyWeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}