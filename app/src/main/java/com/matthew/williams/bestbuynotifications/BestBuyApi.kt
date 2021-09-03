package com.matthew.williams.bestbuynotifications

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url
import java.lang.reflect.Type


interface BestBuyApi {

    @Headers("Accept: text/html")
    @GET
    suspend fun getData(@Url url: String): Response<Document>
}

class HtmlPageAdapter : Converter<ResponseBody, Document?> {
    companion object {
        val FACTORY: Converter.Factory = object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<Annotation?>?,
                retrofit: Retrofit?
            ): Converter<ResponseBody, *>? {
                return if (type === Document::class.java) HtmlPageAdapter() else null
            }
        }
    }

    override fun convert(responseBody: ResponseBody): Document {
        return Jsoup.parse(responseBody.string())
    }
}