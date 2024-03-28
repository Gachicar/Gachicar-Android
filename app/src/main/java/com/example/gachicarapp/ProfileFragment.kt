package com.example.gachicarapp
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gachicarapp.databinding.FragmentProfileBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.response.GroupData
import com.example.gachicarapp.retrofit.service.GroupService
import com.example.gachicarapp.retrofit.service.ReportService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onResume() {
        super.onResume()
        getGroupData()
        getReportData()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getGroupData()

        binding.inviteNewMember.setOnClickListener {
            val intent = Intent(activity, MemberSettingActivity3::class.java)
            startActivity(intent)
        }

        binding.deleteMember.setOnClickListener {
            val intent = Intent(activity, EditMActivity::class.java)
            startActivity(intent)
        }

        binding.editCar.setOnClickListener {
            val intent = Intent(activity, EditCarActivity::class.java)
            startActivity(intent)
        }

        binding.editGroupDetail.setOnClickListener {
            val intent = Intent(activity, EditGActivity::class.java)
            startActivity(intent)
        }

        binding.editNick.setOnClickListener {
            val intent = Intent(activity, EditNickActivity::class.java)
            startActivity(intent)
        }

        binding.reset.setOnClickListener {
            val intent = Intent(activity, MemberSettingActivity1::class.java)
            startActivity(intent)
        }
    }

    private fun getGroupData() {
        val retrofitAPI = RetrofitConnection.getInstance(requireContext()).create(GroupService::class.java)

        // API 호출
        retrofitAPI.getGroupInfo()
            .enqueue(object : Callback<ApiResponse<GroupData>> {
                override fun onResponse(call: Call<ApiResponse<GroupData>>, response: Response<ApiResponse<GroupData>>) {
                    if (response.isSuccessful && response.body() != null) {
                    // 성공적으로 데이터를 받아온 경우 UI 업데이트
                        response.body()?.data?.let { updateUIWithGroupData(it) }

                    } else {
                        // 데이터를 받아오는데 실패한 경우
                            // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                            Toast.makeText(requireContext(), "그룹정보 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onFailure(call: Call<ApiResponse<GroupData>>, t: Throwable) {
                // API 호출 자체가 실패한 경우
                Toast.makeText(requireContext(), "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun getReportData() {
        val retrofitAPI = RetrofitConnection.getInstance(requireContext()).create(ReportService::class.java)

        // API 호출
        retrofitAPI.getDriveReport()
            .enqueue(object : Callback<ApiResponse<DriveReport>> {
                override fun onResponse(
                    call: Call<ApiResponse<DriveReport>>,
                    response: Response<ApiResponse<DriveReport>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        // API 응답이 성공적으로 수신된 경우 UI 업데이트를 수행합니다.
                        response.body()?.data?.let { updateReportUI(it) }
                    } else {
                        // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                        Toast.makeText(requireContext(), "리포트 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<DriveReport>>, t: Throwable) {
                    // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                    Toast.makeText(requireContext(), "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
    }
    private fun updateUIWithGroupData(showGroupInfo: GroupData) {
        // UI 업데이트 로직
        val groupName = showGroupInfo.name
        val description = showGroupInfo.desc
        val groupLeader = showGroupInfo.groupManager.name
        val carNickName = showGroupInfo.car.carName

        binding.tvGroupName.text = groupName
        binding.tvOneLineDesc.text = description
        binding.tvGroupLeaderName.text = groupLeader
        binding.tvCarNickname.text = carNickName

    }
    private fun updateReportUI(report: DriveReport) {
        val userName = report.userName
        val carName = report.car.carName
        val carNumber = report.car.carNumber
        val departure = report.departure
        val destination = report.destination
        val driveTime = report.driveTime
        val startTime = report.startTime
        val endTime = report.endTime


        binding.carNumber.text = carNumber
        binding.curLoc.text = departure
        binding.location.text= destination
        binding.latestDate.text = startTime.toString()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}