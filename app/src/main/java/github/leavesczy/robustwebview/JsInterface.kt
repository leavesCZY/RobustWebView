package github.leavesczy.robustwebview

import android.webkit.JavascriptInterface
import github.leavesczy.robustwebview.utils.log
import github.leavesczy.robustwebview.utils.showToast

/**
 * @Author: leavesCZY
 * @Date: 2021/9/21 15:08
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class JsInterface {

    @JavascriptInterface
    fun showToastByAndroid(log: String) {
        log("showToastByAndroid:$log")
        showToast(log)
    }

}