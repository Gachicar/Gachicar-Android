package com.example.gachicarapp.retrofit.response

data class getGroupInfo(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val car: Car,
        val desc: String,
        val groupId: Int,
        val groupManager: GroupManager,
        val memberList: List<Member>,
        val name: String
    ) {
        data class Car(
            val carName: String,
            val carNumber: String,
            val curLoc: String,
            val driveTime: Int,
            val latestDate: String,
            val location: String
        )

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
}