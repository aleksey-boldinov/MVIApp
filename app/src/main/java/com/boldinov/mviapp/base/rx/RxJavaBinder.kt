package com.boldinov.mviapp.base.rx

import com.arkivanov.mvikotlin.core.binder.Binder
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
class RxJavaBinder(
    private val subscribe: BinderContainer.() -> Unit
) : Binder {

    private val disposables = CompositeDisposable()
    private val container = object : BinderContainer {
        override fun bindTo(disposable: Disposable) {
            disposables.add(disposable)
        }
    }

    override fun start() {
        subscribe.invoke(container)
    }

    override fun stop() {
        disposables.clear()
    }
}

interface BinderContainer {
    infix fun bindTo(disposable: Disposable)
}