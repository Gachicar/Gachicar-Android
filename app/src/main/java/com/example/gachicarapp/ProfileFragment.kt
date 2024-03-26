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
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.response.GetGroupInfo
import com.example.gachicarapp.retrofit.service.AppServices
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

        val retrofitAPI = RetrofitConnection.getInstance(requireContext()).create(AppServices::class.java)

        binding.inviteNewMember.setOnClickListener {
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
        val retrofitAPI = RetrofitConnection.getInstance(requireContext()).create(AppServices::class.java)

        // API 호출
        retrofitAPI.getGroupInfo()
            .enqueue(object : Callback<GetGroupInfo> {
                override fun onResponse(call: Call<GetGroupInfo>, response: Response<GetGroupInfo>) {
                    if (response.isSuccessful) {
                    // 성공적으로 데이터를 받아온 경우 UI 업데이트
                        response.body()?.let { updateUIWithGroupData(it) }
                } else {
                    // 데이터를 받아오는데 실패한 경우
                        // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                        Toast.makeText(requireContext(), "그룹정보 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetGroupInfo>, t: Throwable) {
                // API 호출 자체가 실패한 경우
                Toast.makeText(requireContext(), "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun getReportData() {
        val retrofitAPI = RetrofitConnection.getInstance(requireContext()).create(AppServices::class.java)

        // API 호출
        retrofitAPI.getDriveReport()
            .enqueue(object : Callback<DriveReport> {
                override fun onResponse(call: Call<DriveReport>, response: Response<DriveReport>) {
                    if (response.isSuccessful) {
                        // API 응답이 성공적으로 수신된 경우 UI 업데이트를 수행합니다.
                        response.body()?.let { updateReportUI(it) }
                    } else {
                        // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                        Toast.makeText(requireContext(), "리포트 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DriveReport>, t: Throwable) {
                    // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                    Toast.makeText(requireContext(), "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
    }
    private fun updateUIWithGroupData(showGroupInfo: GetGroupInfo) {
        // UI 업데이트 로직
        val groupName = showGroupInfo.data.name
        val description = showGroupInfo.data.desc
        val groupLeader = showGroupInfo.data.groupManager.name
        val carNickName = showGroupInfo.data.car.carName

        binding.tvGroupName.text = groupName
        binding.tvOneLineDesc.text = description
        binding.tvGroupLeaderName.text = groupLeader
        binding.tvCarNickname.text = carNickName

    }
    private fun updateReportUI(reportQualityData: DriveReport) {
        val userName = reportQualityData.data.userName
        val carName = reportQualityData.data.car.carName
        val carNumber = reportQualityData.data.car.carNumber
        val departure = reportQualityData.data.departure
        val destination = reportQualityData.data.destination
        val driveTime = reportQualityData.data.driveTime
        val startTime = reportQualityData.data.startTime
        val endTime = reportQualityData.data.endTime


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