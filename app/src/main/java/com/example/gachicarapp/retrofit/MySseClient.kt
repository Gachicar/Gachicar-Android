package com.example.gachicarapp.retrofit

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.gachicarapp.AlarmActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject


class MySseClient(private val context: Context) {

    init {
        createNotificationChannel()
    }

    fun startListening() {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url("http://172.16.18.42:9090/api/notification/subscribe/1")
            .build()

        val listener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
                // 연결이 성공적으로 열렸을 때의 처리
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                // 이벤트가 도착했을 때의 처리
                if (type == "reminder" && JSONObject(data).getString("userName") == "이연수") {
                    handleReminderEvent(data)
                }
            }

            override fun onClosed(eventSource: EventSource) {
                // 연결이 닫혔을 때의 처리
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: okhttp3.Response?) {
                // 연결 실패 또는 에러 발생 시의 처리
            }
        }

        EventSources.createFactory(client).newEventSource(request, listener)
    }

    private fun handleReminderEvent(data: String) {
        val jsonObject = JSONObject(data)
        val userName = jsonObject.getString("userName")
        val startTime = jsonObject.getString("startTime")
        val destination = jsonObject.getString("destination")

        sendNotification(userName, startTime, destination)
    }

    private fun sendNotification(userName: String, startTime: String, destination: String) {
        // Android 13(Tiramisu) 이상에서는 알림 권한을 확인합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 알림 권한 확인
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 알림을 보내지 않습니다.
                return
            }
        }
        val message = "$userName 님, $startTime 에 $destination 으로 예약시간이 되었습니다."

        val intent = Intent(context, AlarmActivity::class.java).apply {
            putExtra("notification_message", message) // 인텐트에 메시지 추가
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("예약 알림")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Notification Channel"
            val descriptionText = "Channel for Event Reminder Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "event_reminder_channel"
        const val NOTIFICATION_ID = 1
    }
}
