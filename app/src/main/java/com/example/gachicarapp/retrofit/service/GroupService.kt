package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.request.CreateGroup
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.DeleteGroup
import com.example.gachicarapp.retrofit.response.GetGroupInfo
import com.example.gachicarapp.retrofit.response.UpdateGroupDesc
import com.example.gachicarapp.retrofit.response.UpdateGroupName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupService {
    // Group 관련 API
    @POST("/api/group")
    fun postGroupNameData(
        @Body groupNameData: CreateGroup
    ): Call<ApiResponse<CreateGroup>>

    @DELETE("/api/group/{groupId}")
    fun deleteGroup(@Path("groupId") groupId: Int): Call<DeleteGroup>

    @GET("/api/group")
    fun getGroupInfo(): Call<GetGroupInfo>

    @PATCH("/api/group/updateDesc")
    fun patchGroupDesc(@Body desc: UpdateGroupDesc): Call<UpdateGroupDesc>

    @PATCH("/api/group/updateName")
    fun patchGroupName(@Body name: UpdateGroupName): Call<UpdateGroupName>
}