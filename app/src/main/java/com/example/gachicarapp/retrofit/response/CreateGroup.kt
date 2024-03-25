package com.example.gachicarapp.retrofit.response

data class CreateGroup(
    val code: Int,
    val `data`: Any,
    val message: String
){
    data class GroupNameData(
        val groupName: String,
        val groupDesc: String
    )

}