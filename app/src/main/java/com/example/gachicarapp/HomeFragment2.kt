package com.example.gachicarapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gachicarapp.databinding.FragmentHomeBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.MostUser
import com.example.gachicarapp.retrofit.response.getCarInfo
import com.example.gachicarapp.retrofit.service.AppServices
import com.example.gachicarapp.retrofit.service.CarService
import com.example.gachicarapp.retrofit.service.ReportService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment2 : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val carRetrofitAPI = RetrofitConnection.getInstance(requireContext()).create(CarService::class.java)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCarInfo()

    }

    private fun getCarInfo() {

        // API 호출
        carRetrofitAPI.getCarHomeInfo()
            .enqueue(object : Callback<getCarInfo> {
                override fun onResponse(call: Call<getCarInfo>, response: Response<getCarInfo>) {
                    if (response.isSuccessful) {
                        // API 응답이 성공적으로 수신된 경우 UI 업데이트를 수행합니다.
                        response.body()?.let { updateHomeUI(it) }
                    } else {
                        // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                        Toast.makeText(requireContext(), "차량정보 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<getCarInfo>, t: Throwable) {
                    // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                    Toast.makeText(requireContext(), "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
    }

    private fun updateHomeUI(reportQualityData: getCarInfo) {
        val carName = reportQualityData.data.carName
        val carNumber = reportQualityData.data.carNumber
        val curLoc = reportQualityData.data.curLoc
        val location = reportQualityData.data.location
        val latestDate = reportQualityData.data.latestDate
        // 리포트 작성
        val reportText = "나의 공유차량 정보\n\n"+
                "공유차량 이름: $carName\n" +
                "공유차량 번호: $carNumber\n\n" +
                "현재위치: $curLoc\n" +
                "자주가는 목적지: $location\n\n" +
                "최근 사용 날짜: $latestDate\n"
        binding.tvHomeReportCar.text = reportText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
