package br.com.atitude.finder.data.network

import br.com.atitude.finder.data.network.entity.PostalCodeAddressInfoResponse
import br.com.atitude.finder.data.network.entity.SearchParamsResponse
import br.com.atitude.finder.data.network.entity.SectorResponse
import br.com.atitude.finder.data.network.entity.SimplePointResponse
import br.com.atitude.finder.data.network.entity.TokenResponse
import br.com.atitude.finder.data.network.entity.WeekDayResponse
import br.com.atitude.finder.data.network.entity.request.CreatePointRequest
import br.com.atitude.finder.data.network.entity.request.LoginRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {

    @GET("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @DELETE("v1/point/{id}")
    suspend fun deletePoint(@Path("id") id: String)

    @GET("v1/point/search")
    suspend fun searchPoints(
        @Query("postal_code") postalCode: String,
        @Query("week_day") weekDays: List<String>,
        @Query("tag") tags: List<String>,
        @Query("time") times: List<String>,
    ): List<SimplePointResponse>

    @GET("v1/point/")
    suspend fun getAllPoints(): List<SimplePointResponse>

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
}