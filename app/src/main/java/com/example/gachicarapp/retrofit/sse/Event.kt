package com.example.gachicarapp.retrofit.sse

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.gachicarapp.ConfirmDialog
import com.example.gachicarapp.ConfirmDialogInterface
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
    val url = "http://172.30.1.95:9090/api/notification/subscribe"

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


