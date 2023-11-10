package github.leavesczy.robustwebview.utils

import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.Toast
import java.lang.reflect.Method

/**
 * @Author: leavesCZY
 * @Date: 2021/9/20 0:11
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
fun log(log: Any?) {
    Log.e("RobustWebView-" + Thread.currentThread().name, log.toString())
}

fun showToast(msg: String) {
    Toast.makeText(ContextHolder.application, msg, Toast.LENGTH_SHORT).show()
}

/**
 * 让 activity transition 动画过程中可以正常渲染页面
 */
fun setDrawDuringWindowsAnimating(view: View) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M
        || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1
    ) {
        //小于 4.3 和大于 6.0 时不存在此问题，无须处理
        return
    }
    try {
        val rootParent: ViewParent = view.rootView.parent
        val method: Method = rootParent.javaClass
            .getDeclaredMethod("setDrawDuringWindowsAnimating", Boolean::class.javaPrimitiveType)
        method.isAccessible = true
        method.invoke(rootParent, true)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}