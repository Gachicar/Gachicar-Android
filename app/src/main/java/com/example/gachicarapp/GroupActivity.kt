package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.FragmentNoGroupBinding
import com.example.gachicarapp.databinding.FragmentProfileBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.GetGroupInfo
import com.example.gachicarapp.retrofit.service.GroupService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupActivity : AppCompatActivity() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var binding_no_group: FragmentNoGroupBinding
//    private lateinit var binding_create_group: FragmentStep01Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getGroupData()
    }

    private fun getGroupData() {
        val retrofitAPI = RetrofitConnection.getInstance(this).create(GroupService::class.java)

        // API 호출
        retrofitAPI.getGroupInfo().enqueue(object : Callback<GetGroupInfo> {
            override fun onResponse(call: Call<GetGroupInfo>, response: Response<GetGroupInfo>) {
                if (response.isSuccessful && response.body() != null) {

                    binding = FragmentProfileBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    // 성공적으로 데이터를 받아온 경우 UI 업데이트
                    val groupInfo = response.body()!!
                    updateUIWithGroupData(groupInfo.data)

                } else {
                    // 데이터를 받아오는데 실패한 경우
//                    showError("그룹 정보를 가져오는데 실패했습니다.")

                    showNoGroupPage()

                }
            }

            override fun onFailure(call: Call<GetGroupInfo>, t: Throwable) {
                // API 호출 자체가 실패한 경우
                showError("API 호출 중 오류가 발생했습니다.")
            }
        })
    }

    private fun updateUIWithGroupData(data: GetGroupInfo.Data) {
        // UI 업데이트 로직
        binding.tvGroupName.text = data.name
        binding.tvOneLineDesc.text = data.desc
        binding.tvGroupLeaderName.text = data.groupManager.name
        binding.tvCarNickname.text = data.car.carName
        // 추가적인 UI 업데이트 로직은 여기에 구현하세요.
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
    }
}
