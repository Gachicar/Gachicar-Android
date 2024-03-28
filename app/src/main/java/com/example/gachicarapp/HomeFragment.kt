package com.example.gachicarapp

import ReportListRecyclerViewAdapter
import ReservationListFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gachicarapp.databinding.ActivityRecord2Binding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.Car
import com.example.gachicarapp.retrofit.response.UserAndCount
import com.example.gachicarapp.retrofit.response.UserReportList
import com.example.gachicarapp.retrofit.service.CarService
import com.example.gachicarapp.retrofit.service.ReportService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class HomeFragment : Fragment() {
    // 바인딩 객체 선언을 nullable로 초기화
    private var _binding: ActivityRecord2Binding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportListRecyclerViewAdapter


    // 바인딩 객체에 대한 non-nullable 게터
    private val binding get() = _binding!!

    private lateinit var reportRetrofitAPI: ReportService
    private lateinit var carRetrofitAPI: CarService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 인플레이터로 뷰 바인딩 클래스 인스턴스화
        _binding = ActivityRecord2Binding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = _binding!!.list

        // Set the adapter
        adapter = ReportListRecyclerViewAdapter(emptyList())    // 초기에 빈 리스트로 어댑터 초기화
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(context)

        // API 데이터 가져오기
        fetchReportList()

        // 이 프래그먼트의 루트 뷰 반환
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportRetrofitAPI = RetrofitConnection.getInstance(requireContext()).create(ReportService::class.java)
        carRetrofitAPI = RetrofitConnection.getInstance(requireContext()).create(CarService::class.java)

        getCarInfo()
        getMostUserInGroup()

        // 예약 내역 조회하기 버튼에 클릭 리스너 설정
        binding.btnViewReservation.setOnClickListener {
            // 예약 내역 조회하기 버튼이 클릭되었을 때 ReservationListFragment로 이동
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, ReservationListFragment())
            transaction.addToBackStack(null) // 백스택에 추가하여 이전 Fragment로 돌아갈 수 있도록 함
            transaction.commit()
        }
    }

    private fun getCarInfo() {
        if (!isAdded) {
            // Fragment가 attach되어 있지 않은 경우, API 요청 중단
            return
        }

        // API 호출
        carRetrofitAPI.getCarInfo().enqueue(object : Callback<ApiResponse<Car>> {
            override fun onResponse(
                call: Call<ApiResponse<Car>>,
                response: Response<ApiResponse<Car>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        binding.tvTotalDistance.text = String.format(Locale.getDefault(), "%d Km", it.totalDistance)    // 총 주행 거리
                        binding.tvFrequentDestination.text = it.location    // 자주 가는 목적지
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<Car>>, t: Throwable) {
                // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                Toast.makeText(requireContext(), "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

        })
    }

    private fun getMostUserInGroup() {
        if (!isAdded) {
            // Fragment가 attach되어 있지 않은 경우, API 요청 중단
            return
        }

        // API 호출
        reportRetrofitAPI.getMostUserInGroup()
                .enqueue(object : Callback<ApiResponse<UserAndCount>> {

                    override fun onResponse(call: Call<ApiResponse<UserAndCount>>, response: Response<ApiResponse<UserAndCount>>) {
                        if (response.isSuccessful) {
                            // API 응답이 성공적으로 수신된 경우 UI 업데이트를 수행합니다.
                            response.body()?.data?.let {
                                // 최다 사용자 ui 업데이트
                                binding.tvFrequentUser.text = it.user.userName    // 사용자 이름
                                binding.tvUsageCount.text = it.count.toString()   // 횟수
                            }
                        } else {
                            // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                            Toast.makeText(requireContext(), "그룹 내 최다 사용자 정보 조회에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<UserAndCount>>, t: Throwable) {
                        // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                        Toast.makeText(requireContext(), "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                    }
                })
    }

    private  fun fetchReportList() {
        val service = RetrofitConnection.getInstance(requireContext()).create(ReportService::class.java)
        val call = service.getUserReports()

        call.enqueue(object : Callback<ApiResponse<UserReportList>> {
            override fun onResponse(
                call: Call<ApiResponse<UserReportList>>,
                response: Response<ApiResponse<UserReportList>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { adapter.updateData(it) }
                }
            }

            override fun onFailure(call: Call<ApiResponse<UserReportList>>, t: Throwable) {
                Toast.makeText(requireContext(), "주행 리포트가 없습니다.", Toast.LENGTH_SHORT).show()
            }

        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰가 파괴될 때 바인딩 객체를 해제해 메모리 누수 방지
        _binding = null
    }
}

