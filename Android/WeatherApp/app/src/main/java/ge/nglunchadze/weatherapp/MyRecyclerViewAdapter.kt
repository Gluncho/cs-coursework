package ge.nglunchadze.weatherapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.nglunchadze.weatherapp.databinding.FragmentHourlyWeatherBinding
import ge.nglunchadze.weatherapp.databinding.HourlyWeatherRowItemBinding
import kotlin.math.roundToInt
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val DEGREE = "\u00B0"
class MyRecyclerViewAdapter() :
    RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    var data: HourlyWeatherInfo = HourlyWeatherInfo(emptyList<HourlyWeatherInfoHelper>(), City("Tbilisi", 14400))
    inner class ViewHolder(val view: View, val binding: HourlyWeatherRowItemBinding) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int){
            var curr: HourlyWeatherInfoHelper = data.list[position]
            var weatherIcon = curr.weathers[0].icon
            var timezone = data.city.timezone
            Glide.with(view.context)
                .load("https://openweathermap.org/img/wn/${weatherIcon}@2x.png")
                .into(binding.rowItemIcon)
            binding.rowItemTemp.text = "${curr.main.temp.roundToInt()}$DEGREE"
            binding.rowItemDesc.text = curr.weathers[0].description
            binding.rowItemDate.text = formatDate(curr.dt, timezone)
        }
    }

    private fun formatDate(time: Long, timezone: Long): String {
        val timezoneMillis = TimeUnit.SECONDS.toMillis(timezone)
        val targetTimestamp = time * 1000L + timezoneMillis
        val dateFormat = SimpleDateFormat("hh a dd MMMM", Locale.getDefault())

        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        var formattedDate = dateFormat.format(Date(targetTimestamp))
        val words = formattedDate.split(" ")
        val capitalizedWord = words[1].uppercase()
        formattedDate = "${words[0]} $capitalizedWord ${words[2]} ${words[3]}"
        return formattedDate
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = HourlyWeatherRowItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )

        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.bind(position)
    }

    override fun getItemCount() = data.list.size

}
