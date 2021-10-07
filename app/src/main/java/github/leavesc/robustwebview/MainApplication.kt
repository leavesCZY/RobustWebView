package github.leavesc.robustwebview

import android.app.Application
import github.leavesc.robustwebview.base.WebViewInitTask
import github.leavesc.robustwebview.utils.ContextHolder

/**
 * @Author: leavesC
 * @Date: 2021/9/12 22:22
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextHolder.application = this
        WebViewInitTask.init(this)
    }

}