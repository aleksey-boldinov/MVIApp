package com.boldinov.mviapp.main

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.binder.attachTo
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.boldinov.mviapp.base.router.WeakLifecycleNavigator
import com.boldinov.mviapp.base.rx.RxJavaBinder
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
    private val fragment: Fragment,
    private val viewFactory: (View) -> MainView,
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
        counterRouter.attachNavigator(WeakLifecycleNavigator(fragment))
        fragment.viewLifecycleOwnerLiveData.observeForever {
            it?.lifecycle?.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(
                    source: LifecycleOwner,
                    event: Lifecycle.Event
                ) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        onViewCreated(viewFactory.invoke(fragment.requireView()), source.lifecycle)
                    } else if (event == Lifecycle.Event.ON_DESTROY) {
                        source.lifecycle.removeObserver(this)
                    }
                }
            })
        }
    }

    private fun onViewCreated(view: MainView, viewLifecycle: Lifecycle) {
        RxJavaBinder {
            view.observableEvents().subscribe {
                counterStore.accept(it.toIntent())
            }
            counterStore.observableStates()
                .map(MainViewModelMapper()).subscribe {
                    view.render(it)
                }
            counterStore.observableLabels().subscribe {
                if (it is CounterStore.Label.ShareCounter) {
                    counterRouter.shareToApps(it.counter)
                }
            }
        }.attachTo(viewLifecycle.asEssentyLifecycle(), BinderLifecycleMode.CREATE_DESTROY)
    }

    private fun MainEvent.toIntent(): CounterStore.Intent {
        return when (this) {
            MainEvent.IncreaseClicked -> CounterStore.Intent.Increase
            MainEvent.DecreaseClicked -> CounterStore.Intent.Decrease
            MainEvent.ShareClicked -> CounterStore.Intent.ShareCounter
        }
    }
}