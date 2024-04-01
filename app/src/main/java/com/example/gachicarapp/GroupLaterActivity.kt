package com.example.gachicarapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityGrouplaterBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.GroupData
import com.example.gachicarapp.retrofit.response.deleteUser
import com.example.gachicarapp.retrofit.service.GroupService
import com.example.gachicarapp.retrofit.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class GroupLaterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrouplaterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)

        super.onCreate(savedInstanceState)
        getGroupData()
        binding = ActivityGrouplaterBinding.inflate(layoutInflater)

        setContentView(binding.root)



        binding.editNick.setOnClickListener {
            val intent = Intent(this, EditNickActivity::class.java)
            startActivity(intent)
        }

        binding.withdrawl.setOnClickListener {
            showConfirmDeleteDialog()
        }
    }

    private fun showConfirmDeleteDialog() {
        AlertDialog.Builder(this).apply {
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
        val sharedPreferences = getSharedPreferences("tokens", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)
        if (token == null) {
            Timber.e("Access Token is missing")
            return
        }

        val service = RetrofitConnection.getInstanceWithoutToken(this).create(
            LoginService::class.java)
        val call = service.deleteUserAccount("Bearer $token")

        call.enqueue(object : Callback<deleteUser> {
            override fun onResponse(call: Call<deleteUser>, response: Response<deleteUser>) {
                if (response.isSuccessful) {
                    // 회원 탈퇴 성공 알림창 표시
                    showDeletionSuccessDialog()
                } else {
                    Timber.e("회원 탈퇴 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<deleteUser>, t: Throwable) {
                Timber.e("통신 실패: ${t.message}")
            }
        })
    }
    private fun getGroupData() {
        val retrofitAPI = RetrofitConnection.getInstance(this).create(GroupService::class.java)
        retrofitAPI.getGroupInfo().enqueue(object : Callback<ApiResponse<GroupData>> {
            override fun onResponse(call: Call<ApiResponse<GroupData>>, response: Response<ApiResponse<GroupData>>) {
                if (response.isSuccessful && response.body() != null) {
                    updateUIWithNick(response.body()!!.data)
                } else {
                    Toast.makeText(this@GroupLaterActivity, "Failed to fetch group information", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<GroupData>>, t: Throwable) {
                Toast.makeText(this@GroupLaterActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUIWithNick(showGroupInfo: GroupData) {
        // UI 업데이트 로직
        val nickname=showGroupInfo.groupManager.name

        binding.tvNickname.text= nickname

    }
    private fun showDeletionSuccessDialog() {
        AlertDialog.Builder(this).apply {
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
        val sharedPreferences = getSharedPreferences("tokens", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // 로그인 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // 현재 Activity 종료
    }
}
