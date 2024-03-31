package com.example.gachicarapp.retrofit.response

data class GroupData(
    val car: Car,
    val desc: String,
    val groupId: Int,
    val groupManager: GroupManager,
    val name: String,
    val members: List<Member>
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
