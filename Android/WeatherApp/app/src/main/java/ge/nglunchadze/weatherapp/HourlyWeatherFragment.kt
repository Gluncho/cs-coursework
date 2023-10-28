package ge.nglunchadze.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nglunchadze.weatherapp.databinding.FragmentHourlyWeatherBinding
import org.json.JSONObject
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HourlyWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HourlyWeatherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var postWeathers: PostWeathers = PostWeathers()
    private lateinit var binding: FragmentHourlyWeatherBinding
    private lateinit var adapter: MyRecyclerViewAdapter
    private var city: String = "Tbilisi"

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
        binding = FragmentHourlyWeatherBinding.inflate(layoutInflater, container, false)
        binding.georgia.isClickable = true
        binding.georgia.setOnClickListener {
            displayHourlyWeather("Tbilisi", binding)
        }
        binding.uk.setOnClickListener {
            displayHourlyWeather("London", binding)
        }
        binding.jamaica.setOnClickListener {
            displayHourlyWeather("Kingston", binding)
        }
        val recyclerView = binding.recyclerView
        adapter = MyRecyclerViewAdapter()
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = MyDividerItemDecoration(requireContext(), R.drawable.divider)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.context, R.drawable.divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
        parentFragmentManager.setFragmentResultListener(CITY_NAME_RESULT_KEY, viewLifecycleOwner) { _, result ->
            val cityName = result.getString(CITY_NAME_RESULT_KEY)
            if (cityName != null) {
                displayHourlyWeather(cityName, binding, false)
            }
        }
        displayHourlyWeather(city, binding, false)
        return binding.root
    }

    private fun displayHourlyWeather(cityName: String, binding: FragmentHourlyWeatherBinding,should_send: Boolean = true) {
        postWeathers.getHourlyWeather(cityName, object : HourlyWeatherCallback{
            override fun onSuccess(response: Response<HourlyWeatherInfo>) {
                if(!response.isSuccessful){
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorBody).getString("message")
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    return
                }
                val hourlyWeatherInfo: HourlyWeatherInfo = response.body()!!
                if (should_send){
                    val result = Bundle().apply {
                        putString(CITY_NAME_RESULT_KEY_2, cityName)
                    }
                    parentFragmentManager.setFragmentResult(CITY_NAME_RESULT_KEY_2, result)
                }
                binding.locationText.text = cityName
                city = cityName
                binding.locationText.isAllCaps = true
                adapter.data = hourlyWeatherInfo
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure() {
                Toast.makeText(context, "Failed to connect server",Toast.LENGTH_LONG).show()
            }
        })
    }
}

