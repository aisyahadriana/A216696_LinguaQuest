package com.example.a216696_wan_project2

// The free Google Translate endpoint returns a raw JSON array

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import java.util.concurrent.TimeUnit

interface TranslateApiService {
    // Returns raw ResponseBody because the response is a nested JSON array.
    @GET("translate_a/single")
    suspend fun translateRaw(
        @Query("client") client: String = "gtx",
        @Query("sl")     sl:     String,
        @Query("tl")     tl:     String,
        @Query("dt")     dt:     String = "t",
        @Query("q")      q:      String
    ): Response<ResponseBody>
}

// ── Parse the nested array response from Google Translate ────────
// Response looks like: [[["translated","original",null,null,1]],null,"ko"]
fun parseTranslateResponse(body: String): String {
    return try {
        val outer = JSONArray(body)
        val sentences = outer.getJSONArray(0)
        val sb = StringBuilder()
        for (i in 0 until sentences.length()) {
            val sentence = sentences.optJSONArray(i)
            if (sentence != null) {
                val translated = sentence.optString(0, "")
                if (translated.isNotEmpty()) sb.append(translated)
            }
        }
        sb.toString().trim()
    } catch (e: Exception) {
        ""
    }
}

object TranslateRetrofitClient {
    private const val BASE_URL = "https://translate.googleapis.com/"

    val instance: TranslateApiService by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateApiService::class.java)
    }

    // ── Convenience suspend function ─────────────────────────────
    suspend fun translate(sl: String, tl: String, q: String): String {
        return try {
            val response = instance.translateRaw(sl = sl, tl = tl, q = q)
            if (response.isSuccessful) {
                val bodyStr = response.body()?.string() ?: return ""
                parseTranslateResponse(bodyStr)
            } else ""
        } catch (e: Exception) {
            throw e
        }
    }
}