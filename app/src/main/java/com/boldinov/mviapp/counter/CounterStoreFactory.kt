package com.boldinov.mviapp.counter

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.boldinov.mviapp.base.state.LoadingState
import com.boldinov.mviapp.base.rx.RxJavaExecutor
import com.boldinov.mviapp.counter.repository.CounterRepository
import com.boldinov.mviapp.counter.repository.RandomCounterRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.Serializable

/**
 * Created by Aleksey Boldinov on 22.08.2022.
 */
class CounterStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): CounterStore {
        return object : CounterStore,
            Store<CounterStore.Intent, CounterStore.State, CounterStore.Label> by storeFactory.create(
                name = "CounterStore",
                initialState = CounterStore.State(),
                executorFactory = {
                    ExecutorImpl(RandomCounterRepository())
                },
                reducer = ReducerImpl()
            ) {}
    }

    private sealed interface Msg : Serializable {
        data class Increased(val counter: Int) : Msg
        data class Decreased(val counter: Int) : Msg
        object Loading : Msg
        data class Error(val throwable: Throwable) : Msg
    }

    private class ExecutorImpl(
        private val repository: CounterRepository
    ) : RxJavaExecutor<CounterStore.Intent, Unit, CounterStore.State, Msg, CounterStore.Label>() {

        override fun executeIntent(
            intent: CounterStore.Intent,
            getState: () -> CounterStore.State
        ) {
            when (intent) {
                is CounterStore.Intent.Increase -> {
                    publish(CounterStore.Label.ChangeStarted)
                    dispatch(Msg.Loading)
                    launch {
                        Observable.fromCallable {
                            repository.getCounterRemote()
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                dispatch(Msg.Increased(it))
                                publish(CounterStore.Label.ChangeFinished)
                            }, {
                                dispatch(Msg.Error(it))
                            })
                    }
                }
                is CounterStore.Intent.Decrease -> {
                    dispatch(Msg.Decreased(1))
                }
                is CounterStore.Intent.ShareCounter -> {
                    publish(CounterStore.Label.ShareCounter(getState().counter))
                }
            }
        }
    }

    private class ReducerImpl : Reducer<CounterStore.State, Msg> {

        override fun CounterStore.State.reduce(msg: Msg): CounterStore.State {
            return when (msg) {
                is Msg.Increased -> copy(
                    counter = counter + msg.counter,
                    loading = LoadingState.Idle
                )
                is Msg.Decreased -> copy(
                    counter = counter - msg.counter,
                    loading = LoadingState.Idle
                )
                is Msg.Loading -> copy(loading = LoadingState.Loading)
                is Msg.Error -> copy(loading = LoadingState.Error(msg.throwable))
            }
        }

    }
}