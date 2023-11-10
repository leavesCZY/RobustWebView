package github.leavesczy.robustwebview

import android.app.Application
import github.leavesczy.robustwebview.base.WebViewInitTask
import github.leavesczy.robustwebview.utils.ContextHolder

/**
 * @Author: leavesCZY
 * @Date: 2021/9/12 22:22
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextHolder.init(application = this)
        WebViewInitTask.init(application = this)
    }

}