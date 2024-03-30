package com.example.gachicarapp

import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.Locale

class VoiceFragment : Fragment(), TextToSpeech.OnInitListener {

    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null
    private lateinit var tvConnectionStatus: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var etMessage: EditText
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_voice, container, false)

        tvConnectionStatus = view.findViewById(R.id.tvConnectionStatus)
        progressBar = view.findViewById(R.id.progressBar)
        etMessage = view.findViewById(R.id.etMessage)

        val btnSpeak = view.findViewById<Button>(R.id.btnSpeak)
        btnSpeak.setOnClickListener {
            val message = etMessage.text.toString()
            sendMessage(message)
            etMessage.text.clear()
        }

        textToSpeech = TextToSpeech(context, this)

        connectToServer()

        return view
    }

    private fun sendMessage(message: String) {
        GlobalScope.launch(Dispatchers.IO) {
            writer?.println(message)
            GlobalScope.launch(Dispatchers.Main) {
                addMessageToView("나: $message", false)
            }
        }
    }

    private fun receiveData() {
        try {
            var line: String?
            while (reader!!.readLine().also { line = it } != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    addMessageToView(line ?: "", true)
                    speakOut(line)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun connectToServer() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val serverIp = "172.30.1.95"
                val serverPort = 9595
                socket = Socket(serverIp, serverPort)
                writer = PrintWriter(socket!!.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                updateUI("Connected to Server", false)

                receiveData()
            } catch (e: Exception) {
                e.printStackTrace()
                updateUI("Failed to Connect", false)
            }
        }
    }

    private fun updateUI(message: String, showProgress: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            tvConnectionStatus.text = message
            progressBar.visibility = if (showProgress) View.VISIBLE else View.GONE
        }
    }

    private fun addMessageToView(message: String, isServerMessage: Boolean) {
        activity?.runOnUiThread {
            val messageTextView = TextView(context).apply {
                text = message
                textSize = 16f
                background = if (isServerMessage) {
                    context?.getDrawable(R.drawable.rounded_filled_rec)
                } else {
                    context?.getDrawable(R.drawable.rounded_filled_rec2)
                }
                setTextColor(Color.BLACK)
                setPadding(16, 16, 16, 16)

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = if (isServerMessage) Gravity.START else Gravity.END
                    bottomMargin = 16
                }
            }
            val llMessages: LinearLayout = view?.findViewById(R.id.llMessages) ?: return@runOnUiThread
            llMessages.addView(messageTextView)

            val scrollView: ScrollView = view?.findViewById(R.id.scroll) ?: return@runOnUiThread
            scrollView.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }
    }

    private fun speakOut(text: String?) {
        text?.let {
            textToSpeech.speak(it, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                println("TextToSpeech: 언어를 지원하지 않습니다.")
            }
        } else {
            println("TextToSpeech 초기화 실패")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textToSpeech.stop()
        textToSpeech.shutdown()
        try {
            writer?.close()
            reader?.close()
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
