package com.example.gachicarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gachicarapp.databinding.ActivityReportBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.service.AppServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getReportData()
    }

    private fun getReportData() {
        val retrofitAPI = RetrofitConnection.getInstance(this).create(AppServices::class.java)

        // API 호출
        retrofitAPI.getDriveReport()
            .enqueue(object : Callback<DriveReport> {
                override fun onResponse(call: Call<DriveReport>, response: Response<DriveReport>) {
                    if (response.isSuccessful) {
                        // API 응답이 성공적으로 수신된 경우 UI 업데이트를 수행합니다.
                        response.body()?.let { updateReportUI(it) }
                    } else {
                        // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                        Toast.makeText(this@ReportActivity, "리포트 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DriveReport>, t: Throwable) {
                    // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                    Toast.makeText(this@ReportActivity, "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
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

        // 리포트 작성
        val reportText = "사용자: $userName\n\n" +
                "차량 이름: $carName\n" +
                "차량 번호: $carNumber\n" +
                "출발지: $departure\n" +
                "목적지: $destination\n" +
                "주행 시간: $driveTime\n\n" +
                "차량 시작 시간: $startTime\n" +
                "차량 종료 시간: $endTime"

        binding.tvReportTime.text = reportText
    }

}