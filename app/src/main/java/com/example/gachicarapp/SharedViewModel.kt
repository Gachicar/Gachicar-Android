package com.example.gachicarapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gachicarapp.retrofit.response.GroupData

class SharedViewModel : ViewModel() {
    val groupData: MutableLiveData<GroupData> = MutableLiveData()
    private val _userNickname = MutableLiveData<String>()
    val userNickname: LiveData<String> = _userNickname

    fun updateUserNickname(newNickname: String) {
        _userNickname.value = newNickname
    }
}
