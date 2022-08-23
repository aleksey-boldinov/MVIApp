package com.boldinov.mviapp.base.router

import androidx.lifecycle.*

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
open class BaseRouter<ENTITY : LifecycleOwner> : Router<ENTITY>, Navigator<ENTITY> {

    private var navigator: Navigator<ENTITY>? = null

    final override fun attachNavigator(navigator: Navigator<ENTITY>) {
        this.navigator = navigator
    }

    final override fun execute(command: ENTITY.() -> Unit) {
        navigator?.execute(command)
            ?: throw IllegalStateException("Cannot execute command: navigator not attached to an router")
    }
}