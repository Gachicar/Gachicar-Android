package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gachicarapp.databinding.FragmentNoGroupBinding
import com.example.gachicarapp.databinding.FragmentProfileBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.GroupData
import com.example.gachicarapp.retrofit.service.GroupService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupActivity : AppCompatActivity() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var binding_no_group: FragmentNoGroupBinding
    private lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        getGroupData()

        // LiveData 관찰
        viewModel.userNickname.observe(this, Observer { nickname ->
            // 닉네임이 업데이트되면 UI 업데이트
            binding.tvNickname.text = nickname
        })

    }

    private fun getGroupData() {
        val retrofitAPI = RetrofitConnection.getInstance(this).create(GroupService::class.java)

        // API 호출
        retrofitAPI.getGroupInfo().enqueue(object : Callback<ApiResponse<GroupData>> {
            override fun onResponse(call: Call<ApiResponse<GroupData>>, response: Response<ApiResponse<GroupData>>) {
                if (response.isSuccessful && response.body()?.data != null) {

                    binding = FragmentProfileBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    // 성공적으로 데이터를 받아온 경우 UI 업데이트
                    val groupInfo = response.body()!!
                    updateUIWithGroupData(groupInfo.data)

                } else {
                    // 데이터를 받아오는데 실패한 경우
                    showError("그룹 정보를 가져오는데 실패했습니다.")

                    showNoGroupPage()

                }
            }

            override fun onFailure(call: Call<ApiResponse<GroupData>>, t: Throwable) {
                // API 호출 자체가 실패한 경우
                showError("API 호출 중 오류가 발생했습니다.")
            }
        })
    }

    private fun updateUIWithGroupData(showGroupInfo: GroupData) {
        // UI 업데이트 로직
        val groupName = showGroupInfo.name
        val description = showGroupInfo.desc
        val groupLeader = showGroupInfo.groupManager.name
        val nickname=showGroupInfo.groupManager.name
        //val memberNames = showGroupInfo.members.joinToString(separator = ", ") { it.userName }
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
        // binding.tvGroupMember.text=memberNames
//        binding.curLoc.text = curLoc
//        binding.location.text= location
//        binding.latestDate.text = latestDate.toString()

    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showNoGroupPage() {
        binding_no_group = FragmentNoGroupBinding.inflate(layoutInflater)
        setContentView(binding_no_group.root)

        // Set OnClickListener for the button in binding_no_group
        val createGroupButton: Button = findViewById(R.id.btnCreateGroup)
        createGroupButton.setOnClickListener {

            val intent = Intent(this, CreateGroupActivity::class.java)
            startActivity(intent)
        }

        binding_no_group.btnLater.setOnClickListener {
            val intentMain = Intent(this, GroupLaterActivity::class.java)
            startActivity(intentMain)

        }
    }


}
