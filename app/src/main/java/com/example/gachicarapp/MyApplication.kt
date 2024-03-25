package com.example.gachicarapp

import android.app.Application
import com.example.gachicarapp.BuildConfig.NATIVE_APP_KEY
import com.kakao.sdk.common.KakaoSdk


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, NATIVE_APP_KEY)
    }
}