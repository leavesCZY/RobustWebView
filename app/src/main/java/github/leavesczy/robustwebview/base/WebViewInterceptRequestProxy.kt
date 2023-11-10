package github.leavesczy.robustwebview.base

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import github.leavesczy.robustwebview.utils.log
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

/**
 * @Author: leavesCZY
 * @Date: 2021/10/4 18:56
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
object WebViewInterceptRequestProxy {

    private lateinit var application: Application

    private val webViewResourceCacheDir by lazy {
        File(application.cacheDir, "RobustWebView")
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder().cache(Cache(webViewResourceCacheDir, 600L * 1024 * 1024))
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(getChuckerInterceptor(application = application))
            .addNetworkInterceptor(getWebViewCacheInterceptor())
            .build()
    }

    private fun getChuckerInterceptor(application: Application): Interceptor {
        return ChuckerInterceptor.Builder(application)
            .collector(ChuckerCollector(application))
            .maxContentLength(250000L)
            .alwaysReadResponseBody(true)
            .build()
    }

    private fun getWebViewCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            response.newBuilder()
                .removeHeader("pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "max-age=" + (360L * 24 * 60 * 60))
                .build()
        }
    }

    fun init(application: Application) {
        this.application = application
    }

    fun shouldInterceptRequest(webResourceRequest: WebResourceRequest?): WebResourceResponse? {
        if (toProxy(webResourceRequest)) {
            return getHttpResource(webResourceRequest!!)
        }
        return null
    }

    private fun toProxy(webResourceRequest: WebResourceRequest?): Boolean {
        if (webResourceRequest == null || webResourceRequest.isForMainFrame) {
            return false
        }
        val url = webResourceRequest.url ?: return false
        if (!webResourceRequest.method.equals("GET", true)) {
            return false
        }
        if (url.scheme == "https" || url.scheme == "http") {
            val urlString = url.toString()
            if (urlString.endsWith(".js", true) ||
                urlString.endsWith(".css", true) ||
                urlString.endsWith(".jpg", true) ||
                urlString.endsWith(".png", true) ||
                urlString.endsWith(".webp", true)||
                urlString.endsWith(".awebp", true)
            ) {
                return true
            }
        }
        return false
    }

    private fun getHttpResource(webResourceRequest: WebResourceRequest): WebResourceResponse? {
        try {
            val url = webResourceRequest.url.toString()
            val requestBuilder = Request
                .Builder()
                .url(url)
                .method(webResourceRequest.method, null)
            webResourceRequest.requestHeaders?.forEach {
                requestBuilder.addHeader(it.key, it.value)
            }
            val response = okHttpClient
                .newCall(requestBuilder.build())
                .execute()
            val code = response.code
            if (code != 200) {
                return null
            }
            val body = response.body
            val mimeType = response.header("content-type", body.contentType()?.type)
            val encoding = response.header("content-encoding", "utf-8")
            val responseHeaders = mutableMapOf<String, String>()
            for (header in response.headers) {
                responseHeaders[header.first] = header.second
            }
            var message = response.message
            if (message.isBlank()) {
                message = "OK"
            }
            val resourceResponse = WebResourceResponse(mimeType, encoding, body.byteStream())
            resourceResponse.responseHeaders = responseHeaders
            resourceResponse.setStatusCodeAndReasonPhrase(code, message)
            return resourceResponse
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    private fun getAssetsImage(url: String): WebResourceResponse? {
        if (url.contains(".jpg")) {
            try {
                val inputStream = application.assets.open("ic_launcher.webp")
                return WebResourceResponse(
                    "image/webp",
                    "utf-8", inputStream
                )
            } catch (e: Throwable) {
                log("Throwable: $e")
            }
        }
        return null
    }

}