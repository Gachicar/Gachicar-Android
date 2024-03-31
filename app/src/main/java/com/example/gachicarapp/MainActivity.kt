package com.example.gachicarapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.request.AcceptInvitation
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.service.GroupService
import com.example.gachicarapp.retrofit.service.ReportService
import com.example.gachicarapp.retrofit.sse.startSSEConnection
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ConfirmDialogInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)

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
        // id는 groupId를 의미합니다. 그룹 초대 수락 처리
        val acceptInvitation = AcceptInvitation(groupId = id)
        val retrofitAPI = RetrofitConnection.getInstance(this).create(GroupService::class.java)

        retrofitAPI.postAcceptInvitation(acceptInvitation)
            .enqueue(object : Callback<ApiResponse<AcceptInvitation>> {
                override fun onResponse(
                    call: Call<ApiResponse<AcceptInvitation>>,
                    response: Response<ApiResponse<AcceptInvitation>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        // 응답이 성공적이고, 응답 본문이 null이 아닌 경우
                        val apiResponse = response.body()!!
                        // 여기에서 수락 처리에 대한 후속 작업을 수행하세요.
                        // 예를 들어, UI 업데이트나 성공 메시지 표시 등
                        Toast.makeText(this@MainActivity, "그룹 초대가 성공적으로 수락되었습니다.", Toast.LENGTH_SHORT).show()
                        // 그룹 정보를 다시 불러오거나 UI 업데이트 로직을 추가할 수 있습니다.
                    } else {
                        // 서버 응답에 실패한 경우
                        Toast.makeText(this@MainActivity, "그룹 초대 수락에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<AcceptInvitation>>, t: Throwable) {
                    // 네트워크 요청 자체에 실패한 경우
                    Toast.makeText(this@MainActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onClickNoButton(id: Int) {
        TODO("Not yet implemented")
    }
}