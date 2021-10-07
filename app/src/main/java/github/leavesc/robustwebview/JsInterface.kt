package github.leavesc.robustwebview

import android.webkit.JavascriptInterface
import github.leavesc.robustwebview.utils.log
import github.leavesc.robustwebview.utils.showToast

/**
 * @Author: leavesC
 * @Date: 2021/9/21 15:08
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class JsInterface {

    @JavascriptInterface
    fun showToastByAndroid(log: String) {
        log("showToastByAndroid:$log")
        showToast(log)
    }

}