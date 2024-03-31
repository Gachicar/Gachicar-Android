package com.example.gachicarapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.request.AcceptInvitation
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.response.GroupData
import com.example.gachicarapp.retrofit.service.GroupService
import com.example.gachicarapp.retrofit.service.ReportService
import com.example.gachicarapp.retrofit.sse.startSSEConnection
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        setContentView(R.layout.activity_main)

        startSSEConnection(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            // 모든 아이콘을 초기 상태로 설정
            resetIconsToOffState()

            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_voice -> {
                    selectedFragment = VoiceFragment()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.on_voice_ic)
                }
                R.id.nav_stats -> {
                    selectedFragment = StatsFragment()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.on_stats_ic)
                }
                R.id.nav_home -> {
                    selectedFragment = HomeFragment()
                }
                R.id.nav_notifications -> {
                    selectedFragment = NotificationsFragment()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.on_alarm_ic)
                }
                R.id.nav_profile -> {
                    selectedFragment = ProfileFragment()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.on_profile_ic)
                }
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }

            true
        }

        // 기본으로 선택된 탭 설정
        bottomNavigationView.selectedItemId = R.id.nav_home
    }
    fun refreshGroupInfo() {
        // RetrofitConnection 인스턴스를 통해 GroupService 인터페이스의 구현체를 얻음
        val retrofitAPI = RetrofitConnection.getInstance(this).create(GroupService::class.java)

        // 그룹 정보를 불러오는 API 호출
        retrofitAPI.getGroupInfo().enqueue(object : Callback<ApiResponse<GroupData>> {
            override fun onResponse(call: Call<ApiResponse<GroupData>>, response: Response<ApiResponse<GroupData>>) {
                if (response.isSuccessful && response.body() != null) {
                    // API 응답이 성공하고, body가 null이 아니라면 ViewModel의 LiveData 업데이트
                    viewModel.groupData.value = response.body()?.data
                } else {
                    // 데이터를 받아오는 데 실패한 경우, 사용자에게 에러 메시지 표시
                    Toast.makeText(applicationContext, "그룹 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_LONG).show()
                    Log.e("GroupInfoRefresh", "Failed to fetch group data: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<GroupData>>, t: Throwable) {
                // API 호출 자체가 실패한 경우, 사용자에게 알림과 함께 로깅 수행
                Toast.makeText(applicationContext, "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show()
                Log.e("GroupInfoRefresh", "API call failed", t)
            }
        })
    }


    private fun resetIconsToOffState() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.apply {
            findItem(R.id.nav_voice).icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.off_voice_ic)
            findItem(R.id.nav_stats).icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.off_stats_ic)
            findItem(R.id.nav_notifications).icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.off_alarm_ic)
            findItem(R.id.nav_profile).icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.off_profile_ic)
        }
    }

    override fun onClickYesButton(id: Int) {
        // 그룹 초대 수락 처리
        val retrofitAPI = RetrofitConnection.getInstance(this).create(GroupService::class.java)
        retrofitAPI.postAcceptInvitation(AcceptInvitation(groupId = id))
            .enqueue(object : Callback<ApiResponse<AcceptInvitation>> {
                override fun onResponse(
                    call: Call<ApiResponse<AcceptInvitation>>,
                    response: Response<ApiResponse<AcceptInvitation>>
                ) {
                    if (response.isSuccessful) {
                        // 응답이 성공적인 경우, UI 업데이트나 성공 메시지 표시
                        Toast.makeText(applicationContext, "그룹 초대가 성공적으로 수락되었습니다.", Toast.LENGTH_SHORT).show()
                        // 그룹 정보를 다시 불러오는 등의 후속 작업 수행
                        refreshGroupInfo()
                    } else {
                        // 서버 응답에 실패한 경우
                        Toast.makeText(applicationContext, "그룹 초대 수락에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<AcceptInvitation>>, t: Throwable) {
                    // 네트워크 요청 자체에 실패한 경우
                    Toast.makeText(applicationContext, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onClickNoButton(id: Int) {
        TODO("Not yet implemented")
    }
}