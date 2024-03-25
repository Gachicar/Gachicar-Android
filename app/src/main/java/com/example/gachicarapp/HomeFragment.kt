package com.example.gachicarapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gachicarapp.databinding.ActivityRecord2Binding
import com.example.gachicarapp.databinding.FragmentHomeBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.MostUser
import com.example.gachicarapp.retrofit.response.getCarInfo
import com.example.gachicarapp.retrofit.service.AppServices
import com.example.gachicarapp.retrofit.service.ReportService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class HomeFragment : Fragment() {
    // 바인딩 객체 선언을 nullable로 초기화
    private var _binding: ActivityRecord2Binding? = null

    // 바인딩 객체에 대한 non-nullable 게터
    private val binding get() = _binding!!

    private lateinit var reportRetrofitAPI: ReportService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 인플레이터로 뷰 바인딩 클래스 인스턴스화
        _binding = ActivityRecord2Binding.inflate(inflater, container, false)

        // 이 프래그먼트의 루트 뷰 반환
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportRetrofitAPI = RetrofitConnection.getInstance(requireContext()).create(ReportService::class.java)
        getMostUserInGroup()
    }

    private fun getMostUserInGroup() {
        if (!isAdded) {
            // Fragment가 attach되어 있지 않은 경우, API 요청 중단
            return
        }

        // API 호출
        reportRetrofitAPI.getMostUserInGroup()
                .enqueue(object : Callback<ApiResponse<MostUser>> {

                    override fun onResponse(call: Call<ApiResponse<MostUser>>, response: Response<ApiResponse<MostUser>>) {
                        if (response.isSuccessful) {
                            // API 응답이 성공적으로 수신된 경우 UI 업데이트를 수행합니다.
                            response.body()?.let { updateMostUserUI(it.data) }
                        } else {
                            // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                            Toast.makeText(requireContext(), "그룹 내 최다 사용자 정보 조회에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<MostUser>>, t: Throwable) {
                        // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                        Toast.makeText(requireContext(), "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                    }
                })
    }

    // 최다 사용자 ui 업데이트
    private fun updateMostUserUI(data: MostUser) {
        val userName = data.userName
        Timber.tag("최다 사용자").e(userName)
        binding.tvFrequentUser.text = userName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰가 파괴될 때 바인딩 객체를 해제해 메모리 누수 방지
        _binding = null
    }
}

