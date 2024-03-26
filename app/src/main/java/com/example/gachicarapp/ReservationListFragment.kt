import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gachicarapp.databinding.FragmentReservationListBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.CarAndReport
import com.example.gachicarapp.retrofit.service.ReportService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservationRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentReservationListBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.list

        // Set the adapter
        adapter = ReservationRecyclerViewAdapter(emptyList()) // 초기에 빈 리스트로 어댑터 초기화
        recyclerView.adapter = adapter

        // Layout Manager 설정
        recyclerView.layoutManager = LinearLayoutManager(context)

        // API에서 데이터 가져오기
        fetchData()

        return view
    }

    // API에서 데이터 가져오는 함수
    private fun fetchData() {
        val service = RetrofitConnection.getInstance(requireContext())?.create(ReportService::class.java)
        val call = service?.getReserveReports()

        call?.enqueue(object : Callback<ApiResponse<CarAndReport>> {
            override fun onResponse(
                call: Call<ApiResponse<CarAndReport>>,
                response: Response<ApiResponse<CarAndReport>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val reservations = apiResponse.data?.report // null 체크 추가
                        reservations?.let { adapter.updateData(it) } // null 체크 후 호출
                    }
                }
            }


            override fun onFailure(call: Call<ApiResponse<CarAndReport>>, t: Throwable) {
                // API 호출 실패 시 처리
            }
        })
    }
}
