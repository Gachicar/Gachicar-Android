package com.example.gachicarapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gachicarapp.databinding.ActivityRecord2Binding
import com.example.gachicarapp.databinding.FragmentHomeBinding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.getCarInfo
import com.example.gachicarapp.retrofit.service.AppServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    // 바인딩 객체 선언을 nullable로 초기화
    private var _binding: ActivityRecord2Binding? = null

    // 바인딩 객체에 대한 non-nullable 게터
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 인플레이터로 뷰 바인딩 클래스 인스턴스화
        _binding = ActivityRecord2Binding.inflate(inflater, container, false)

        // 이 프래그먼트의 루트 뷰 반환
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기서 바인딩을 사용하여 뷰에 액세스
        // 예: binding.textView.text = "Hello World!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰가 파괴될 때 바인딩 객체를 해제해 메모리 누수 방지
        _binding = null
    }
}

