package github.leavesc.robustwebview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import github.leavesc.robustwebview.base.RobustWebView
import github.leavesc.robustwebview.base.WebViewCacheHolder
import github.leavesc.robustwebview.base.WebViewListener
import github.leavesc.robustwebview.utils.showToast

/**
 * @Author: leavesC
 * @Date: 2021/10/1 23:08
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class WebViewActivity : AppCompatActivity() {

    private val rootLayout by lazy {
        findViewById<ViewGroup>(R.id.rootLayout)
    }

    private val tvTitle by lazy {
        findViewById<TextView>(R.id.tvTitle)
    }

    private val tvProgress by lazy {
        findViewById<TextView>(R.id.tvProgress)
    }

    private val url1 =
        "https://p26-passport.byteacctimg.com/img/user-avatar/6019f80db5be42d33c31c98adaf3fa8c~300x300.image"

    private val url2 = "https://juejin.cn/user/923245496518439/posts"

    private val url3 = "https://www.bilibili.com/"

    private lateinit var webView: RobustWebView

    private val webViewListener = object : WebViewListener {
        override fun onProgressChanged(webView: RobustWebView, progress: Int) {
            tvProgress.text = progress.toString()
        }

        override fun onReceivedTitle(webView: RobustWebView, title: String) {
            tvTitle.text = title
        }

        override fun onPageFinished(webView: RobustWebView, url: String) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = WebViewCacheHolder.acquireWebViewInternal(this)
        webView.webViewListener = webViewListener
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        rootLayout.addView(webView, layoutParams)
        findViewById<View>(R.id.tvBack).setOnClickListener {
            onBackPressed()
        }
        findViewById<View>(R.id.btnOpenUrl1).setOnClickListener {
            webView.loadUrl(url1)
        }
        findViewById<View>(R.id.btnOpenUrl2).setOnClickListener {
            webView.loadUrl(url2)
        }
        findViewById<View>(R.id.btnOpenUrl3).setOnClickListener {
            webView.loadUrl(url3)
        }
        findViewById<View>(R.id.btnReload).setOnClickListener {
            webView.reload()
        }
        findViewById<View>(R.id.btnOpenHtml).setOnClickListener {
            webView.loadUrl("""file:/android_asset/javascript.html""")
        }
        findViewById<View>(R.id.btnCallJsByAndroid).setOnClickListener {
            val parameter = "\"业志陈\""
            webView.evaluateJavascript(
                "javascript:callJsByAndroid(${parameter})"
            ) {
                showToast("evaluateJavascript: $it")
            }
//            webView.loadUrl("javascript:callJsByAndroid(${parameter})")
        }
        findViewById<View>(R.id.btnShowToastByAndroid).setOnClickListener {
            webView.loadUrl("javascript:showToastByAndroid()")
        }
        findViewById<View>(R.id.btnCallJsPrompt).setOnClickListener {
            webView.loadUrl("javascript:callJsPrompt()")
        }
    }

    override fun onBackPressed() {
        if (webView.toGoBack()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        WebViewCacheHolder.prepareWebView()
    }

}