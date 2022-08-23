package com.boldinov.mviapp.counter

import com.arkivanov.mvikotlin.core.store.Store
import com.boldinov.mviapp.base.state.LoadingState
import java.io.Serializable
import com.boldinov.mviapp.counter.CounterStore.Intent
import com.boldinov.mviapp.counter.CounterStore.State
import com.boldinov.mviapp.counter.CounterStore.Label

/**
 *
 * Created by Aleksey Boldinov on 22.08.2022.
 */
interface CounterStore : Store<Intent, State, Label> {

    /**
     * Serializable for for exporting events in Time Travel
     */
    sealed class Intent : Serializable {
        object Increase : Intent()
        object Decrease : Intent()
        object ShareCounter : Intent()
    }

    data class State(
        val counter: Int = 0,
        val loading: LoadingState = LoadingState.Idle
    ) : Serializable

    sealed class Label : Serializable {
        object ChangeStarted : Label()
        object ChangeFinished : Label()
        data class ShareCounter(val counter: Int) : Label()
    }
}