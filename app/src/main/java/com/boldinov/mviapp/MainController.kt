package com.boldinov.mviapp

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.binder.attachTo
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.boldinov.mviapp.base.RxJavaBinder
import com.boldinov.mviapp.base.observableEvents
import com.boldinov.mviapp.base.observableLabels
import com.boldinov.mviapp.base.observableStates
import com.boldinov.mviapp.counter.CounterStore
import com.boldinov.mviapp.counter.CounterStoreFactory
import com.boldinov.mviapp.mapper.MainViewModelMapper

/**
 * Created by Aleksey Boldinov on 22.08.2022.
 */
class MainController(
    private val storeFactory: StoreFactory,
    instanceKeeper: InstanceKeeper
) {

    private val counterStore = instanceKeeper.getStore {
        CounterStoreFactory(
            storeFactory = storeFactory
        ).create()
    }

    fun onViewCreated(view: MainView, viewLifecycle: Lifecycle) {
        RxJavaBinder {
            view.observableEvents().subscribe {
                counterStore.accept(it.toIntent())
            }
            counterStore.observableStates()
                .map(MainViewModelMapper()).subscribe {
                    view.render(it)
                }
            counterStore.observableLabels().subscribe {

            }
        }.attachTo(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY)
    }

    private fun MainEvent.toIntent(): CounterStore.Intent {
        return when (this) {
            MainEvent.IncreaseClicked -> CounterStore.Intent.Increase
            MainEvent.DecreaseClicked -> CounterStore.Intent.Decrease
        }
    }

}