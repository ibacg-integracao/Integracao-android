package br.com.atitude.finder.data.network.entity.response.search

import br.com.atitude.finder.domain.SearchParams
import com.google.gson.annotations.SerializedName

data class SearchParamsResponse(
    @SerializedName("week_days") val weekDays: List<String>,
    @SerializedName("times") val times: List<String>,
    @SerializedName("tags") val tags: List<String>
)

fun SearchParamsResponse.toDomain() = SearchParams(
    weekDays, times, tags
)