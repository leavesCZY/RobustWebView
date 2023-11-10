package github.leavesczy.robustwebview.base

import android.app.Application
import android.content.Context
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import github.leavesczy.robustwebview.utils.log

/**
 * @Author: leavesCZY
 * @Date: 2021/9/20 23:47
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
object WebViewInitTask {

    fun init(application: Application) {
        initWebView(application)
        WebViewCacheHolder.init(application)
        WebViewInterceptRequestProxy.init(application)
    }

    private fun initWebView(context: Context) {
        QbSdk.setDownloadWithoutWifi(true)
        val map = mutableMapOf<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_PRIVATE_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        val callback = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(isX5Core: Boolean) {
                log("onViewInitFinished: $isX5Core")
            }

            override fun onCoreInitFinished() {
                log("onCoreInitFinished")
            }
        }
        QbSdk.initX5Environment(context, callback)
    }

}