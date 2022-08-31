package com.boldinov.mviapp.main

import android.view.View
import androidx.fragment.app.Fragment
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.boldinov.mviapp.base.attachRxJavaBinder
import com.boldinov.mviapp.base.router.buffer.BufferStatePolicy
import com.boldinov.mviapp.base.router.navigator.WeakLifecycleNavigator
import com.boldinov.mviapp.base.rx.observableEvents
import com.boldinov.mviapp.base.rx.observableLabels
import com.boldinov.mviapp.base.rx.observableStates
import com.boldinov.mviapp.counter.CounterRouterImpl
import com.boldinov.mviapp.counter.CounterStore
import com.boldinov.mviapp.counter.CounterStoreFactory
import com.boldinov.mviapp.main.mapper.MainViewModelMapper

/**
 * Created by Aleksey Boldinov on 22.08.2022.
 */
class MainController private constructor(
    fragment: Fragment,
    viewFactory: (View) -> MainView,
    private val storeFactory: StoreFactory,
) {

    companion object {

        fun attachTo(
            fragment: Fragment,
            viewFactory: (View) -> MainView,
            storeFactory: StoreFactory = DefaultStoreFactory()
        ) {
            MainController(fragment, viewFactory, storeFactory)
        }
    }

    private val counterRouter = CounterRouterImpl()
    private val counterStore = fragment.instanceKeeper().getStore {
        CounterStoreFactory(
            storeFactory = storeFactory
        ).create()
    }

    init {
        counterRouter.attachNavigator(
            WeakLifecycleNavigator(
                fragment,
                bufferStatePolicy = BufferStatePolicy.ViewModel("counter", fragment)
            )
        )
        fragment.attachRxJavaBinder(
            BinderLifecycleMode.CREATE_DESTROY,
            viewFactory = viewFactory,
            binder = { view ->
                view.observableEvents().bind {
                    counterStore.accept(it.toIntent())
                }
                counterStore.observableLabels().bind {
                    if (it is CounterStore.Label.ShareCounter) {
                        counterRouter.shareToApps(it.counter)
                    }
                }
                counterStore.observableStates()
                    .map(MainViewModelMapper()) bindTo view
            })
    }

    private fun MainEvent.toIntent(): CounterStore.Intent {
        return when (this) {
            MainEvent.IncreaseClicked -> CounterStore.Intent.Increase
            MainEvent.DecreaseClicked -> CounterStore.Intent.Decrease
            MainEvent.ShareClicked -> CounterStore.Intent.ShareCounter
        }
    }
}