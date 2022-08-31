package com.boldinov.mviapp.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.boldinov.mviapp.base.rx.BinderContainer
import com.boldinov.mviapp.base.rx.RxJavaBinder

/**
 * Created by Aleksey Boldinov on 01.09.2022.
 */

inline fun <VIEW> Fragment.attachRxJavaBinder(
    mode: BinderLifecycleMode = BinderLifecycleMode.CREATE_DESTROY,
    crossinline viewFactory: (View) -> VIEW,
    crossinline binder: BinderContainer.(VIEW) -> Unit,
) {
    attachBinder(mode, viewFactory) {
        RxJavaBinder {
            binder.invoke(this, it)
        }
    }
}

inline fun <VIEW> Fragment.attachBinder(
    mode: BinderLifecycleMode = BinderLifecycleMode.CREATE_DESTROY,
    crossinline viewFactory: (View) -> VIEW,
    crossinline binder: (VIEW) -> Binder,
) {
    viewLifecycleOwnerLiveData.observeForever {
        it?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(
                source: LifecycleOwner,
                event: Lifecycle.Event
            ) {
                if (event == Lifecycle.Event.ON_CREATE) {
                    binder.invoke(viewFactory.invoke(requireView()))
                        .attachTo(source.lifecycle, mode)
                } else if (event == Lifecycle.Event.ON_DESTROY) {
                    source.lifecycle.removeObserver(this)
                }
            }
        })
    }
}

fun Binder.attachTo(lifecycle: Lifecycle, mode: BinderLifecycleMode): Binder =
    when (mode) {
        BinderLifecycleMode.CREATE_DESTROY -> {
            lifecycle.subscribe(onCreate = ::start, onDestroy = ::stop)
        }
        BinderLifecycleMode.START_STOP -> {
            lifecycle.subscribe(onStart = ::start, onStop = ::stop)
        }
        BinderLifecycleMode.RESUME_PAUSE -> {
            lifecycle.subscribe(onResume = ::start, onPause = ::stop)
        }
    }.let { this }