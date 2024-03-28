//package com.example.gachicarapp.retrofit.sse
//
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.os.IBinder
//import android.widget.Toast
//import androidx.core.content.ContentProviderCompat.requireContext
//import com.example.gachicarapp.ConfirmDialog
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//import org.json.JSONException
//import org.json.JSONObject
//import timber.log.Timber
//
//class SSEBackgroundService : Service() {
//
//    private var isRunning = false
//
//    override fun onCreate() {
//        super.onCreate()
//        isRunning = true
//        startListeningForSseEvents()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        isRunning = false
//        stopListeningForSseEvents()
//    }
//
//    private fun startListeningForSseEvents() {
//        // SSE 이벤트를 수신하는 코드 작성
//        // 예를 들어, HttpClient를 사용하여 서버와 SSE 연결하고 이벤트를 수신
//        // 수신한 이벤트를 처리하여 알림을 표시하는 메서드를 호출
//        runBlocking {
//            launch {
//                // SSE 이벤트를 비동기적으로 수신
//                getEventsFlow().collect { event ->
//                    // 이벤트 타입에 따라 적절한 처리 수행
//                    when (event.name) {
//                        "notify" -> showNotification(event.data)
//                        "invite" -> processInvitation(applicationContext, event.data)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun stopListeningForSseEvents() {
//        // SSE 이벤트 수신 중지 코드 작성
//    }
//
//    private fun showNotification(message: String) {
//        // 알림을 표시하는 코드 작성
//        // 예를 들어, NotificationManager를 사용하여 알림을 표시
//        Toast.makeText(this, "알림 설정 완료", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun processInvitation(context: Context, data: String?) {
//        // 초대 메시지를 처리하는 로직 구현
//        // 예를 들어, 데이터를 파싱하여 그룹 초대 처리 또는 화면에 표시
//        data?.let { jsonData ->
//            try {
//                val jsonObject = JSONObject(jsonData)
//                val sender = jsonObject.getString("sender")
//                val groupId = jsonObject.getInt("groupId")
//                val createdAt = jsonObject.getString("created_at")
//
//                // 초대 메시지 처리
//                // 예를 들어, 알림창 또는 다이얼로그를 띄우거나 그룹에 가입하는 등의 작업 수행
//                val content = "$sender 님이 회원님을 그룹에 초대하고 싶어 합니다."
//                val dialog = ConfirmDialog(context, "그룹 초대 요청", content, "수락", 1)
//                dialog.show(context.supp)
//
//
//                // 여기서는 예시로 로그를 출력함
//                Timber.tag("Invitation")
//                    .d("Received invitation from " + sender + " to join group " + groupId + " created at " + createdAt)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//}
//
