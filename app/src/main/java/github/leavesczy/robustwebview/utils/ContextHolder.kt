package github.leavesczy.robustwebview.utils

import android.app.Application

/**
 * @Author: leavesCZY
 * @Date: 2021/10/1 23:16
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
object ContextHolder {

    lateinit var application: Application
        private set

    fun init(application: Application) {
        this.application = application
    }

}