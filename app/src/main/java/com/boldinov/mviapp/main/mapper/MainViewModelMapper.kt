package com.boldinov.mviapp.main.mapper

import com.boldinov.mviapp.main.MainViewModel
import com.boldinov.mviapp.base.LoadingState
import com.boldinov.mviapp.counter.CounterStore

/**
 * Created by Aleksey Boldinov on 23.08.2022.
 */
class MainViewModelMapper : (CounterStore.State) -> MainViewModel {

    override fun invoke(state: CounterStore.State): MainViewModel {
        return MainViewModel(
            counter = state.counter.toString(),
            isLoading = state.loading == LoadingState.Loading
        )
    }
}