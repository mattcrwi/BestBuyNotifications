package com.matthew.williams.bestbuynotifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.matthew.williams.bestbuynotifications.BestBuyNotifications.Companion.GENERAL_NOTIFICATION_CHANNEL
import com.matthew.williams.bestbuynotifications.ProductData.Companion.linkData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.nodes.Document
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KSuspendFunction1

class UpdateController constructor(private val applicationContext: Context) {
    companion object {

        @Suppress("JoinDeclarationAndAssignment")
        private val objectMapper: ObjectMapper
        private val retrofit: Retrofit
        private val bestBuyApi: BestBuyApi
        private val timeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("M/d hh:mm:ss a").withZone(
                ZoneId.systemDefault()
            )
        private val savedResponses = ArrayList<String>(linkData.size)

        init {
            for (i in 0 until linkData.size) {
                savedResponses.add("init")
            }

            objectMapper = jacksonObjectMapper().apply {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                registerModule(JavaTimeModule())
            }
            val httpClient = OkHttpClient.Builder()
            httpClient.interceptors()
                .add(UserAgentInterceptor("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0"))
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            httpClient.addInterceptor(logging)

            retrofit = Retrofit.Builder()
                .baseUrl("http://www.bestbuy.com")
                .addConverterFactory(HtmlPageAdapter.FACTORY)
                .client(httpClient.build())
                .build()

            bestBuyApi = retrofit.create(BestBuyApi::class.java)
        }
    }

    suspend fun doUpdate(
        id: Int
    ): String {
        var response = ""
        try {
            val jsoupDocument =
                bestBuyApi.getData(linkData[id].url).body()!!
            val buttonText = jsoupDocument.select(".add-to-cart-button").html()

            Log.d(this::class.simpleName, "BestBuyPage: $buttonText")
            if (buttonText != "Sold Out" && buttonText != "Coming Soon" && (savedResponses[id] != buttonText)) {
                savedResponses[id] = buttonText
                response =
                    "${linkData[id].description}. Button: $buttonText " + timeFormatter.format(
                        ZonedDateTime.now()
                    )
                popNotification(response, id)
            } else {
                response =
                    "no ${linkData[id].description} at ${timeFormatter.format(ZonedDateTime.now())} button: $buttonText"
            }
        } catch (he: HttpException) {
            Log.e(this::class.simpleName, "failed update; http error.", he)
            response = "failed update; http error." + he.message

        } catch (e: Exception) {
            Log.e(this::class.simpleName, "failed update", e)
            response = "failed update; unknown error." + e.message
        } finally {
            return response
        }
    }


    private fun popNotification(messageText: String?, id: Int) {

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(linkData[id].url)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val builder = NotificationCompat.Builder(
            applicationContext,
            GENERAL_NOTIFICATION_CHANNEL
        )
            .setContentTitle(linkData[id].description)
            .setContentText(messageText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }
    }
}
