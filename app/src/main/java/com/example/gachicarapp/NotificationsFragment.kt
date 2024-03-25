package com.example.gachicarapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gachicarapp.databinding.FragmentNotificationsBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ReserveData
import com.example.gachicarapp.retrofit.service.AppServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getReserve()
    }

    private fun getReserve() {
        val retrofitAPI = RetrofitConnection.getInstance(requireContext()).create(AppServices::class.java)

        // API 호출
        retrofitAPI.getReserveData()
            .enqueue(object : Callback<ReserveData> {
                override fun onResponse(call: Call<ReserveData>, response: Response<ReserveData>) {
                    try {
                        if (response.isSuccessful) {
                            // API 응답이 성공적으로 수신된 경우 UI 업데이트를 수행합니다.
                            response.body()?.let { updateReserveUI(it) }
                        } else {
                            // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                            Toast.makeText(requireContext(), "예약 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                            binding.tvReserveReport.text = "예약내역이 없습니다."
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ReserveData>, t: Throwable) {
                    // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                    Toast.makeText(requireContext(), "인터넷 연결이 없어 예약 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    binding.tvReserveReport.text = "예약내역이 없습니다."
                    t.printStackTrace()
                }
            })
    }

    private fun updateReserveUI(reportQualityData: ReserveData) {
        try {
            val userName = reportQualityData.data.userName
            val carName = reportQualityData.data.car.carName
            val carNumber = reportQualityData.data.car.carNumber
            val reserveTime = reportQualityData.data.endTime
            val reservePlace = reportQualityData.data.destination
            val curLoc = reportQualityData.data.car.curLoc
            val latestDate = reportQualityData.data.car.latestDate

            val reportText = "사용자의 최근 예약 내역 \n\n"+
                    "사용자: $userName\n\n" +
                    "차량 이름: $carName\n" +
                    "차량 번호: $carNumber\n\n" +
                    "예약시간: $reserveTime\n" +
                    "예약장소: $reservePlace\n\n" +
                    "현재 위치: $curLoc\n\n" +
                    "최근 사용 시간: $latestDate\n"

            binding.tvReserveReport.text = reportText
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
