package br.com.atitude.finder.data.analytics.tracking

class SearchEvent(private val postalCode: String): Event(name = "search_points", params = {
    param("postal_code", postalCode)
})