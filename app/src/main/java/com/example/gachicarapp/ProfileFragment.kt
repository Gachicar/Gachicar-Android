package com.example.gachicarapp

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gachicarapp.databinding.FragmentProfileBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.Car
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.response.GroupData
import com.example.gachicarapp.retrofit.response.deleteUser
import com.example.gachicarapp.retrofit.service.GroupService
import com.example.gachicarapp.retrofit.service.LoginService
import com.example.gachicarapp.retrofit.service.ReportService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var viewModel: SharedViewModel
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
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.userNickname.observe(viewLifecycleOwner, Observer { nickname ->
            // 닉네임이 업데이트되면 UI 업데이트
            binding.tvNickname.text = nickname
        })
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

//        binding.editGroupDetail.setOnClickListener {
//            val intent = Intent(activity, EditGActivity::class.java)
//            startActivity(intent)
//        }

        binding.editNick.setOnClickListener {
            val intent = Intent(activity, EditNickActivity::class.java)
            startActivity(intent)
        }

        binding.reset.setOnClickListener {
            val intent = Intent(activity, MemberSettingActivity1::class.java)
            startActivity(intent)
        }
        binding.withdrawl.setOnClickListener {
            showConfirmDeleteDialog()
        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        viewModel.groupData.observe(this, Observer { groupData ->
            // 여기에서 groupData를 사용하여 UI 업데이트
            updateUIWithGroupData(groupData)
        })
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
        val nickname=showGroupInfo.groupManager.name
        val memberNames = showGroupInfo.members.joinToString(separator = ", ") { it.userName }
        val carNickName = showGroupInfo.car.carName
        val carNumber = showGroupInfo.car.carNumber
        val curLoc =  showGroupInfo.car.curLoc
        val location =  showGroupInfo.car.location
        val latestDate = showGroupInfo.car.latestDate

        binding.tvGroupName.text = groupName
        binding.tvOneLineDesc.text = description
        binding.tvGroupLeaderName.text = groupLeader
        binding.tvNickname.text= nickname
        binding.tvCarNickname.text = carNickName
        binding.carNumber.text = carNumber
        binding.tvMember.text= memberNames
//      binding.tvGroupMember.text=memberNames
//        binding.curLoc.text = curLoc
//        binding.location.text= location
//        binding.latestDate.text = latestDate.toString()

    }
    private fun updateUIWithNick(showGroupInfo: GroupData) {
        // UI 업데이트 로직
        val nickname=showGroupInfo.groupManager.name

        binding.tvNickname.text= nickname

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


//        binding.carNumber.text = carNumber
        binding.curLoc.text = departure
        binding.location.text= destination
        binding.latestDate.text = startTime.toString()

    }

    private fun showConfirmDeleteDialog() {
        AlertDialog.Builder(context).apply {
            setTitle("계정 탈퇴")
            setMessage("정말로 탈퇴하시겠습니까?\n이 작업은 되돌릴 수 없습니다.")
            setPositiveButton("예") { _, _ ->
                deleteUserAccount()
            }
            setNegativeButton("아니오", null)
            show()
        }
    }


    private fun deleteUserAccount() {
        val sharedPreferences = requireActivity().getSharedPreferences("tokens", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)
        if (token == null) {
            Timber.tag(TAG).e("Access Token is missing")
            return
        }

        val service = RetrofitConnection.getInstanceWithoutToken(requireContext()).create(LoginService::class.java)
        val call = service.deleteUserAccount("Bearer $token")

        call.enqueue(object : Callback<deleteUser> {
            override fun onResponse(call: Call<deleteUser>, response: Response<deleteUser>) {
                if (response.isSuccessful) {
                    // 회원 탈퇴 성공 알림창 표시
                    showDeletionSuccessDialog()
                } else {
                    Timber.tag(TAG).e("회원 탈퇴 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<deleteUser>, t: Throwable) {
                Timber.tag(TAG).e("통신 실패: ${t.message}")
            }
        })
    }

    private fun showDeletionSuccessDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("탈퇴 완료")
            setMessage("탈퇴가 완료되었습니다.")
            setPositiveButton("확인") { _, _ ->
                // '확인' 버튼을 누르면 로그아웃하고 로그인 화면으로 이동
                logoutAndNavigateToLogin()
            }
            setCancelable(false) // 다이얼로그 바깥을 눌러도 닫히지 않도록 설정
            show()
        }
    }


    private fun logoutAndNavigateToLogin() {
        // SharedPreferences에서 토큰 제거
        val sharedPreferences = requireActivity().getSharedPreferences("tokens", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // 로그인 화면으로 이동
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // 현재 Activity 종료
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}