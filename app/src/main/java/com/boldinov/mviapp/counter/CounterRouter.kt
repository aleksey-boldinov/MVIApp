package com.boldinov.mviapp.counter

import android.content.Intent
import androidx.fragment.app.Fragment
import com.boldinov.mviapp.base.router.Router
import com.boldinov.mviapp.base.router.fragment.FragmentRouter

/**
 * Created by Aleksey Boldinov on 24.08.2022.
 */
interface CounterRouter : Router<Fragment> {

    fun shareToApps(counter: Int)
}

class CounterRouterImpl : FragmentRouter(), CounterRouter {

    override fun shareToApps(counter: Int) {
        execute {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, counter.toString())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

}