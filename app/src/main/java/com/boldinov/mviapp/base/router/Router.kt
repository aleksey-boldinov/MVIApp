package com.boldinov.mviapp.base.router

import androidx.lifecycle.LifecycleOwner
import com.boldinov.mviapp.base.router.navigator.Navigator

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
interface Router<ENTITY : LifecycleOwner> {

    fun attachNavigator(navigator: Navigator<ENTITY>)
}