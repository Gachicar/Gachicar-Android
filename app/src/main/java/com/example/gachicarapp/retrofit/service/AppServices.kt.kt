package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.response.CreateGroup
import com.example.gachicarapp.retrofit.response.DeleteGroup
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.response.GetGroupInfo
import com.example.gachicarapp.retrofit.response.MostUser
import com.example.gachicarapp.retrofit.response.ReserveData
import com.example.gachicarapp.retrofit.response.UpdateGroupDesc
import com.example.gachicarapp.retrofit.response.UpdateGroupName
import com.example.gachicarapp.retrofit.response.getCarInfo
import com.example.gachicarapp.retrofit.response.socialSignUpandLogin
import com.example.gachicarapp.retrofit.response.statistic_r_Response
import retrofit2.Call
import retrofit2.http.*


interface AppServices {
    // 로그인 관련 API
    @POST("/api/auth/social")
    fun token(@Header("Authorization") accessToken: String): Call<socialSignUpandLogin>


    // Group 관련 API
    @POST("/api/group")
    fun postGroupNameData(
        @Body groupNameData: CreateGroup.GroupNameData
    ): Call<CreateGroup>

    @DELETE("/api/group/{groupId}")
    fun deleteGroup(@Path("groupId") groupId: Int): Call<DeleteGroup>

    @GET("/api/group")
    fun getGroupInfo(): Call<GetGroupInfo>

    @PATCH("/api/group/updateDesc")
    fun patchGroupDesc(@Body desc: UpdateGroupDesc): Call<UpdateGroupDesc>

    @PATCH("/api/group/updateName")
    fun patchGroupName(@Body name: UpdateGroupName): Call<UpdateGroupName>

    // Car 정보 API
    @GET("/api/car")
    fun getCarHomeInfo(): Call<getCarInfo>

    // 예약 데이터 API
    @GET("/api/report/reserve")
    fun getReserveData(): Call<ReserveData>

    // 통계 관련 API
    @GET("/api/car/fuel")
    fun getFuelData(): Call<statistic_r_Response>

    @GET("/api/report")
    fun getDriveReport(): Call<DriveReport>

    @GET("/api/report/most")
    fun getMostUser(): Call<MostUser>


}