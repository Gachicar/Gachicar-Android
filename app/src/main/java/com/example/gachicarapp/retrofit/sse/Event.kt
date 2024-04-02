package com.example.gachicarapp.retrofit.sse

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.gachicarapp.AcceptConfirmDialog
import com.example.gachicarapp.AcceptConfirmDialogInterface
import com.example.gachicarapp.BuildConfig.SERVER_IP_ADDRESS
import com.example.gachicarapp.CarDepartureDialog
import com.example.gachicarapp.ConfirmDialog
import com.example.gachicarapp.ConfirmDialogInterface
import com.example.gachicarapp.TimeAlarmDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL

data class Event(val name: String = "", val data: String = "")

// SSE 이벤트 수신 Flow 생성
fun getEventsFlow(context: Context): Flow<Event> = flow {
    val serverIp = SERVER_IP_ADDRESS
    val url = "http://${serverIp}:9090/api/notification/subscribe"

    val accessToken = createHttpClientWithToken(context)

    // Gets HttpURLConnection. Blocking function.  Should run in background
    val conn = (URL(url).openConnection() as HttpURLConnection).also {
        it.setRequestProperty("Accept", "text/event-stream") // set this Header to stream
        it.setRequestProperty("Authorization", "Bearer ${accessToken}")
        it.doInput = true // enable inputStream
    }

    conn.connect() // Blocking function. Should run in background

    val inputReader = conn.inputStream.bufferedReader()

    var event = Event()

    var name = ""
    var data = ""

    // run forever
    while (true) {
        val line = inputReader.readLine() // Blocking function. Read stream until \n is found

        when {
            line.startsWith("event:") -> { // get event name
                name = line.substringAfter("event:").trim()
            }

            line.startsWith("data:") -> { // get data
                data = line.substringAfter("data:").trim()
            }

            line.isEmpty() -> { // empty line, finished block. Emit the event
                emit(event)
                event = Event(name, data)
            }
        }
    }
}.catch { e ->
    Timber.e(e, "Error receiving SSE events")
}.flowOn(Dispatchers.IO)

private fun createHttpClientWithToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("tokens", Context.MODE_PRIVATE)
    return sharedPreferences.getString("access_token", null)
}

// SSE 이벤트 수신을 설정
fun startSSEConnection(context: Context) {
    // Context를 사용하여 SSE 이벤트 Flow를 가져옴
    val eventsFlow = getEventsFlow(context)

    // CoroutineScope를 사용하여 수신된 이벤트를 처리
    CoroutineScope(Dispatchers.Main).launch {
        eventsFlow.collect { event ->
            // 수신된 이벤트에 따라 처리
            when (event.name) {
                "notify" -> Toast.makeText(context, "알림 설정 성공", Toast.LENGTH_SHORT).show()
                "invite" -> processInvitation(context as FragmentActivity, event.data)
                "accept" -> processAcceptance(context as FragmentActivity, event.data )// 그룹 초대 수락 이벤트 처리
                "reminder" -> processReminder(context as FragmentActivity, event.data ) // 예약 시간 알림 처리
                "car" -> processCarDeparture(context as FragmentActivity, event.data ) // 차량 출발 알림 처리
            }
        }
    }
}

fun processInvitation(fragmentActivity: FragmentActivity, data: String?) {
    data?.let { jsonData ->
        try {
            val jsonObject = JSONObject(jsonData)
            val sender = jsonObject.getString("sender")
            val groupId = jsonObject.getInt("groupId")
            val createdAt = jsonObject.getString("created_at")

            val content = "$sender 님이 회원님을 그룹에 초대하고 싶어 합니다."
            val dialog = ConfirmDialog(fragmentActivity as ConfirmDialogInterface, "그룹 초대 요청", content, "수락", 1)
            dialog.show(fragmentActivity.supportFragmentManager, "InviteDialog")

            Timber.tag("Invitation").d("Received invitation from $sender to join group $groupId created at $createdAt")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

// 그룹 초대 수락 이벤트를 처리하는 함수
fun processAcceptance(context: Context, data: String?) {
    data?.let { jsonData ->
        try {
            val jsonObject = JSONObject(jsonData)
            val sender = jsonObject.getString("sender")
            val groupId = jsonObject.getInt("groupId")
            val createdAt = jsonObject.getString("createdAt")


            Timber.tag("Acceptance").d("Group invitation accepted by $sender for group $groupId created at $createdAt")


            if (context is FragmentActivity) {

                val dialog = AcceptConfirmDialog(object : AcceptConfirmDialogInterface {
                    override fun onAcceptanceConfirmed() {

                    }
                }, sender, 1, createdAt)
                dialog.show(context.supportFragmentManager, "AcceptConfirmDialog")
            } else {

                Toast.makeText(context, "$sender 님이 그룹 초대를 수락했습니다.", Toast.LENGTH_LONG).show()
            }

        } catch (e: JSONException) {
            Timber.e(e, "Error processing acceptance event")
        }
    }
}


// 예약 시간 알림 처리 함수
fun processReminder(context: Context, data: String?) {
    data?.let { jsonData ->
        try {
            val jsonObject = JSONObject(jsonData)
            val userName = jsonObject.getString("userName")
            val startTime = jsonObject.getString("startTime")
            val destination = jsonObject.getString("destination")

            // 로깅
            Timber.tag("알림").d("$userName 님의 예약 시간 알림 - $destination 에 $startTime 에 도착 예정")

            if (context is FragmentActivity) {
                // 다이얼로그 표시
                val dialog = TimeAlarmDialog(userName, startTime, destination)
                dialog.show(context.supportFragmentManager, "TimeAlarmDialog")
            }
        } catch (e: JSONException) {
            Timber.e(e, "예약 시간 알림 이벤트 처리 중 오류 발생")
        }
    }
}



// 차량 출발 알림 처리 함수
fun processCarDeparture(fragmentActivity: FragmentActivity, data: String?) {
    data?.let { jsonData ->
        try {
            val jsonObject = JSONObject(jsonData)
            val userName = jsonObject.getString("userName")
            val startTime = jsonObject.getString("startTime")
            val destination = jsonObject.getString("destination")

            // 차량 출발 알림 로깅
            Timber.tag("차량출발").d("$userName 님의 $startTime 에 예약된 $destination 으로의 차량 출발 알림")

            // 차량 출발 정보를 담은 다이얼로그 표시
            val dialog = CarDepartureDialog(userName, startTime, destination)
            dialog.show(fragmentActivity.supportFragmentManager, "CarDepartureDialog")

        } catch (e: JSONException) {
            Timber.e(e, "차량 출발 알림 이벤트 처리 중 오류 발생")
        }
    }
}
