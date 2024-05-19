package br.com.atitude.finder.data.network

import br.com.atitude.finder.data.network.entity.request.CreatePointRequest
import br.com.atitude.finder.data.network.entity.request.UpdatePointRequest
import br.com.atitude.finder.data.network.entity.AuthenticatedUserResponse
import br.com.atitude.finder.data.network.entity.TokenResponse
import br.com.atitude.finder.data.network.entity.UserManagerItemResponse
import br.com.atitude.finder.data.network.entity.request.ChangePasswordRequest
import br.com.atitude.finder.data.network.entity.request.LoginRequest
import br.com.atitude.finder.data.network.entity.request.RegisterAccountRequest
import br.com.atitude.finder.data.network.entity.response.PostalCodeAddressInfoResponse
import br.com.atitude.finder.data.network.entity.response.SimplePointResponse
import br.com.atitude.finder.data.network.entity.response.WeekDayResponse
import br.com.atitude.finder.data.network.entity.response.pointdetail.PointDetailResponse
import br.com.atitude.finder.data.network.entity.response.search.SearchParamsResponse
import br.com.atitude.finder.data.network.entity.response.sector.SectorResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {

    @GET("v1/users/")
    suspend fun getAuthenticatedUser(): AuthenticatedUserResponse

    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @DELETE("v1/point/{id}")
    suspend fun deletePoint(@Path("id") id: String)

    @GET("v2/point/search")
    suspend fun searchPointsByAddressOrPostalCode(
        @Query("input") input: String,
        @Query("point_name") pointName: String?,
        @Query("week_day") weekDays: List<String>,
        @Query("tag") tags: List<String>,
        @Query("time") times: List<String>,
    ): List<SimplePointResponse>

    @GET("v1/point/time")
    suspend fun pointsTime(): List<String>

    @GET("v1/point/search/params")
    suspend fun searchParams(): SearchParamsResponse

    @POST("v1/point/")
    suspend fun createPoint(@Body request: CreatePointRequest)

    @GET("v1/address/postalcode/{postalcode}")
    suspend fun findPostalCodeAddressInfo(@Path("postalcode") postalCode: String): PostalCodeAddressInfoResponse?

    @GET("v1/weekdays/")
    suspend fun getWeekDays(): List<WeekDayResponse>

    @GET("v1/sectors/")
    suspend fun getSectors(): List<SectorResponse>

    @GET("v1/point/{id}")
    suspend fun getPointById(@Path("id") id: String): PointDetailResponse

    @PUT("v1/point/{id}")
    suspend fun updatePoint(
        @Path("id") id: String,
        @Body request: UpdatePointRequest
    ): SimplePointResponse

    @POST("v1/auth/register")
    suspend fun registerAccount(@Body request: RegisterAccountRequest)

    @PUT("v1/users/password")
    suspend fun updatePassword(@Body request: ChangePasswordRequest)

    @GET("v1/users/all")
    suspend fun getAllUsers(): List<UserManagerItemResponse>

    @PUT("v1/users/accept/{id}")
    suspend fun enabledUser(@Path("id") id: String)

    @PUT("v1/users/refuse/{id}")
    suspend fun disableUser(@Path("id") id: String)

    @DELETE("v1/users/{id}")
    suspend fun deleteNotAcceptedUser(@Path("id") id: String)
}