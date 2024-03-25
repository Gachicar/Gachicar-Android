package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gachicarapp.databinding.FragmentStatsBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.statistic_r_Response
import com.example.gachicarapp.retrofit.service.AppServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private var lastFuelQualityData: statistic_r_Response? = null // 마지막으로 받은 연료 정보를 저장할 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRefreshButton()
        setReportButton() // Report 버튼 리스너 설정
        getFuelQualityData() // 프래그먼트가 처음 생성될 때 연료 정보를 가져옴
    }

    private fun setReportButton() {
        binding.btnReport.setOnClickListener {
            val intent = Intent(activity, ReportActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setRefreshButton() {
        binding.btnRefresh.setOnClickListener {
            getFuelQualityData()
        }
    }

    private fun getFuelQualityData() {
        val retrofitAPI = RetrofitConnection.getInstance().create(AppServices::class.java)

        retrofitAPI.getFuelData().enqueue(object : Callback<statistic_r_Response> {
            override fun onResponse(call: Call<statistic_r_Response>, response: Response<statistic_r_Response>) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "최신 정보 업데이트 완료!", Toast.LENGTH_SHORT).show()
                    response.body()?.let { updateFuelUI(it) }
                } else {
                    Toast.makeText(activity, "업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<statistic_r_Response>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun updateFuelUI(fuelQualityData: statistic_r_Response) {
        lastFuelQualityData = fuelQualityData
        val fuelData = fuelQualityData.data.oilStatus
        binding.tvCount.text = fuelData.toString()

        when (fuelData) {
            in 85..100 -> {
                binding.tvTitle.text = "연료상태 : 좋음"
                binding.imgBg.setImageResource(R.drawable.bg_good)
            }
            in 50..84 -> {
                binding.tvTitle.text = "연료상태 : 보통"
                binding.imgBg.setImageResource(R.drawable.bg_soso)
            }
            in 25..49 -> {
                binding.tvTitle.text = "연료상태 : 나쁨"
                binding.imgBg.setImageResource(R.drawable.bg_bad)
            }
            else -> {
                binding.tvTitle.text = "연료상태 : 비상"
                binding.imgBg.setImageResource(R.drawable.bg_worst)
            }
        }

        // 현재 시간 업데이트
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        binding.tvCheckTime.text = dateFormatter.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
