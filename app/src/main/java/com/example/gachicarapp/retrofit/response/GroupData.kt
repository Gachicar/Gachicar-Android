package com.example.gachicarapp.retrofit.response

import com.google.gson.annotations.SerializedName

data class GroupData(
    val car: Car,
    val desc: String,
    val groupId: Int,
    val groupManager: GroupManager,
    val name: String,
    @SerializedName("memberList") val members: List<Member>
) {
    data class GroupManager(
        val id: Int,
        val name: String
    )

    data class Member(
        val email: String,
        val role: String,
        val userId: Int,
        val userName: String
    )
}
